package com.smart.controller;

import com.smart.dto.ColisDTO;
import com.smart.dto.DestinataireDTO;
import com.smart.service.ColisService;
import com.smart.service.DestinataireService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/destinataires")
@RequiredArgsConstructor
@Tag(name = "Destinataire", description = "API pour la gestion des destinataires")
@SecurityRequirement(name = "bearerAuth")
public class DestinataireController {
    private final DestinataireService service;
    private final ColisService colisService;


    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_USERS')")
    @Operation(summary = "Récupérer tous les destinataires")
    public List<DestinataireDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VIEW_USERS')")
    @Operation(summary = "Récupérer un destinataire par son ID")
    public ResponseEntity<DestinataireDTO> getById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    @Operation(summary = "Créer un nouveau destinataire")
    public DestinataireDTO create(@RequestBody DestinataireDTO dto) {
        return service.save(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    @Operation(summary = "Mettre à jour un destinataire existant")
    public ResponseEntity<DestinataireDTO> update(@PathVariable String id, @RequestBody DestinataireDTO dto) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        dto.setId(id);
        return ResponseEntity.ok(service.save(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    @Operation(summary = "Supprimer un destinataire")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    // colis
    // Voir les détails d'un colis
    @GetMapping("/{destinataireId}/colis/{colisId}")
    @PreAuthorize("hasAuthority('VIEW_COLIS_DESTINATAIRE')")
    @Operation(summary = "Voir les détails d'un colis pour un destinataire")
    public ResponseEntity<ColisDTO> viewColis(
            @PathVariable String destinataireId,
            @PathVariable String colisId) {
        return ResponseEntity.ok(colisService.findById(colisId)
                .orElseThrow(() -> new EntityNotFoundException("Colis non trouvé")));
    }

    // Confirmer réception
    @PostMapping("/{destinataireId}/colis/{colisId}/confirmation")
    @PreAuthorize("hasAuthority('MANAGE_COLIS_DESTINATAIRE')")
    @Operation(summary = "Confirmer la réception d'un colis pour un destinataire")
    public ResponseEntity<ColisDTO> confirmReception(
            @PathVariable String destinataireId,
            @PathVariable String colisId) {
        return ResponseEntity.ok(colisService.updateStatus(colisId, "LIVRE", "Livré avec succès"));
    }
}
