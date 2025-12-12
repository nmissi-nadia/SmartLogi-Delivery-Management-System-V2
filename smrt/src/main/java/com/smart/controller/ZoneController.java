package com.smart.controller;

import com.smart.dto.ZoneDTO;
import com.smart.service.ZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/zones")
@RequiredArgsConstructor
@PreAuthorize("hasRole('GESTIONNAIRE_LOGISTIQUE')")
@Tag(name = "Zone", description = "API for Zone management")
@SecurityRequirement(name = "bearerAuth")
public class ZoneController {
    private final ZoneService service;

    @GetMapping
    @Operation(summary = "Get all zones")
    public List<ZoneDTO> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a zone by ID")
    public ResponseEntity<ZoneDTO> getById(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new zone")
    public ZoneDTO create(@RequestBody ZoneDTO dto) {
        return service.save(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing zone")
    public ResponseEntity<ZoneDTO> update(@PathVariable String id, @RequestBody ZoneDTO dto) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        dto.setId(id);
        return ResponseEntity.ok(service.save(dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a zone")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
