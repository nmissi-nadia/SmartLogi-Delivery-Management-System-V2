package com.smart.livraison.src.main.java.com.smart.livraison.dto;

import lombok.Data;

@Data
public class LivreurDTO {
    private String id;
    private String nom;
    private String prenom;
    private String telephone;
    private String vehicule;
    private String zoneAssigneeId;
}
