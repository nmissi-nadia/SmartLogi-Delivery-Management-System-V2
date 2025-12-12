package com.smart.controller;

import com.smart.dto.ColisDTO;
import com.smart.dto.LivreurDTO;
import com.smart.entity.Livreur;
import com.smart.entity.Zone;
import com.smart.service.ColisService;
import com.smart.service.LivreurService;
import com.smart.service.ZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/livreurs")
@RequiredArgsConstructor
@Tag(name = "Livreur", description = "API for Livreur management")
@SecurityRequirement(name = "bearerAuth")
public class LivreurController {
    private final LivreurService service;
    private final ColisService colisService;
    private final ZoneService zoneService;

    @GetMapping
    @PreAuthorize("hasRole('GESTIONNAIRE_LOGISTIQUE')")
    @Operation(summary = "Get all livreurs")
    public List<LivreurDTO> getAll() {
        return service.findAll();
    }


    @GetMapping("/search")
    @PreAuthorize("hasRole('GESTIONNAIRE_LOGISTIQUE')")
    @Operation(summary = "Search for livreurs by keyword")
    public Page<LivreurDTO> searchByKeyword(@RequestParam String keyword, Pageable pageable) {
        return service.searchByKeyword(keyword, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('GESTIONNAIRE_LOGISTIQUE')")
    @Operation(summary = "Get a livreur by ID")
    public ResponseEntity<LivreurDTO> getById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

   @PostMapping
    @PreAuthorize("hasRole('GESTIONNAIRE_LOGISTIQUE')")
    @Operation(summary = "Create a new livreur")
    public ResponseEntity<LivreurDTO> createLivreur(@RequestBody LivreurDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('GESTIONNAIRE_LOGISTIQUE')")
    @Operation(summary = "Update an existing livreur")
    public ResponseEntity<LivreurDTO> update(@PathVariable String id, @RequestBody LivreurDTO dto) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        dto.setId(id);
        return ResponseEntity.ok(service.save(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GESTIONNAIRE_LOGISTIQUE')")
    @Operation(summary = "Delete a livreur")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    // Colis
    @PutMapping("/{livreurId}/colis/{colisId}/statut")
    @PreAuthorize("hasRole('LIVREUR') or hasRole('GESTIONNAIRE_LOGISTIQUE')")
    @Operation(summary = "Update the status of a colis")
    public ResponseEntity<ColisDTO> updateColisStatus(
            @PathVariable String livreurId,
            @PathVariable String colisId,
            @RequestParam String nouveauStatut) {
        return ResponseEntity.ok(colisService.updateStatus(colisId, nouveauStatut, null));
    }

    // Voir les colis assignés
    @GetMapping("/{livreurId}/colis")
    @PreAuthorize("hasRole('LIVREUR') or hasRole('GESTIONNAIRE_LOGISTIQUE')")
    @Operation(summary = "Get the colis assigned to a livreur")
    public ResponseEntity<Page<ColisDTO>> getColisAssignes(
            @PathVariable String livreurId,
            @RequestParam(required = false) String statut,
            Pageable pageable) {
        return ResponseEntity.ok(colisService.findByLivreurIdAndStatut(livreurId, statut, pageable));
    }

    // statistique de livreur 
    @GetMapping("/{livreurId}/statistiques")
    @PreAuthorize("hasRole('GESTIONNAIRE_LOGISTIQUE')")
    @Operation(summary = "Get the statistics of a livreur")
    public ResponseEntity<Map<String, Object>> getStatistiquesLivreur(
            @PathVariable String livreurId,
            @RequestParam(required = false) String zoneId) {

        // Vérification de l'existence du livreur
        if (!service.findById(livreurId).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> stats = colisService.getStatistiques(livreurId, zoneId);
        return ResponseEntity.ok(stats);
    }
}