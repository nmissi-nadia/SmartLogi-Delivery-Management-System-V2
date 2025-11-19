package com.smart.livraison.src.main.java.com.smart.livraison.service;

import com.smart.dto.LivreurDTO;
import com.smart.entity.Livreur;
import com.smart.entity.Zone;
import com.smart.mapper.LivreurMapper;
import com.smart.livraison.src.main.java.com.smart.livraison.repository.LivreurRepository;
import com.smart.repository.ZoneRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class LivreurService {
    private final LivreurRepository repository;
    private final LivreurMapper mapper;
    private final ZoneRepository zoneRepository;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public List<LivreurDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<LivreurDTO> findById(String id) {
        return repository.findById(id).map(mapper::toDto);
    }

    public LivreurDTO save(LivreurDTO dto) {
    // Log du DTO reçu
    log.info("Tentative de sauvegarde d'un livreur avec le DTO: {}", dto);
    
    if (dto == null) {
        log.error("Le DTO fourni est null");
        throw new IllegalArgumentException("Le DTO ne peut pas être null");
    }
    
    if (dto.getZoneAssigneeId() == null) {
        log.error("L'ID de la zone est null dans le DTO: {}", dto);
        throw new IllegalArgumentException("L'ID de la zone est requis");
    }
    
    try {
        log.debug("Recherche de la zone avec l'ID: {}", dto.getZoneAssigneeId());
        Zone zone = zoneRepository.findById(dto.getZoneAssigneeId())
            .orElseThrow(() -> {
                String errorMsg = "Zone non trouvée avec l'ID: " + dto.getZoneAssigneeId();
                log.error(errorMsg);
                return new EntityNotFoundException(errorMsg);
            });
        
        log.debug("Mapping du DTO vers l'entité Livreur");
        Livreur entity = mapper.toEntity(dto);
        
        log.debug("Définition de la zone sur le livreur");
        entity.setZoneAssignee(zone);
        
        log.debug("Sauvegarde du livreur...");
        Livreur savedEntity = repository.save(entity);
        log.info("Livreur sauvegardé avec succès, ID: {}", savedEntity.getId());
        
        return mapper.toDto(savedEntity);
        
    } catch (Exception e) {
        log.error("Erreur lors de la sauvegarde du livreur", e);
        throw e; 
    }
}

    public void deleteById(String id) {
        repository.deleteById(id);
    }
    public Page<LivreurDTO> searchByKeyword(String keyword, Pageable pageable) {
        return repository.searchByKeyword(keyword, pageable).map(mapper::toDto);
    }

    public boolean isOwner(String livreurId, String username) {
        return repository.findById(livreurId)
                .map(livreur -> livreur.getUser().getUsername().equals(username))
                .orElse(false);
    }
}