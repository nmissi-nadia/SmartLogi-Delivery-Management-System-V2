package com.smart.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ColisProduitKey implements Serializable {

    @Column(name = "id_colis")
    private String colisId;

    @Column(name = "id_produit")
    private String produitId;
}