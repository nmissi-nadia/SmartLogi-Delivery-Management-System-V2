package com.smart.colis.src.main.java.com.smart.colis.mapper;

import com.smart.dto.ColisDTO;
import com.smart.entity.Colis;
import com.smart.entity.Enum.PrioriteEnum;
import com.smart.entity.Enum.StatutColis;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ZoneMapper.class}, imports = {PrioriteEnum.class, StatutColis.class})
public interface ColisMapper extends EntityMapper<ColisDTO, Colis> {

    @Mapping(source = "zoneLivraison.id", target = "zoneId")
    @Mapping(source = "livreur.id", target = "livreurId")
    @Mapping(source = "clientExpediteur.id", target = "clientExpediteurId")
    @Mapping(source = "destinataire.id", target = "destinataireId")
    ColisDTO toDto(Colis colis);

    @Mapping(source = "zoneId", target = "zoneLivraison.id")
    @Mapping(source = "livreurId", target = "livreur.id")
    @Mapping(source = "clientExpediteurId", target = "clientExpediteur.id")
    @Mapping(source = "destinataireId", target = "destinataire.id")
    Colis toEntity(ColisDTO colisDTO);

    default StatutColis mapToStatutColis(String value) {
        return value != null ? StatutColis.valueOf(value) : null;
    }
    default PrioriteEnum mapToPrioriteEnum(String value) {
        return value != null ? PrioriteEnum.valueOf(value) : null;
    }
}