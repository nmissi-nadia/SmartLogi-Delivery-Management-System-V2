package com.smart.service;

import com.smart.dto.PermissionDTO;
import java.util.List;

public interface PermissionService {
    PermissionDTO createPermission(PermissionDTO permissionDTO);
    void deletePermission(String id);
    List<PermissionDTO> getAllPermissions();
}
