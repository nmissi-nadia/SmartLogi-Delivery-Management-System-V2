package com.smart.mapper;

import com.smart.dto.DestinataireDTO;
import com.smart.entity.Destinataire;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DestinataireMapper {
    DestinataireDTO toDto(Destinataire entity);
    Destinataire toEntity(DestinataireDTO dto);
}