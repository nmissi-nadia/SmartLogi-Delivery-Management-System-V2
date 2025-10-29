package com.smart.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "colis_produits")
public class ColisProduit {

    @EmbeddedId
    private ColisProduitKey id = new ColisProduitKey();

    @ManyToOne
    @MapsId("colisId")
    @JoinColumn(name = "colis_id")
    private Colis colis;

    @ManyToOne
    @MapsId("produitId")
    @JoinColumn(name = "produit_id")
    private Produit produit;

    private int quantite;
    private Double prix;
}
