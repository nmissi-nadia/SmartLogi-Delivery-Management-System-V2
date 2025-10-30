package com.smart.controller;

import com.smart.entity.Colis;
import com.smart.service.ColisService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/colis")
public class ColisController {
    private final ColisService colisService;

    public ColisController(ColisService colisService) {
        this.colisService = colisService;
    }

    @GetMapping
    public List<Colis> getAllColis() {
        return colisService.getAllColis();
    }

    @GetMapping("/{id}")
    public Colis getColisById(@PathVariable Long id) {
        return colisService.getColisById(id);
    }

    @PostMapping
    public Colis addColis(@RequestBody Colis colis) {
        return colisService.addColis(colis);
    }

    @PutMapping("/{id}")
    public Colis updateColis(@PathVariable Long id, @RequestBody Colis colis) {
        return colisService.updateColis(id, colis);
    }

    @DeleteMapping("/{id}")
    public void deleteColis(@PathVariable Long id) {
        colisService.deleteColis(id);
    }
}
