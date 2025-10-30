package com.smart.service;

import com.smart.dto.DestinataireDTO;
import com.smart.entity.Destinataire;
import com.smart.mapper.DestinataireMapper;
import com.smart.repository.DestinataireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DestinataireService {
    private final DestinataireRepository repository;
    private final DestinataireMapper mapper;

    public List<DestinataireDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<DestinataireDTO> findById(String id) {
        return repository.findById(id).map(mapper::toDto);
    }

    public DestinataireDTO save(DestinataireDTO dto) {
        Destinataire entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }
}