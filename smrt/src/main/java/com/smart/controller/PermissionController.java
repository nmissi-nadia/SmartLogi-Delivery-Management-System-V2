package com.smart.controller;

import com.smart.dto.PermissionDTO;
import com.smart.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Permissions", description = "API pour la gestion des permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_PERMISSIONS')")
    @Operation(summary = "Lister toutes les permissions", description = "Récupère la liste de toutes les permissions.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des permissions récupérée avec succès"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    public ResponseEntity<List<PermissionDTO>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MANAGE_PERMISSIONS')")
    @Operation(summary = "Créer une nouvelle permission", description = "Crée une nouvelle permission.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Permission créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    public ResponseEntity<PermissionDTO> createPermission(@RequestBody PermissionDTO permissionDTO) {
        return new ResponseEntity<>(permissionService.createPermission(permissionDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_PERMISSIONS')")
    @Operation(summary = "Supprimer une permission", description = "Supprime une permission par son ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Permission supprimée avec succès"),
            @ApiResponse(responseCode = "403", description = "Accès refusé"),
            @ApiResponse(responseCode = "404", description = "Permission non trouvée")
    })
    public ResponseEntity<Void> deletePermission(@Parameter(description = "ID of the permission to delete") @PathVariable String id) {
        permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }
}