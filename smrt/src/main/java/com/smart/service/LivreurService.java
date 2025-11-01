package com.smart.service;

import com.smart.dto.LivreurDTO;
import com.smart.entity.Livreur;
import com.smart.mapper.LivreurMapper;
import com.smart.repository.LivreurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LivreurService {
    private final LivreurRepository repository;
    private final LivreurMapper mapper;

    public List<LivreurDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<LivreurDTO> findById(String id) {
        return repository.findById(id).map(mapper::toDto);
    }

    public LivreurDTO save(LivreurDTO dto) {
        Livreur entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }
}