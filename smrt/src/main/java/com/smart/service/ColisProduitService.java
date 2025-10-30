package com.smartlogi.service;

import com.smartlogi.entity.ColisProduit;
import com.smartlogi.repository.ColisProduitRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ColisProduitService {
    private final ColisProduitRepository colisProduitRepository;

    public ColisProduitService(ColisProduitRepository colisProduitRepository) {
        this.colisProduitRepository = colisProduitRepository;
    }

    public List<ColisProduit> getAllColisProduits() { return colisProduitRepository.findAll(); }
    public ColisProduit createColisProduit(ColisProduit cp) { return colisProduitRepository.save(cp); }
}
