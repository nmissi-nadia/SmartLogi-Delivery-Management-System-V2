package com.smart.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProduitDTO {
    private String id;
    private String nom;
    private String categorie;
    private BigDecimal poids;
    private Double prix;
}