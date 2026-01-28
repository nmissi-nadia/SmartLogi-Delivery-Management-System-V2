package com.smart.controller;

import com.smart.dto.ColisDTO;
import com.smart.dto.DestinataireDTO;
import com.smart.service.ColisService;
import com.smart.service.DestinataireService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur public pour le suivi de colis par destinataire
 * Aucune authentification JWT requise
 */
@Slf4j
@RestController
@RequestMapping("/api/public/destinataires")
@RequiredArgsConstructor
@Tag(name = "Destinataire Public", description = "API publique pour le suivi de colis par destinataire")
public class DestinatairePublicController {
    
    private final DestinataireService destinataireService;
    private final ColisService colisService;

    /**
     * Rechercher un destinataire par nom ET email (exact match)
     * Utilisé pour l'authentification publique des destinataires
     */
    @GetMapping("/search")
    @Operation(summary = "Rechercher un destinataire par nom et email")
    public ResponseEntity<DestinataireDTO> searchDestinataire(
            @RequestParam String nom,
            @RequestParam String email) {
        
        log.debug("Recherche destinataire: nom={}, email={}", nom, email);
        
        return destinataireService.findByNomAndEmail(nom, email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Récupérer tous les colis d'un destinataire
     */
    @GetMapping("/{destinataireId}/colis")
    @Operation(summary = "Récupérer tous les colis d'un destinataire")
    public ResponseEntity<List<ColisDTO>> getColisByDestinataire(
            @PathVariable String destinataireId) {
        
        log.debug("Récupération des colis pour destinataire: {}", destinataireId);
        
        // Vérifier que le destinataire existe
        if (destinataireService.findById(destinataireId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        List<ColisDTO> colis = colisService.findByDestinataireId(destinataireId);
        return ResponseEntity.ok(colis);
    }

    /**
     * Confirmer la réception d'un colis
     * Vérifie que le colis appartient bien au destinataire avant de confirmer
     */
    @PostMapping("/{destinataireId}/colis/{colisId}/confirmation")
    @Operation(summary = "Confirmer la réception d'un colis")
    public ResponseEntity<ColisDTO> confirmReception(
            @PathVariable String destinataireId,
            @PathVariable String colisId) {
        
        log.debug("Confirmation de réception: destinataire={}, colis={}", destinataireId, colisId);
        
        // Vérifier que le colis existe
        ColisDTO colis = colisService.findById(colisId)
                .orElse(null);
        
        if (colis == null) {
            log.warn("Colis non trouvé: {}", colisId);
            return ResponseEntity.notFound().build();
        }
        
        // Vérifier que le colis appartient bien au destinataire
        if (!destinataireId.equals(colis.getDestinataireId())) {
            log.warn("Le colis {} n'appartient pas au destinataire {}", colisId, destinataireId);
            return ResponseEntity.status(403).build(); // Forbidden
        }
        
        // Mettre à jour le statut à LIVRE
        ColisDTO updatedColis = colisService.updateStatus(colisId, "LIVRE", "Livré avec succès");
        return ResponseEntity.ok(updatedColis);
    }
}
