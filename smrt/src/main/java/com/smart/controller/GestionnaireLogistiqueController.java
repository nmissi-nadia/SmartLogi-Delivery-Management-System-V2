package com.smart.controller;

import com.smart.dto.ColisDTO;
import com.smart.entity.GestionnaireLogistique;
import com.smart.entity.Enum.StatutColis;
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
    // Lister les colis avec statut CREE et livreur null
    @GetMapping("/colis")
    public List<ColisDTO> getAllColis() {
        return colisService.findAll();
    }
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
    public ResponseEntity<Map<String, Object>> groupColisBy(@PathVariable String field) {
        return ResponseEntity.ok(colisService.groupBy(field));
    }
    // Rechercher des colis
    @GetMapping("/colis/recherche")
    public ResponseEntity<List<ColisDTO>> rechercherColis(
            @RequestParam(required = false) String statut,
            @RequestParam(required = false) String ville,
            @RequestParam(required = false) String priorite) {
        return ResponseEntity.ok(colisService.findByCritere(statut, ville, priorite));
    }
    @PutMapping("/colis/{colisId}/traiter")
    public ResponseEntity<ColisDTO> traiterColis(
            @PathVariable String colisId,
            @RequestParam StatutColis statut,
            @RequestParam(required = false) String commentaire) {
        return ResponseEntity.ok(colisService.updateStatus(colisId, statut.toString(), commentaire));
    }
    
}