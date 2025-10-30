package com.smartlogi.service;

import com.smartlogi.entity.GestionnaireLogistique;
import com.smartlogi.repository.GestionnaireLogistiqueRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GestionnaireLogistiqueService {
    private final GestionnaireLogistiqueRepository gestionnaireRepository;

    public GestionnaireLogistiqueService(GestionnaireLogistiqueRepository gestionnaireRepository) {
        this.gestionnaireRepository = gestionnaireRepository;
    }

    public List<GestionnaireLogistique> getAllGestionnaires() { return gestionnaireRepository.findAll(); }
    public GestionnaireLogistique createGestionnaire(GestionnaireLogistique gestionnaire) { return gestionnaireRepository.save(gestionnaire); }
}
