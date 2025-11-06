package com.smart.controller;

import com.smart.dto.ColisDTO;
import com.smart.entity.GestionnaireLogistique;
import com.smart.service.ColisService;
import com.smart.service.GestionnaireLogistiqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/gestionnaires")
@RequiredArgsConstructor
public class GestionnaireLogistiqueController {
    private final GestionnaireLogistiqueService service;
    private final ColisService colisService;

    @GetMapping
    public List<GestionnaireLogistique> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GestionnaireLogistique> getById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public GestionnaireLogistique create(@RequestBody GestionnaireLogistique gestionnaire) {
        return service.save(gestionnaire);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GestionnaireLogistique> update(@PathVariable String id, @RequestBody GestionnaireLogistique gestionnaire) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        gestionnaire.setId(id);
        return ResponseEntity.ok(service.save(gestionnaire));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // gestion des colis 
    // Assigner un livreur
    @PostMapping("/colis/{colisId}/assigner")
    public ResponseEntity<ColisDTO> assignerLivreur(
            @PathVariable String colisId,
            @RequestParam String livreurId) {
        return ResponseEntity.ok(colisService.assignLivreur(colisId, livreurId));
    }

    // Obtenir des statistiques
    @GetMapping("/statistiques")
    public ResponseEntity<Map<String, Object>> getStatistiques(
            @RequestParam(required = false) String livreurId,
            @RequestParam(required = false) String zoneId) {
        return ResponseEntity.ok(colisService.getStatistiques(livreurId, zoneId));
    }

    // Grouper les colis
    @GetMapping("/colis/group-by/{field}")
    public ResponseEntity<Map<String, Long>> groupColisBy(@PathVariable String field) {
        return ResponseEntity.ok(colisService.groupBy(field));
    }
}