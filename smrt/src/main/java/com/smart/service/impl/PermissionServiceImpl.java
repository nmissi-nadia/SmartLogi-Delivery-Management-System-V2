package com.smart.service.impl;

import com.smart.dto.PermissionDTO;
import com.smart.entity.Permission;
import com.smart.mapper.PermissionMapper;
import com.smart.repository.PermissionRepository;
import com.smart.service.PermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

    private static final Logger log = LoggerFactory.getLogger(PermissionServiceImpl.class);

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public PermissionServiceImpl(PermissionRepository permissionRepository, PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public PermissionDTO createPermission(PermissionDTO permissionDTO) {
        Permission permission = permissionMapper.toEntity(permissionDTO);
        permission = permissionRepository.save(permission);
        log.info("Permission created: {}", permission.getName());
        return permissionMapper.toDto(permission);
    }

    @Override
    public void deletePermission(String id) {
        if (!permissionRepository.existsById(id)) {
            throw new com.smart.exception.ResourceNotFoundException("Permission not found with id: " + id);
        }
        permissionRepository.deleteById(id);
        log.info("Permission deleted: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PermissionDTO> getAllPermissions() {
        return permissionMapper.toDto(permissionRepository.findAll());
    }
}
