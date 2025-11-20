package com.smart.utilisateur.service;


import com.smart.utilisateur.src.main.java.com.smart.utilisateur.dto.UserDTO;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.mapper.UserMapper;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.repository.GestionnaireLogistiqueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GestionnaireLogistiqueService {
    private final GestionnaireLogistiqueRepository repository;
    private final UserMapper userMapper;

    public List<UserDTO> findAll() {
        return repository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }
    public Optional<UserDTO> findById(String id) {
        return repository.findById(id).map(userMapper::toDto);
    }
}
