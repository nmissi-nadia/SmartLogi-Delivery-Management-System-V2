package com.smart.controller;

import com.smart.dto.ClientExpediteurDTO;
import com.smart.dto.ColisDTO;
import com.smart.dto.ColisRequestDTO;
import com.smart.service.ClientExpediteurService;
import com.smart.service.ColisService;
import com.smart.repository.ClientExpediteurRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Tag(name = "Client Expediteur", description = "API for Client Expediteur management")
@SecurityRequirement(name = "bearerAuth")
public class ClientExpediteurController {
    private final ClientExpediteurService service;
    private final ColisService colisService;
    private final ClientExpediteurRepository clientExpediteurRepository;

    @GetMapping
    @PreAuthorize("hasAuthority('MANAGE_CLIENTS')")
    @Operation(summary = "Get all client expediteurs")
    public List<ClientExpediteurDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_CLIENTS') or #id == @clientExpediteurService.findByUsername(authentication.principal.username).get().id")
    @Operation(summary = "Get a client expediteur by ID")
    public ResponseEntity<ClientExpediteurDTO> getById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MANAGE_CLIENTS')")
    @Operation(summary = "Create a new client expediteur")
    public ClientExpediteurDTO create(@RequestBody ClientExpediteurDTO dto) {
        return service.save(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_CLIENTS') or #id == @clientExpediteurService.findByUsername(authentication.principal.username).get().id")
    @Operation(summary = "Update an existing client expediteur")
    public ResponseEntity<ClientExpediteurDTO> update(@PathVariable String id, @RequestBody ClientExpediteurDTO dto) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        dto.setId(id);
        return ResponseEntity.ok(service.save(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGE_CLIENTS')")
    @Operation(summary = "Delete a client expediteur")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
     @GetMapping("/search")
     @PreAuthorize("hasAuthority('MANAGE_CLIENTS')")
     @Operation(summary = "Search for client expediteurs by keyword")
    public Page<ClientExpediteurDTO> searchByKeyword(@RequestParam String keyword, Pageable pageable) {
        return service.searchByKeyword(keyword, pageable);
    }
    //partie pour gestion des colis
    // Créer un nouveau colis
    @PostMapping("/colis")
    @PreAuthorize("hasAuthority('CREATE_COLIS_CLIENT')")
    @Operation(summary = "Create a new colis for a client")
    public ResponseEntity<ColisDTO> createColis(
            @Valid @RequestBody ColisRequestDTO colisRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String clientId = service.findByUsername(username).get().getId();
        // Vérifier que le client expéditeur existe
        if (!clientExpediteurRepository.existsById(clientId)) {
            throw new EntityNotFoundException("Client expéditeur non trouvé avec l'ID: " + clientId);
        }
        
        // Déléguer la logique de création au service
        ColisDTO createdColis = colisService.createColisWithDetails(clientId, colisRequest);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(createdColis);
    }

    // Lister les colis d'un client
   @GetMapping("/colis")
   @PreAuthorize("hasAuthority('VIEW_OWN_COLIS')")
   @Operation(summary = "Get all colis for a client")
    public ResponseEntity<Page<ColisDTO>> getColisByClient(
            @RequestParam(required = false) String status,
            Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String clientId = service.findByUsername(username).get().getId();
        if (status != null) {
            return ResponseEntity.ok(colisService.findColisByClientExpediteurAndStatut(clientId, status, pageable));
        }
        return ResponseEntity.ok(colisService.findColisByClientExpediteur(clientId, pageable));
    }
    @GetMapping("/track/{colisId}")
    @PreAuthorize("hasAuthority('VIEW_OWN_COLIS')")
    @Operation(summary = "Track a colis for a client")
    public ResponseEntity<ColisDTO> trackColis(
            @PathVariable String colisId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String clientId = service.findByUsername(username).get().getId();
        // Vérifier que le colis appartient bien au client
        ColisDTO colis = colisService.trackColis(clientId, colisId);
        return ResponseEntity.ok(colis);
    }
}