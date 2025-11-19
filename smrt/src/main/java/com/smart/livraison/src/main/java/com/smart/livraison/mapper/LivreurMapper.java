package com.smart.livraison.src.main.java.com.smart.livraison.mapper;

import com.smart.dto.LivreurDTO;
import com.smart.entity.Livreur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LivreurMapper {

    @Mapping(source = "zoneAssignee.id", target = "zoneAssigneeId")
    LivreurDTO toDto(Livreur entity);

    @Mapping(source = "zoneAssigneeId", target = "zoneAssignee.id")
    Livreur toEntity(LivreurDTO dto);
}
