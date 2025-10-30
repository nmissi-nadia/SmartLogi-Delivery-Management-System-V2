package com.smart.controller;

import com.smart.entity.Livreur;
import com.smart.service.LivreurService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/livreurs")
public class LivreurController {
    private final LivreurService livreurService;

    public LivreurController(LivreurService livreurService) {
        this.livreurService = livreurService;
    }

    @GetMapping
    public List<Livreur> getAllLivreurs() {
        return livreurService.getAllLivreurs();
    }

    @GetMapping("/{id}")
    public Livreur getLivreurById(@PathVariable Long id) {
        return livreurService.getLivreurById(id);
    }

    @PostMapping
    public Livreur addLivreur(@RequestBody Livreur livreur) {
        return livreurService.addLivreur(livreur);
    }

    @PutMapping("/{id}")
    public Livreur updateLivreur(@PathVariable Long id, @RequestBody Livreur livreur) {
        return livreurService.updateLivreur(id, livreur);
    }

    @DeleteMapping("/{id}")
    public void deleteLivreur(@PathVariable Long id) {
        livreurService.deleteLivreur(id);
    }
}
