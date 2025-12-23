package com.smart.mapper;

import com.smart.dto.LivreurDTO;
import com.smart.entity.Livreur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ZoneMapper.class})
public interface LivreurMapper extends EntityMapper<LivreurDTO, Livreur> {

    @Mapping(source = "zone.id", target = "zoneId")
    LivreurDTO toDto(Livreur entity);

    @Mapping(source = "zoneId", target = "zone")
    Livreur toEntity(LivreurDTO dto);
}
