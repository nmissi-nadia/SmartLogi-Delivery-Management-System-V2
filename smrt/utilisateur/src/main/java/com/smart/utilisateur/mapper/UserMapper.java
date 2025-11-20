package com.smart.utilisateur.mapper;

import com.smart.common.src.main.java.com.smart.entity.ClientExpediteur;
import com.smart.common.src.main.java.com.smart.entity.Livreur;
import com.smart.common.src.main.java.com.smart.entity.User;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    // Mapping de base pour tous les utilisateurs
    public abstract UserDTO toDto(User user);

    // Mapping spécifique pour le Livreur, qui a une zone assignée
    @Mapping(target = "zoneId", source = "zoneAssignee.id")
    public abstract UserDTO toDto(Livreur livreur);

    // Mapping spécifique pour le ClientExpediteur, qui a une adresse
    public abstract UserDTO toDto(ClientExpediteur clientExpediteur);
}