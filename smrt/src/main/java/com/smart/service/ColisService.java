package com.smartlogi.service;

import com.smartlogi.entity.Colis;
import com.smartlogi.repository.ColisRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ColisService {
    private final ColisRepository colisRepository;

    public ColisService(ColisRepository colisRepository) {
        this.colisRepository = colisRepository;
    }

    public List<Colis> getAllColis() { return colisRepository.findAll(); }
    public Colis createColis(Colis colis) { return colisRepository.save(colis); }
}
