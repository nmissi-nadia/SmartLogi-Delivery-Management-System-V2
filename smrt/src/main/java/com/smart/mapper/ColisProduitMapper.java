package com.smart.mapper;

import com.smart.dto.ColisProduitDTO;
import com.smart.entity.Colis;
import com.smart.entity.ColisProduit;
import com.smart.entity.Produit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ColisProduitMapper extends EntityMapper<ColisProduitDTO, ColisProduit> {
    @Mapping(target = "id", ignore = true) // On gère l'ID manuellement
    @Mapping(target = "colis", source = "colis")
    @Mapping(target = "produit", source = "produit")
    @Mapping(target = "quantite", source = "dto.quantite")
    @Mapping(target = "prix", expression = "java(produit.getPrix() * dto.getQuantite())")
    @Mapping(target = "dateAjout", expression = "java(java.time.LocalDateTime.now())")
    ColisProduit toEntity(ColisProduitDTO dto, Colis colis, Produit produit);
}
