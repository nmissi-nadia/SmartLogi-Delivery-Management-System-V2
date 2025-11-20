package com.smart.utilisateur.controller;

import com.smart.colis.src.main.java.com.smart.colis.dto.ColisDTO;
import com.smart.colis.src.main.java.com.smart.colis.service.ColisService;
import com.smart.common.src.main.java.com.smart.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/livreurs")
@RequiredArgsConstructor
public class LivreurController {

    private final ColisService colisService;

    /**
     * Endpoint pour que le livreur authentifié récupère la liste de ses colis assignés.
     */
    @GetMapping("/me/colis")
    @PreAuthorize("hasRole('LIVREUR')")
    public ResponseEntity<Page<ColisDTO>> getMesColis(@AuthenticationPrincipal User user, Pageable pageable) {
        Page<ColisDTO> colis = colisService.findColisByLivreur(user.getId(), pageable);
        return ResponseEntity.ok(colis);
    }

    /**
     * Endpoint pour que le livreur mette à jour le statut d'un de ses colis.
     */
    @PutMapping("/colis/{colisId}/statut")
    @PreAuthorize("hasRole('LIVREUR')")
    public ResponseEntity<ColisDTO> updateStatutColis(@PathVariable String colisId, @RequestBody Map<String, String> request, @AuthenticationPrincipal User user) {
        // On pourrait ajouter une vérification pour s'assurer que le colis appartient bien au livreur.
        ColisDTO updatedColis = colisService.updateStatus(colisId, request.get("statut"), request.get("commentaire"));
        return ResponseEntity.ok(updatedColis);
    }
}