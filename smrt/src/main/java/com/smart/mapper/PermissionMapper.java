package com.smart.mapper;

import com.smart.dto.PermissionDTO;
import com.smart.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper extends EntityMapper<PermissionDTO, Permission> {
}
