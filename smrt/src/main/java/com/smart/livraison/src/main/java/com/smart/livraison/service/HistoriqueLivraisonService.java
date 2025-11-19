package com.smart.livraison.src.main.java.com.smart.livraison.service;

import com.smart.dto.HistoriqueLivraisonDTO;
import com.smart.entity.HistoriqueLivraison;
import com.smart.mapper.HistoriqueLivraisonMapper;
import com.smart.livraison.src.main.java.com.smart.livraison.repository.HistoriqueLivraisonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoriqueLivraisonService {
    private final HistoriqueLivraisonRepository repository;
    private final HistoriqueLivraisonMapper mapper;

    public List<HistoriqueLivraisonDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<HistoriqueLivraisonDTO> findById(String id) {
        return repository.findById(id).map(mapper::toDto);
    }

    public HistoriqueLivraisonDTO save(HistoriqueLivraisonDTO dto) {
        HistoriqueLivraison entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }
}