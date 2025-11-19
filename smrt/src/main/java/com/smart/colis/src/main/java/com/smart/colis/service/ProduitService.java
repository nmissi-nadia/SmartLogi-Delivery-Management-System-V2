package com.smart.colis.src.main.java.com.smart.colis.service;

import com.smart.dto.ProduitDTO;
import com.smart.entity.Produit;
import com.smart.mapper.ProduitMapper;
import com.smart.colis.src.main.java.com.smart.colis.repository.ProduitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProduitService {
    private final ProduitRepository repository;
    private final ProduitMapper mapper;

    public List<ProduitDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<ProduitDTO> findById(String id) {
        return repository.findById(id).map(mapper::toDto);
    }

    public ProduitDTO save(ProduitDTO dto) {
        Produit entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }
}