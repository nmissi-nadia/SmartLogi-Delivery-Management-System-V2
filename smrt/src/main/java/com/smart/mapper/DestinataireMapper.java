package com.smart.mapper;

import com.smart.dto.DestinataireDTO;
import com.smart.entity.Destinataire;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DestinataireMapper {
    DestinataireDTO toDto(Destinataire entity);
    @Mapping(target = "colisRecus", ignore = true)
    Destinataire toEntity(DestinataireDTO dto);
}