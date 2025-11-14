package com.smart.service;

import com.smart.entity.ColisProduit;
import com.smart.repository.ColisProduitRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ColisProduitService {
    private final ColisProduitRepository colisProduitRepository;

    public ColisProduitService(ColisProduitRepository colisProduitRepository) {
        this.colisProduitRepository = colisProduitRepository;
    }

    public List<ColisProduit> getAllColisProduits() { return colisProduitRepository.findAll(); }
    public ColisProduit createColisProduit(ColisProduit cp) {
        if (cp == null) {
            throw new IllegalArgumentException("L'objet ColisProduit ne peut pas être null");
        }
        return colisProduitRepository.save(cp); }
}
