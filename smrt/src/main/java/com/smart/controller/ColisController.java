package com.smart.controller;

import com.smart.dto.ColisDTO;
import com.smart.service.ColisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/colis")
@RequiredArgsConstructor
public class ColisController {
    private final ColisService colisService;
    private static final Logger log = LoggerFactory.getLogger(ColisController.class);

    @GetMapping
    public List<ColisDTO> getAll() {
        log.debug("Récupération de tous les colis");
        return colisService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColisDTO> getById(@PathVariable String id) {
        log.info("Requête pour le colis avec l'ID: {}", id);
        return colisService.findById(id)
                .map(colis -> {
                    log.debug("Colis trouvé: {}", colis);
                    return ResponseEntity.ok(colis);
                })
                .orElseGet(() -> {
                    log.warn("Aucun colis trouvé avec l'ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    public ColisDTO create(@RequestBody ColisDTO dto) {
        log.debug("Création d'un colis: {}", dto);
            return colisService.save(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ColisDTO> update(@PathVariable String id, @RequestBody ColisDTO dto) {
        log.debug("Mise à jour du colis avec l'ID: {}", id);
        if (!colisService.findById(id).isPresent()) {
            log.error("Tentative de mise à jour d'un colis inexistant, ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        dto.setId(id);
        return ResponseEntity.ok(colisService.save(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        log.debug("Suppression du colis avec l'ID: {}", id);
        if (!colisService.findById(id).isPresent()) {
            log.error("Tentative de suppression d'un colis inexistant, ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        colisService.deleteById(id);
        log.info("Colis supprimé avec succès, ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}