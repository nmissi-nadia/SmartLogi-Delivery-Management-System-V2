package com.smart.utilisateur.mapper;


import com.smart.common.src.main.java.com.smart.entity.ClientExpediteur;
import com.smart.common.src.main.java.com.smart.mapper.EntityMapper;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.dto.ClientExpediteurDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientExpediteurMapper extends EntityMapper<ClientExpediteurDTO, ClientExpediteur> {
}