package com.smart.colis.controller;


import com.smart.colis.src.main.java.com.smart.colis.dto.ColisDTO;
import com.smart.colis.src.main.java.com.smart.colis.service.ColisService;
import com.smart.livraison.src.main.java.com.smart.livraison.dto.HistoriqueLivraisonDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
public class ColisController {
    private final ColisService colisService;
    private static final Logger log = LoggerFactory.getLogger(ColisController.class);

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'LIVREUR', 'CLIENT')")
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
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or (hasRole('LIVREUR') and @colisService.isLivreurOfColis(#id, principal.username)) or (hasRole('CLIENT') and @colisService.isClientOfColis(#id, principal.username))")
    public ResponseEntity<ColisDTO> updateColis(
            @PathVariable String id,
            @RequestBody ColisDTO colisDTO) {
        return ResponseEntity.ok(colisService.update(id, colisDTO));
    }

    @GetMapping("/recherche")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<ColisDTO>> searchColis(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String statut,
            @RequestParam(required = false) String ville,
            @RequestParam(required = false) String priorite,
            Pageable pageable) {
        return ResponseEntity.ok(colisService.searchByKeyword(keyword, pageable));
    }

    // Obtenir l'historique d'un colis
    @GetMapping("/{colisId}/historique")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<HistoriqueLivraisonDTO>> getHistoriqueColis(
            @PathVariable String colisId) {
        List<HistoriqueLivraisonDTO> historique = colisService.getHistoriqueForColis(colisId);
        return ResponseEntity.ok(historique);
    }
}