package com.smart.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ColisProduit {

    @EmbeddedId
    private ColisProduitKey id;

    @ManyToOne
    @MapsId("colisId")
    @JoinColumn(name = "id_colis")
    private Colis colis;

    @ManyToOne
    @MapsId("produitId")
    @JoinColumn(name = "id_produit")
    private Produit produit;

    private Integer quantite;
    private Double prix; // Prix au moment de l'ajout
    private LocalDateTime dateAjout;
}