package com.smart.common.src.main.java.com.smart.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ColisProduit {

    @EmbeddedId
    private ColisProduitKey id;

    @ManyToOne
    @MapsId("colisId")
    @JoinColumn(name = "colis_id")
    private Colis colis;

    @ManyToOne
    @MapsId("produitId")
    @JoinColumn(name = "produit_id")
    private Produit produit;

    private Integer quantite;
    private Double prix; // Prix au moment de l'ajout
    private LocalDate dateAjout;
}