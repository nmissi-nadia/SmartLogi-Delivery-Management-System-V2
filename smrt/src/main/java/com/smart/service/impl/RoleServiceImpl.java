package com.smart.service.impl;

import com.smart.dto.RoleDTO;
import com.smart.entity.Permission;
import com.smart.entity.Role;
import com.smart.exception.ResourceNotFoundException;
import com.smart.mapper.RoleMapper;
import com.smart.repository.PermissionRepository;
import com.smart.repository.RoleRepository;
import com.smart.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private static final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = roleMapper.toEntity(roleDTO);
        role = roleRepository.save(role);
        log.info("Role created: {}", role.getName());
        return roleMapper.toDto(role);
    }

    @Override
    public void deleteRole(String id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role not found with id: " + id);
        }
        roleRepository.deleteById(id);
        log.info("Role deleted: {}", id);
    }

    @Override
    public RoleDTO addPermissionToRole(String roleId, String permissionId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        Permission permission = permissionRepository.findById(permissionId).orElseThrow(() -> new ResourceNotFoundException("Permission not found"));
        role.getPermissions().add(permission);
        role = roleRepository.save(role);
        log.info("Permission {} added to role {}", permissionId, roleId);
        return roleMapper.toDto(role);
    }

    @Override
    public RoleDTO removePermissionFromRole(String roleId, String permissionId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        Permission permission = permissionRepository.findById(permissionId).orElseThrow(() -> new ResourceNotFoundException("Permission not found"));
        role.getPermissions().remove(permission);
        role = roleRepository.save(role);
        log.info("Permission {} removed from role {}", permissionId, roleId);
        return roleMapper.toDto(role);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> getAllRoles() {
        return roleMapper.toDto(roleRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDTO getRoleById(String id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        return roleMapper.toDto(role);
    }
}
