package com.smart.livraison.mapper;


import com.smart.common.src.main.java.com.smart.entity.Colis;
import com.smart.common.src.main.java.com.smart.entity.HistoriqueLivraison;
import com.smart.livraison.src.main.java.com.smart.livraison.dto.HistoriqueLivraisonDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",imports = {
            com.smart.common.src.main.java.com.smart.Enum.StatutColis.class
        })
public interface HistoriqueLivraisonMapper {
    HistoriqueLivraisonMapper INSTANCE = Mappers.getMapper(HistoriqueLivraisonMapper.class);
    
    @Mapping(source = "colis.id", target = "colisId")
    @Mapping(target = "statutLibelle", expression = "java(entity.getStatut().toString())")
    HistoriqueLivraisonDTO toDto(HistoriqueLivraison entity);

    @Mapping(source = "colisId", target = "colis")
    @Mapping(target = "statut", expression = "java(com.smart.common.src.main.java.com.smart.Enum.StatutColis.valueOf(dto.getStatut()))")
    @Mapping(target = "id", ignore = true)  // Ignore ID as it's generated
    @Mapping(target = "dateChangement", ignore = true)  // Will be set by @PrePersist
    HistoriqueLivraison toEntity(HistoriqueLivraisonDTO dto);

    default Colis mapColis(String colisId) {
        if (colisId == null) {
            return null;
        }
        Colis colis = new Colis();
        colis.setId(colisId);
        return colis;
    }
}