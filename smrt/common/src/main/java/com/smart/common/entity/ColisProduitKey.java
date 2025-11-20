package com.smart.common.entity;

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

    @Column(name = "colis_id")
    private String colisId;

    @Column(name = "produit_id")
    private String produitId;
}