package com.smart.controller;

import com.smart.dto.ColisDTO;
import com.smart.service.ColisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/colis")
@RequiredArgsConstructor
public class ColisController {
    private final ColisService colisService;

    @GetMapping
    public List<ColisDTO> getAll() {
        return colisService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColisDTO> getById(@PathVariable String id) {
        return colisService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ColisDTO create(@RequestBody ColisDTO dto) {
        return colisService.save(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ColisDTO> update(@PathVariable String id, @RequestBody ColisDTO dto) {
        if (!colisService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        dto.setId(id);
        return ResponseEntity.ok(colisService.save(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!colisService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        colisService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}