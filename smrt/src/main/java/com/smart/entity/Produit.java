package com.smart.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "produits")
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String nom;
    private String categorie;
    private Double poids;
    private Double prix;

    @OneToMany(mappedBy = "produit")
    private List<ColisProduit> colisProduits;
}
