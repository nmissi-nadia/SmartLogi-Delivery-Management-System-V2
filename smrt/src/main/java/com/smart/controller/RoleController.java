package com.smart.controller;

import com.smart.dto.RoleDTO;
import com.smart.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Tag(name = "Roles", description = "API for Role management")
@SecurityRequirement(name = "bearerAuth")
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
    @Operation(summary = "Get all roles")
    public List<RoleDTO> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PostMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
    @Operation(summary = "Assign a permission to a role")
    public ResponseEntity<Void> assignPermissionToRole(@PathVariable String roleId, @PathVariable String permissionId) {
        roleService.assignPermissionToRole(roleId, permissionId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
    @Operation(summary = "Revoke a permission from a role")
    public ResponseEntity<Void> revokePermissionFromRole(@PathVariable String roleId, @PathVariable String permissionId) {
        roleService.revokePermissionFromRole(roleId, permissionId);
        return ResponseEntity.ok().build();
    }
}