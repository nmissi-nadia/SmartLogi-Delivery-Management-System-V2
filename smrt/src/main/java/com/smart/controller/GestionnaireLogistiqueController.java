package com.smart.controller;

import com.smart.entity.GestionnaireLogistique;
import com.smart.service.GestionnaireLogistiqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/gestionnaires")
@RequiredArgsConstructor
public class GestionnaireLogistiqueController {
    private final GestionnaireLogistiqueService service;

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
}