package com.smart.security.oauth2;

import com.smart.entity.Enum.Provider;
import com.smart.entity.Role;
import com.smart.entity.User;
import com.smart.repository.RoleRepository;
import com.smart.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public CustomOAuth2UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oauth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
        Map<String, Object> attributes = new HashMap<>(oauth2User.getAttributes());

        // Identifier OAuth2
        String providerId = (attributes.containsKey("sub")) ? String.valueOf(attributes.get("sub"))
                : (attributes.containsKey("id")) ? String.valueOf(attributes.get("id")) : null;

        String email = (String) attributes.get("email");
        String firstName = (String) attributes.getOrDefault("given_name", attributes.get("first_name"));
        String lastName = (String) attributes.getOrDefault("family_name", null);

        if ((firstName == null || lastName == null) && attributes.containsKey("name")) {
            String name = (String) attributes.get("name");
            if (name != null && name.contains(" ")) {
                String[] parts = name.split(" ", 2);
                firstName = (firstName == null) ? parts[0] : firstName;
                lastName = (lastName == null) ? parts[1] : lastName;
            }
        }

        // Vérifier si l’utilisateur existe
        User user = null;
        if (email != null) user = userRepository.findByEmail(email);

        if (user == null) {
            user = new User();
            user.setUsername(email != null ? email : UUID.randomUUID().toString());
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPassword(null);
            user.setProvider(Provider.valueOf(registrationId));
            user.setProviderId(providerId);
            user.setEnabled(true);

            // Rôle par défaut
            Role defaultRole = roleRepository.findByName("CLIENT");
            if (defaultRole != null) user.setRoles(new HashSet<>(Collections.singletonList(defaultRole)));

            userRepository.save(user);
        } else {
            // update info si nécessaire
            boolean changed = false;
            if (!user.getProvider().name().equals(registrationId)) {
                user.setProvider(Provider.valueOf(registrationId));
                changed = true;
            }
            if (providerId != null && !providerId.equals(user.getProviderId())) {
                user.setProviderId(providerId);
                changed = true;
            }
            if (firstName != null && !firstName.equals(user.getFirstName())) {
                user.setFirstName(firstName);
                changed = true;
            }
            if (lastName != null && !lastName.equals(user.getLastName())) {
                user.setLastName(lastName);
                changed = true;
            }
            if (changed) userRepository.save(user);
        }

        // Ajouter l’ID local dans les attributs pour le SuccessHandler
        attributes.put("localUserId", user.getId());

        // Authorities
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        if (user.getRoles() != null) {
            user.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
                if (role.getPermissions() != null)
                    role.getPermissions().forEach(p -> authorities.add(new SimpleGrantedAuthority(p.getName())));
            });
        }

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        return new DefaultOAuth2User(authorities, attributes, userNameAttributeName);
    }
}
