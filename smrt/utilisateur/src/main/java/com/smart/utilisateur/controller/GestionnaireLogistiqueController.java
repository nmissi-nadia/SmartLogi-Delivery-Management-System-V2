package com.smart.utilisateur.controller;


import com.smart.colis.src.main.java.com.smart.colis.dto.ColisDTO;
import com.smart.colis.src.main.java.com.smart.colis.service.ColisService;
import com.smart.common.src.main.java.com.smart.Enum.StatutColis;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.dto.UserDTO;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.service.GestionnaireLogistiqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/gestionnaires")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MANAGER')")
public class GestionnaireLogistiqueController {
    private final GestionnaireLogistiqueService service;
    private final ColisService colisService;

    @GetMapping
    public List<UserDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // =================== Gestion des Colis ===================
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
            @RequestBody Map<String, Object> request) {
        
        StatutColis statut = StatutColis.valueOf((String) request.get("statut"));
        String commentaire = (String) request.get("commentaire");
        
        return ResponseEntity.ok(colisService.updateStatus(
            colisId, 
            statut.toString(), 
            commentaire
        ));
    }
    
}