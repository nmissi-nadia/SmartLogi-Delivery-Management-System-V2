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

@Service
@RequiredArgsConstructor
public class ColisService {
    private final ColisRepository colisRepository;
    private final ColisMapper colisMapper;

    public List<ColisDTO> findAll() {
        return colisRepository.findAll().stream()
                .map(colisMapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<ColisDTO> findById(String id) {
        return colisRepository.findById(id)
                .map(colisMapper::toDto);
    }

    public ColisDTO save(ColisDTO dto) {
        Colis entity = colisMapper.toEntity(dto);
        // La logique de l'ID est gérée par @PrePersist dans BaseEntity
        Colis savedEntity = colisRepository.save(entity);
        return colisMapper.toDto(savedEntity);
    }

    public void deleteById(String id) {
        colisRepository.deleteById(id);
    }
}