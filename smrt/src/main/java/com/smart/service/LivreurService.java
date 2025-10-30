package com.smartlogi.service;

import com.smartlogi.entity.Livreur;
import com.smartlogi.repository.LivreurRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LivreurService {
    private final LivreurRepository livreurRepository;

    public LivreurService(LivreurRepository livreurRepository) {
        this.livreurRepository = livreurRepository;
    }

    public List<Livreur> getAllLivreurs() { return livreurRepository.findAll(); }
    public Livreur createLivreur(Livreur livreur) { return livreurRepository.save(livreur); }
}
