package com.smartlogi.service;

import com.smartlogi.entity.Produit;
import com.smartlogi.repository.ProduitRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProduitService {
    private final ProduitRepository produitRepository;

    public ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    public List<Produit> getAllProduits() { return produitRepository.findAll(); }
    public Produit createProduit(Produit produit) { return produitRepository.save(produit); }
}
