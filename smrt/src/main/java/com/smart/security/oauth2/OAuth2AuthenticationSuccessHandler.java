package com.smart.security.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.entity.CustomUserDetails;
import com.smart.entity.Enum.Provider;
import com.smart.entity.Role;
import com.smart.entity.User;
import com.smart.repository.RoleRepository;
import com.smart.repository.UserRepository;
import com.smart.security.JwtTokenProvider;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OAuth2AuthenticationSuccessHandler(JwtTokenProvider tokenProvider, UserRepository userRepository,RoleRepository roleRepository) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

        // Identifier le provider (google, okta, facebook…)
        String registrationId =
                ((OAuth2AuthenticationToken) authentication)
                        .getAuthorizedClientRegistrationId();

        Provider provider = Provider.valueOf(registrationId.toUpperCase());

        // Récupération email (standard OIDC)
        String email = (String) oauth2User.getAttributes().get("email");

        if (email == null) {
            email = (String) oauth2User.getAttributes().get("preferred_username");
        }

        if (email == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Email non fourni par le provider OAuth2");
            return;
        }

        User user = userRepository.findByEmail(email);

        if (user == null) {
            user = new User();
            user.setEmail(email);

            user.setUsername(
                    oauth2User.getAttribute("name") != null
                            ? oauth2User.getAttribute("name")
                            : email
            );

            user.setFirstName(oauth2User.getAttribute("given_name"));
            user.setLastName(oauth2User.getAttribute("family_name"));

            user.setPassword(null); // OAuth2 → pas de mot de passe
            user.setProvider(provider);
            user.setProviderId(oauth2User.getAttribute("sub"));

            // Rôle par défaut
            Role defaultRole = roleRepository.findByName("CLIENT_EXPEDITEUR");
            if (defaultRole == null) {
                throw new IllegalStateException("Rôle CLIENT_EXPEDITEUR introuvable");
            }

            user.setRoles(Set.of(defaultRole));
            user = userRepository.save(user);
        }

        UserDetails userDetails = new CustomUserDetails(user);

        Authentication jwtAuth =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        String jwt = tokenProvider.generateToken(jwtAuth);

        Map<String, String> tokenResponse = new HashMap<>();
        tokenResponse.put("token", jwt);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), tokenResponse);
    }

}
