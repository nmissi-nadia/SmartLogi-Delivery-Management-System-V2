package com.smart.service;

import com.smart.dto.ColisDTO;
import com.smart.entity.Colis;
import com.smart.mapper.ColisMapper;
import com.smart.repository.ColisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class ColisService {
    private final ColisRepository colisRepository;
    private final ColisMapper colisMapper;
    private static final Logger log = LoggerFactory.getLogger(ColisService.class);

    public List<ColisDTO> findAll() {
        log.debug("Récupération de tous les colis");
        return colisRepository.findAll().stream()
                .map(colisMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<ColisDTO> findById(String id) {
        log.debug("Récupération du colis avec l'ID: {}", id);
        return colisRepository.findById(id)
                .map(colisMapper::toDto);
    }

    public ColisDTO save(ColisDTO dto) {
        log.info("Création/mise à jour d'un colis: {}", dto);
        Colis entity = colisMapper.toEntity(dto);
        // La logique de l'ID est gérée par @PrePersist dans BaseEntity
        Colis savedEntity = colisRepository.save(entity);
        log.info("Colis enregistré avec succès, ID: {}", savedEntity.getId());
        return colisMapper.toDto(savedEntity);
    }

    public void deleteById(String id) {
        log.info("Suppression du colis avec l'ID: {}", id);
        colisRepository.deleteById(id);
        log.info("Colis supprimé avec succès, ID: {}", id);
    }
}