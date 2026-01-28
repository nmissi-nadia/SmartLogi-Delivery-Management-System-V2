package com.smart.mapper;

import com.smart.dto.ClientExpediteurDTO;
import com.smart.entity.ClientExpediteur;
import com.smart.entity.User;
import com.smart.repository.UserRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ClientExpediteurMapper implements EntityMapper<ClientExpediteurDTO, ClientExpediteur> {

    @Autowired
    protected UserRepository userRepository;

    @Mapping(source = "user.id", target = "userId")
    public abstract ClientExpediteurDTO toDto(ClientExpediteur entity);

    @Mapping(source = "userId", target = "user", qualifiedByName = "mapUserIdToUser")
    public abstract ClientExpediteur toEntity(ClientExpediteurDTO dto);

    @Named("mapUserIdToUser")
    public User mapUserIdToUser(String userId) {
        if (userId == null) {
            return null;
        }
        return userRepository.findById(userId).orElse(null);
    }
}