package com.smart.colis.mapper;


import com.smart.colis.src.main.java.com.smart.colis.dto.ColisProduitDTO;
import com.smart.common.src.main.java.com.smart.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ColisProduitMapper extends EntityMapper<ColisProduitDTO, ColisProduit> {
    @Mapping(target = "id", ignore = true) // On gère l'ID manuellement
    @Mapping(target = "colis", source = "colis")
    @Mapping(target = "produit", source = "produit")
    @Mapping(target = "quantite", source = "dto.quantite")
    @Mapping(target = "prix", expression = "java(produit.getPrix() * dto.getQuantite())")
    @Mapping(target = "dateAjout", expression = "java(java.time.LocalDate.now())")
    ColisProduit toEntity(ColisProduitDTO dto, Colis colis, Produit produit);
}
