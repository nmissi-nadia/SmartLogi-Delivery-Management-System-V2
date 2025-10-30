package com.smart.service;

import com.smart.entity.HistoriqueLivraison;
import com.smart.repository.HistoriqueLivraisonRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HistoriqueLivraisonService {
    private final HistoriqueLivraisonRepository historiqueRepository;

    public HistoriqueLivraisonService(HistoriqueLivraisonRepository historiqueRepository) {
        this.historiqueRepository = historiqueRepository;
    }

    public List<HistoriqueLivraison> getAllHistoriqueLivraisons() { return historiqueRepository.findAll(); }
    public HistoriqueLivraison createHistorique(HistoriqueLivraison historique) { return historiqueRepository.save(historique); }
}
