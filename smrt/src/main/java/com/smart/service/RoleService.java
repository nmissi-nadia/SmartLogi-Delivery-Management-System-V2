package com.smartlogi.service;

import com.smartlogi.entity.Role;
import com.smartlogi.repository.RoleRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRoles() { return roleRepository.findAll(); }
    public Role createRole(Role role) { return roleRepository.save(role); }
}
