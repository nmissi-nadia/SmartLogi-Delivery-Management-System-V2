package com.smartlogi.service;

import com.smartlogi.entity.HistoriqueLivraison;
import com.smartlogi.repository.HistoriqueLivraisonRepository;
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
