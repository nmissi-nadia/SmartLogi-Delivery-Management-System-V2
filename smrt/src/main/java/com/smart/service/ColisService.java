package com.smart.service;

import com.smart.entity.Colis;
import com.smart.repository.ColisRepository;
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
