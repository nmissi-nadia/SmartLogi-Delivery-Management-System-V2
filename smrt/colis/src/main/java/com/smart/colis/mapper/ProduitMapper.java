package com.smart.colis.mapper;


import com.smart.colis.src.main.java.com.smart.colis.dto.ProduitDTO;
import com.smart.common.src.main.java.com.smart.entity.Produit;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProduitMapper {
    ProduitDTO toDto(Produit entity);
    Produit toEntity(ProduitDTO dto);
}
