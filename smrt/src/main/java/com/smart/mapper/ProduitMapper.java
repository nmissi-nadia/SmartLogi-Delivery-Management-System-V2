package com.smart.mapper;

import com.smart.dto.ProduitDTO;
import com.smart.entity.Produit;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProduitMapper {
    ProduitDTO toDto(Produit entity);
    Produit toEntity(ProduitDTO dto);
}
