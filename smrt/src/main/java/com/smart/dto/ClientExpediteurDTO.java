package com.smart.dto;

import lombok.Data;

@Data
public class ClientExpediteurDTO {
    private String id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
}