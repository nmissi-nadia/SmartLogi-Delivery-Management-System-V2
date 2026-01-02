package com.smart.mapper;

import com.smart.dto.LivreurDTO;
import com.smart.entity.Livreur;
import com.smart.entity.User;
import com.smart.entity.Zone;
import com.smart.repository.UserRepository;
import com.smart.service.ZoneService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class LivreurMapper implements EntityMapper<LivreurDTO, Livreur> {

    @Autowired
    protected ZoneService zoneService;

    @Mapping(source = "zone.id", target = "zoneId")
    @Mapping(source = "user.id", target = "userId") // Assuming LivreurDTO has userId
    public abstract LivreurDTO toDto(Livreur entity);

    @Mapping(source = "zoneId", target = "zone", qualifiedByName = "mapZoneIdToZone")
    @Mapping(source = "userId", target = "user", qualifiedByName = "mapUserIdToUser")
    public abstract Livreur toEntity(LivreurDTO dto);

    @Named("mapZoneIdToZone")
    public Zone mapZoneIdToZone(String zoneId) {
        if (zoneId == null) {
            return null;
        }
        return zoneService.findEntityById(zoneId).orElse(null);
    }

    @Autowired
    protected UserRepository userRepository;

    @Named("mapUserIdToUser")
    public User mapUserIdToUser(String userId) {
        if (userId == null) {
            return null;
        }
        return userRepository.findById(userId).orElse(null);
    }
}
