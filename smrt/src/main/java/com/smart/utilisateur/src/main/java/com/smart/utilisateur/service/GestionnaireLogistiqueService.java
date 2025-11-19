package com.smart.utilisateur.src.main.java.com.smart.utilisateur.service;

import com.smart.entity.GestionnaireLogistique;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.repository.GestionnaireLogistiqueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GestionnaireLogistiqueService {
    private final GestionnaireLogistiqueRepository repository;

    public List<GestionnaireLogistique> findAll() { return repository.findAll(); }
    public Optional<GestionnaireLogistique> findById(String id) { return repository.findById(id); }
    public GestionnaireLogistique save(GestionnaireLogistique gestionnaire) { return repository.save(gestionnaire); }
    public void deleteById(String id) { repository.deleteById(id); }
}
