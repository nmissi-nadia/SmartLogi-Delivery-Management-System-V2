package com.smart.controller;

import com.smart.dto.ColisDTO;
import com.smart.dto.HistoriqueLivraisonDTO;
import com.smart.entity.HistoriqueLivraison;
import com.smart.service.ColisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/colis")
@RequiredArgsConstructor
@Tag(name = "Colis", description = "API for Colis management")
@SecurityRequirement(name = "bearerAuth")
public class ColisController {
    private final ColisService colisService;
    private static final Logger log = LoggerFactory.getLogger(ColisController.class);

    @PostMapping
    @PreAuthorize("hasRole('CLIENT') or hasRole('GESTIONNAIRE_LOGISTIQUE')")
    @Operation(summary = "Create a new colis")
    public ColisDTO createColis(@RequestBody ColisDTO colisDTO) {
        return colisService.save(colisDTO);
    }

    @GetMapping
    @PreAuthorize("hasRole('GESTIONNAIRE_LOGISTIQUE')")
    @Operation(summary = "Get all colis")
    public Page<ColisDTO> getAll(@RequestParam(required = false) String statut,
                                @RequestParam(required = false) String ville,
                                @RequestParam(required = false) String priorite,
                                @RequestParam(required = false) String zoneId,
                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
                                @RequestParam(required = false) String clientId,
                                @RequestParam(required = false) String destinataireId,
                                @RequestParam(required = false) String livreurId,
                                Pageable pageable) {
        log.debug("Récupération de tous les colis");
        LocalDateTime startOfDay = dateDebut != null ? dateDebut.atStartOfDay() : null;
        LocalDateTime endOfDay = dateFin != null ? dateFin.atTime(23, 59, 59) : null;
        return colisService.findAll(statut, ville, priorite, zoneId, startOfDay, endOfDay, pageable, clientId, destinataireId, livreurId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CLIENT') or hasRole('GESTIONNAIRE_LOGISTIQUE') or hasRole('LIVREUR')")
    @Operation(summary = "Get a colis by ID")
    public ResponseEntity<ColisDTO> getColisById(@PathVariable String id) {
        return colisService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('GESTIONNAIRE_LOGISTIQUE')")
    @Operation(summary = "Update an existing colis")
    public ResponseEntity<ColisDTO> updateColis(
            @PathVariable String id,
            @RequestBody ColisDTO colisDTO) {
        return ResponseEntity.ok(colisService.update(id, colisDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GESTIONNAIRE_LOGISTIQUE')")
    @Operation(summary = "Delete a colis")
    public ResponseEntity<Void> deleteColis(@PathVariable String id) {
        colisService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recherche")
    @PreAuthorize("hasRole('GESTIONNAIRE_LOGISTIQUE')")
    @Operation(summary = "Search for colis")
    public ResponseEntity<Page<ColisDTO>> searchColis(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String statut,
            @RequestParam(required = false) String ville,
            @RequestParam(required = false) String priorite,
            Pageable pageable) {
        return ResponseEntity.ok(colisService.searchByKeyword(keyword, pageable));
    }

    @GetMapping("/statistiques/overview")
    @PreAuthorize("hasRole('GESTIONNAIRE_LOGISTIQUE')")
    @Operation(summary = "Get an overview of the colis statistics")
    public ResponseEntity<Map<String, Long>> getStatistiquesOverview() {
        return ResponseEntity.ok(colisService.getStatistiquesOverview());
    }

    @PostMapping("/assign")
    @PreAuthorize("hasRole('GESTIONNAIRE_LOGISTIQUE')")
    @Operation(summary = "Assign a livreur to a colis")
    public ResponseEntity<Void> assignColis(@RequestBody Map<String, String> assignment) {
        colisService.assignLivreurToColis(assignment.get("colisId"), assignment.get("livreurId"));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/non-assignes")
    @PreAuthorize("hasRole('GESTIONNAIRE_LOGISTIQUE')")
    @Operation(summary = "Get all non-assigned colis")
    public ResponseEntity<List<ColisDTO>> getNonAssignedColis() {
        return ResponseEntity.ok(colisService.findAllNonAssigned());
    }

    // Obtenir l'historique d'un colis
    @GetMapping("/{colisId}/historique")
    @PreAuthorize("hasRole('CLIENT') or hasRole('GESTIONNAIRE_LOGISTIQUE') or hasRole('LIVREUR')")
    @Operation(summary = "Get the history of a colis")
    public ResponseEntity<List<HistoriqueLivraisonDTO>> getHistoriqueColis(
            @PathVariable String colisId) {
        List<HistoriqueLivraisonDTO> historique = colisService.getHistoriqueForColis(colisId);
        return ResponseEntity.ok(historique); 
    }
}