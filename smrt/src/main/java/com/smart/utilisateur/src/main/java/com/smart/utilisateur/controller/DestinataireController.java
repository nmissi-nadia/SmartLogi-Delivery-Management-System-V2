package com.smart.utilisateur.src.main.java.com.smart.utilisateur.controller;

import com.smart.dto.ColisDTO;
import com.smart.dto.DestinataireDTO;
import com.smart.service.ColisService;
import com.smart.service.DestinataireService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/destinataires")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MANAGER')")
public class DestinataireController {
    private final DestinataireService service;
    private final ColisService colisService;


    @GetMapping
    public List<DestinataireDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DestinataireDTO> getById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public DestinataireDTO create(@RequestBody DestinataireDTO dto) {
        return service.save(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DestinataireDTO> update(@PathVariable String id, @RequestBody DestinataireDTO dto) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        dto.setId(id);
        return ResponseEntity.ok(service.save(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    // colis
    // Voir les détails d'un colis
    @GetMapping("/{destinataireId}/colis/{colisId}")
    public ResponseEntity<ColisDTO> viewColis(
            @PathVariable String destinataireId,
            @PathVariable String colisId) {
        return ResponseEntity.ok(colisService.findById(colisId)
                .orElseThrow(() -> new EntityNotFoundException("Colis non trouvé")));
    }

    // Confirmer réception
    @PostMapping("/{destinataireId}/colis/{colisId}/confirmation")
    public ResponseEntity<ColisDTO> confirmReception(
            @PathVariable String destinataireId,
            @PathVariable String colisId) {
        return ResponseEntity.ok(colisService.updateStatus(colisId, "LIVRE", "Livré avec succès"));
    }
}
