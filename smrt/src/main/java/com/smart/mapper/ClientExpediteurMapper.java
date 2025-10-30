package com.smart.mapper;

import com.smart.dto.ClientExpediteurDTO;
import com.smart.entity.ClientExpediteur;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientExpediteurMapper {
    ClientExpediteurDTO toDto(ClientExpediteur entity);
    ClientExpediteur toEntity(ClientExpediteurDTO dto);
}