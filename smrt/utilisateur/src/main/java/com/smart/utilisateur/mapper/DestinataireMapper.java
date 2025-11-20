package com.smart.utilisateur.mapper;


import com.smart.common.src.main.java.com.smart.entity.Destinataire;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.dto.DestinataireDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DestinataireMapper {
    DestinataireDTO toDto(Destinataire entity);
    @Mapping(target = "colisRecus", ignore = true)
    Destinataire toEntity(DestinataireDTO dto);
}