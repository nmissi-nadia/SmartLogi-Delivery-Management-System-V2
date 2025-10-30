package com.smart.controller;

import com.smart.entity.HistoriqueLivraison;
import com.smart.service.HistoriqueLivraisonService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/historique-livraisons")
public class HistoriqueLivraisonController {
    private final HistoriqueLivraisonService historiqueLivraisonService;

    public HistoriqueLivraisonController(HistoriqueLivraisonService historiqueLivraisonService) {
        this.historiqueLivraisonService = historiqueLivraisonService;
    }

    @GetMapping
    public List<HistoriqueLivraison> getAllHistoriqueLivraisons() {
        return historiqueLivraisonService.getAllHistoriqueLivraisons();
    }

    @PostMapping
    public HistoriqueLivraison addHistorique(@RequestBody HistoriqueLivraison historiqueLivraison) {
        return historiqueLivraisonService.addHistoriqueLivraison(historiqueLivraison);
    }
}
