package com.smart.mapper;

import com.smart.dto.RoleDTO;
import com.smart.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {
}