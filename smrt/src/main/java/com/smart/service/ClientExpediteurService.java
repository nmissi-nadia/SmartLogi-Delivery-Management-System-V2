package com.smart.service;

import com.smart.dto.ClientExpediteurDTO;
import com.smart.entity.ClientExpediteur;
import com.smart.mapper.ClientExpediteurMapper;
import com.smart.repository.ClientExpediteurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class ClientExpediteurService {
    private final ClientExpediteurRepository repository;
    private final ClientExpediteurMapper mapper;

    public List<ClientExpediteurDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<ClientExpediteurDTO> findById(String id) {
        return repository.findById(id).map(mapper::toDto);
    }

    public ClientExpediteurDTO save(ClientExpediteurDTO dto) {
        ClientExpediteur entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }
    public Page<ClientExpediteurDTO> searchByKeyword(String keyword, Pageable pageable) {
        return repository.searchByKeyword(keyword, pageable).map(mapper::toDto);
    }

    public Optional<ClientExpediteur> findByUsername(String username) {
        return repository.findByUsername(username);
    }
}