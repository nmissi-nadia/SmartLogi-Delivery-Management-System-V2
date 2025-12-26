package com.smart.controller;

import com.smart.dto.ProduitDTO;
import com.smart.service.ProduitService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
@Tag(name = "Produit", description = "API pour la gestion des produits")
@SecurityRequirement(name = "bearerAuth")
public class ProduitController {
    private final ProduitService service;

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_PRODUITS')")
    @Operation(summary = "Récupérer tous les produits")
    public List<ProduitDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VIEW_PRODUITS')")
    @Operation(summary = "Récupérer un produit par son ID")
    public ResponseEntity<ProduitDTO> getById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MANAGE_PRODUITS')")
    @Operation(summary = "Créer un nouveau produit")
    public ProduitDTO create(@RequestBody ProduitDTO dto) {
        return service.save(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_PRODUITS')")
    @Operation(summary = "Mettre à jour un produit existant")
    public ResponseEntity<ProduitDTO> update(@PathVariable String id, @RequestBody ProduitDTO dto) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        dto.setId(id);
        return ResponseEntity.ok(service.save(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_PRODUITS')")
    @Operation(summary = "Supprimer un produit")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}