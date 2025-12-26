package com.smart.controller;

import com.smart.dto.HistoriqueLivraisonDTO;
import com.smart.service.HistoriqueLivraisonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/historiques")
@RequiredArgsConstructor
@Tag(name = "Historique Livraison", description = "API pour la gestion de l'historique des livraisons")
@SecurityRequirement(name = "bearerAuth")
public class HistoriqueLivraisonController {
    private final HistoriqueLivraisonService service;

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_COLIS')")
    @Operation(summary = "Récupérer tout l'historique des livraisons")
    public List<HistoriqueLivraisonDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VIEW_COLIS')")
    @Operation(summary = "Récupérer un historique de livraison par son ID")
    public ResponseEntity<HistoriqueLivraisonDTO> getById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MANAGE_COLIS')")
    @Operation(summary = "Créer un nouvel historique de livraison")
    public HistoriqueLivraisonDTO create(@RequestBody HistoriqueLivraisonDTO dto) {
        return service.save(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_COLIS')")
    @Operation(summary = "Mettre à jour un historique de livraison existant")
    public ResponseEntity<HistoriqueLivraisonDTO> update(@PathVariable String id, @RequestBody HistoriqueLivraisonDTO dto) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        dto.setId(id);
        return ResponseEntity.ok(service.save(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_COLIS')")
    @Operation(summary = "Supprimer un historique de livraison")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}