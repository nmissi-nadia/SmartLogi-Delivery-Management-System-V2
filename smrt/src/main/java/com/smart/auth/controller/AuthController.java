package com.smart.auth.controller;

import com.smart.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import com.smart.auth.dto.AuthenticationRequest;
import com.smart.auth.dto.AuthenticationResponse;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, 
                         UserDetailsService userDetailsService, 
                         JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) 
            throws Exception {
        
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), 
                    authenticationRequest.getPassword()
                )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Nom d'utilisateur ou mot de passe incorrect", e);
        }

        final UserDetails userDetails = userDetailsService
            .loadUserByUsername(authenticationRequest.getUsername());
        
        final String jwt = jwtUtil.generateToken(userDetails.getUsername()); // Utiliser getUsername() au lieu de l'objet complet

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}



