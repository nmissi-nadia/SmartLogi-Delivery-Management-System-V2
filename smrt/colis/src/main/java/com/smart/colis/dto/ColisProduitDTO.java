package com.smart.colis.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ColisProduitDTO {

    @Valid
    @NotNull
    private ProduitDTO produit;

    @Positive(message = "La quantité doit être positive")
    private int quantite;

    
}
