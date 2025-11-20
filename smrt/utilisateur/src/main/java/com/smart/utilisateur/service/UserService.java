package com.smart.utilisateur.service;

import com.smart.common.src.main.java.com.smart.Enum.Role;
import com.smart.common.src.main.java.com.smart.entity.ClientExpediteur;
import com.smart.common.src.main.java.com.smart.entity.GestionnaireLogistique;
import com.smart.common.src.main.java.com.smart.entity.Livreur;
import com.smart.common.src.main.java.com.smart.entity.User;
import com.smart.common.src.main.java.com.smart.entity.Zone;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.dto.UserDTO;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.repository.ClientExpediteurRepository;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.repository.GestionnaireLogistiqueRepository;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.repository.LivreurRepository;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.repository.UserRepository;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final LivreurRepository livreurRepository;
    private final GestionnaireLogistiqueRepository gestionnaireLogistiqueRepository;
    private final ClientExpediteurRepository clientExpediteurRepository;
    private final PasswordEncoder passwordEncoder;
    private final ZoneRepository zoneRepository;
    private final UserMapper userMapper;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Un utilisateur avec cet email existe déjà.");
        }

        if (userDTO.getRole() == Role.LIVREUR) {
            Livreur livreur = new Livreur();
            livreur.setEmail(userDTO.getEmail());
            livreur.setNom(userDTO.getNom());
            livreur.setPrenom(userDTO.getPrenom());
            livreur.setPassword(passwordEncoder.encode("password")); // Mot de passe par défaut ou généré
            livreur.setTelephone(userDTO.getTelephone());
            livreur.setVehicule(userDTO.getVehicule());
            // Gérer l'assignation de la zone
            if (userDTO.getZoneId() != null) {
                Zone zone = zoneRepository.findById(userDTO.getZoneId()).orElseThrow(() -> new EntityNotFoundException("Zone non trouvée avec l'ID : " + userDTO.getZoneId()));
                livreur.setZoneAssignee(zone);
            }
            Livreur savedLivreur = livreurRepository.save(livreur);
            return userMapper.toDto(savedLivreur);
        } else if (userDTO.getRole() == Role.MANAGER) {
            GestionnaireLogistique manager = new GestionnaireLogistique();
            manager.setEmail(userDTO.getEmail());
            manager.setNom(userDTO.getNom());
            manager.setPrenom(userDTO.getPrenom());
            manager.setPassword(passwordEncoder.encode("password")); // Mot de passe par défaut ou généré
            GestionnaireLogistique savedManager = gestionnaireLogistiqueRepository.save(manager);
            return userMapper.toDto(savedManager);
        } else if (userDTO.getRole() == Role.CLIENT) {
            ClientExpediteur client = new ClientExpediteur();
            client.setEmail(userDTO.getEmail());
            client.setNom(userDTO.getNom());
            client.setPrenom(userDTO.getPrenom());
            client.setPassword(passwordEncoder.encode("password")); // Mot de passe par défaut ou généré
            client.setTelephone(userDTO.getTelephone());
            client.setAdresse(userDTO.getAdresse());
            ClientExpediteur savedClient = clientExpediteurRepository.save(client);
            return userMapper.toDto(savedClient);
        } else {
            throw new IllegalArgumentException("Le rôle spécifié n'est pas valide pour la création d'utilisateur.");
        }
    }
}