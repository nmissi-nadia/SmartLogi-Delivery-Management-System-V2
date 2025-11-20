package com.smart.livraison.controller;


import com.smart.livraison.src.main.java.com.smart.livraison.dto.HistoriqueLivraisonDTO;
import com.smart.livraison.src.main.java.com.smart.livraison.service.HistoriqueLivraisonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/historiques")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MANAGER')")
public class HistoriqueLivraisonController {
    private final HistoriqueLivraisonService service;

    @GetMapping
    public List<HistoriqueLivraisonDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoriqueLivraisonDTO> getById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public HistoriqueLivraisonDTO create(@RequestBody HistoriqueLivraisonDTO dto) {
        return service.save(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistoriqueLivraisonDTO> update(@PathVariable String id, @RequestBody HistoriqueLivraisonDTO dto) {
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
}