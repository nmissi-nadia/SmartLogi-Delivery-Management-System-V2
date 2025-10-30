package com.smart.mapper;

import com.smart.dto.ColisDTO;
import com.smart.entity.Colis;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {HistoriqueLivraisonMapper.class})
public interface ColisMapper {

    @Mappings({
            @Mapping(source = "livreur.id", target = "livreurId"),
            @Mapping(source = "clientExpediteur.id", target = "clientExpediteurId"),
            @Mapping(source = "destinataire.id", target = "destinataireId"),
            @Mapping(source = "zone.id", target = "zoneId")
    })
    ColisDTO toDto(Colis entity);

    @Mappings({
            @Mapping(source = "livreurId", target = "livreur.id"),
            @Mapping(source = "clientExpediteurId", target = "clientExpediteur.id"),
            @Mapping(source = "destinataireId", target = "destinataire.id"),
            @Mapping(source = "zoneId", target = "zone.id")
    })
    Colis toEntity(ColisDTO dto);
}