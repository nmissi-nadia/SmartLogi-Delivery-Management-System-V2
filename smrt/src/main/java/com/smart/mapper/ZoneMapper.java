package com.smart.mapper;

import com.smart.dto.ZoneDTO;
import com.smart.entity.Zone;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ZoneMapper {
    ZoneDTO toDto(Zone entity);
    Zone toEntity(ZoneDTO dto);
}