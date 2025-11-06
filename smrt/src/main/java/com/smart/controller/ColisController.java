package com.smart.controller;

import com.smart.dto.ColisDTO;
import com.smart.dto.HistoriqueLivraisonDTO;
import com.smart.entity.HistoriqueLivraison;
import com.smart.service.ColisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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

    @GetMapping("/recherche")
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
    public ResponseEntity<List<HistoriqueLivraison>> getHistoriqueColis(
            @PathVariable String colisId) {
        return ResponseEntity.ok(colisService.getHistoriqueForColis(colisId));
    }
}