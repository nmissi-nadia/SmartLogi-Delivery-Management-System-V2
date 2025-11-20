package com.smart.zone.mapper;


import com.smart.common.src.main.java.com.smart.entity.Zone;
import com.smart.zone.src.main.java.com.smart.zone.dto.ZoneDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ZoneMapper {
    ZoneDTO toDto(Zone entity);
    Zone toEntity(ZoneDTO dto);
}