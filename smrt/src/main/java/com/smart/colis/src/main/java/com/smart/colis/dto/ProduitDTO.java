package com.smart.colis.src.main.java.com.smart.colis.dto;

import lombok.Data;

@Data
public class ProduitDTO {
    private String id;
    private String nom;
    private String categorie;
    private Double poids;
    private Double prix;
}