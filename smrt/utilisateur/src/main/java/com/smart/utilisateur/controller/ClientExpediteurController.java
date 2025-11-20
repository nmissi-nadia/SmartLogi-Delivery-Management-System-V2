package com.smart.utilisateur.controller;


import com.smart.colis.src.main.java.com.smart.colis.dto.ColisDTO;
import com.smart.colis.src.main.java.com.smart.colis.dto.ColisRequestDTO;
import com.smart.colis.src.main.java.com.smart.colis.service.ColisService;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.dto.ClientExpediteurDTO;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.repository.ClientExpediteurRepository;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.service.ClientExpediteurService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientExpediteurController {
    private final ClientExpediteurService service;
    private final ColisService colisService;
    private final ClientExpediteurRepository clientExpediteurRepository;

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    public List<ClientExpediteurDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or (hasRole('CLIENT') and @clientExpediteurService.isOwner(#id, principal.username))")
    public ResponseEntity<ClientExpediteurDTO> getById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or (hasRole('CLIENT') and @clientExpediteurService.isOwner(#id, principal.username))")
    public ResponseEntity<ClientExpediteurDTO> update(@PathVariable String id, @RequestBody ClientExpediteurDTO dto) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        dto.setId(id);
        return ResponseEntity.ok(service.save(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
     @GetMapping("/search")
     @PreAuthorize("isAuthenticated()")
    public Page<ClientExpediteurDTO> searchByKeyword(@RequestParam String keyword, Pageable pageable) {
        return service.searchByKeyword(keyword, pageable);
    }
    //partie pour gestion des colis
    // Créer un nouveau colis
    @PostMapping("/{clientId}/colis")
    @PreAuthorize("hasRole('CLIENT') and @clientExpediteurService.isOwner(#clientId, principal.username)")
    public ResponseEntity<ColisDTO> createColis(
            @PathVariable String clientId,
            @Valid @RequestBody ColisRequestDTO colisRequest) {
        
        // Vérifier que le client expéditeur existe
        if (!clientExpediteurRepository.existsById(clientId)) {
            throw new EntityNotFoundException("Client expéditeur non trouvé avec l'ID: " + clientId);
        }
        
        // Déléguer la logique de création au service
        ColisDTO createdColis = colisService.createColisWithDetails(clientId, colisRequest);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdColis);
    }

    // Lister les colis d'un client
   @GetMapping("/{clientId}/colis")
   @PreAuthorize("hasRole('MANAGER') or (hasRole('CLIENT') and @clientExpediteurService.isOwner(#clientId, principal.username))")
    public ResponseEntity<Page<ColisDTO>> getColisByClientAndStatus(
            @PathVariable String clientId,
            @RequestParam(required = false) String status,
            Pageable pageable) {
        if (status != null) {
            return ResponseEntity.ok(colisService.findColisByClientExpediteurAndStatut(clientId, status, pageable));
        }
        return ResponseEntity.ok(colisService.findColisByClientExpediteur(clientId, pageable));
    }

    // Suivre un colis
    @GetMapping("/{clientId}/colis/{colisId}")
    @PreAuthorize("hasRole('MANAGER') or (hasRole('CLIENT') and @clientExpediteurService.isOwner(#clientId, principal.username))")
    public ResponseEntity<ColisDTO> trackColis(
            @PathVariable String clientId,
            @PathVariable String colisId) {
        return ResponseEntity.ok(colisService.findById(colisId)
                .orElseThrow(() -> new EntityNotFoundException("Colis non trouvé")));
    }
   

}