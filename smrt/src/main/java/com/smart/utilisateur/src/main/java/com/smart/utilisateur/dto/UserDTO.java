package com.smart.utilisateur.src.main.java.com.smart.utilisateur.dto;

import com.smart.entity.Enum.Role;
import lombok.Data;

@Data
public class UserDTO {
    private String id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private Role role;
    private String adresse;
    private String ville;
    private Boolean disponibilite;
    private String vehicule;
    private String zoneId;
}