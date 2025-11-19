package com.smart.utilisateur.src.main.java.com.smart.utilisateur.mapper;

import com.smart.dto.ClientExpediteurDTO;
import com.smart.entity.ClientExpediteur;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientExpediteurMapper extends EntityMapper<ClientExpediteurDTO, ClientExpediteur> {
}