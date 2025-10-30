package com.smart.mapper;

import com.smart.dto.HistoriqueLivraisonDTO;
import com.smart.entity.HistoriqueLivraison;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HistoriqueLivraisonMapper {

    @Mapping(source = "colis.id", target = "colisId")
    HistoriqueLivraisonDTO toDto(HistoriqueLivraison entity);

    @Mapping(source = "colisId", target = "colis.id")
    HistoriqueLivraison toEntity(HistoriqueLivraisonDTO dto);
}