package com.smart.controller;

import com.smart.dto.RoleDTO;
import com.smart.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of roles"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public List<RoleDTO> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PostMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
    @Operation(summary = "Assign a permission to a role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permission successfully assigned to role"),
            @ApiResponse(responseCode = "404", description = "Role or Permission not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Void> assignPermissionToRole(
            @Parameter(description = "ID of the role to which the permission will be assigned") @PathVariable String roleId,
            @Parameter(description = "ID of the permission to assign") @PathVariable String permissionId) {
        roleService.addPermissionToRole(roleId, permissionId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasAuthority('MANAGE_ROLES')")
    @Operation(summary = "Revoke a permission from a role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permission successfully revoked from role"),
            @ApiResponse(responseCode = "404", description = "Role or Permission not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<Void> revokePermissionFromRole(
            @Parameter(description = "ID of the role from which the permission will be revoked") @PathVariable String roleId,
            @Parameter(description = "ID of the permission to revoke") @PathVariable String permissionId) {
        roleService.removePermissionFromRole(roleId, permissionId);
        return ResponseEntity.ok().build();
    }
}