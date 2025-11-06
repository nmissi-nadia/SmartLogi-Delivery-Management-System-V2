package com.smart.controller;

import com.smart.dto.ColisDTO;
import com.smart.dto.LivreurDTO;
import com.smart.service.ColisService;
import com.smart.service.LivreurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/api/livreurs")
@RequiredArgsConstructor
public class LivreurController {
    private final LivreurService service;
    private final ColisService colisService;

    @GetMapping
    public List<LivreurDTO> getAll() {
        return service.findAll();
    }


    @GetMapping("/search")
    public Page<LivreurDTO> searchByKeyword(@RequestParam String keyword, Pageable pageable) {
        return service.searchByKeyword(keyword, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivreurDTO> getById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public LivreurDTO create(@RequestBody LivreurDTO dto) {
        return service.save(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivreurDTO> update(@PathVariable String id, @RequestBody LivreurDTO dto) {
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
    // Colis
    @PutMapping("/{livreurId}/colis/{colisId}/statut")
    public ResponseEntity<ColisDTO> updateColisStatus(
            @PathVariable String livreurId,
            @PathVariable String colisId,
            @RequestParam String nouveauStatut) {
        return ResponseEntity.ok(colisService.updateStatus(colisId, nouveauStatut));
    }

    // Voir les colis assignés
    @GetMapping("/{livreurId}/colis")
    public ResponseEntity<Page<ColisDTO>> getColisAssignes(
            @PathVariable String livreurId,
            @RequestParam(required = false) String statut,
            Pageable pageable) {
        return ResponseEntity.ok(colisService.findByLivreurIdAndStatut(livreurId, statut, pageable));
    }
}