package com.smart.service;

import com.smart.dto.RoleDTO;
import java.util.List;

public interface RoleService {
    RoleDTO createRole(RoleDTO roleDTO);
    void deleteRole(String id);
    RoleDTO addPermissionToRole(String roleId, String permissionId);
    RoleDTO removePermissionFromRole(String roleId, String permissionId);
    List<RoleDTO> getAllRoles();
    RoleDTO getRoleById(String id);
}
