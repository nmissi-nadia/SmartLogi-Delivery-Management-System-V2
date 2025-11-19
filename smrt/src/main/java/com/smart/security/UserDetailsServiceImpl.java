package com.smart.security;

import com.smart.common.src.main.java.com.smart.entity.User;
import com.smart.utilisateur.*;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    // Le nom du repository doit correspondre à l'interface que vous avez créée.
    private final UserRepository utilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // On cherche l'utilisateur par son email, qui est notre "username"
        User utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email));
        
        // On crée la liste des rôles (authorities) pour Spring Security
        // Spring Security ajoute automatiquement le préfixe "ROLE_"
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + utilisateur.getRole().name()));
        
        // On retourne un objet UserDetails que Spring Security peut utiliser
        return new org.springframework.security.core.userdetails.User(utilisateur.getEmail(), utilisateur.getPassword(), authorities);
    }
}
