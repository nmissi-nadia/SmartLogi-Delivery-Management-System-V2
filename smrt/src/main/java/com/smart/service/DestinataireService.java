package com.smartlogi.service;

import com.smartlogi.entity.Destinataire;
import com.smartlogi.repository.DestinataireRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DestinataireService {
    private final DestinataireRepository destinataireRepository;

    public DestinataireService(DestinataireRepository destinataireRepository) {
        this.destinataireRepository = destinataireRepository;
    }

    public List<Destinataire> getAllDestinataires() { return destinataireRepository.findAll(); }
    public Destinataire createDestinataire(Destinataire destinataire) { return destinataireRepository.save(destinataire); }
}
