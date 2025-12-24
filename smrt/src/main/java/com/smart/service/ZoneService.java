package com.smart.service;

import com.smart.dto.ZoneDTO;
import com.smart.entity.Zone;
import com.smart.mapper.ZoneMapper;
import com.smart.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ZoneService {
    private final ZoneRepository repository;
    private final ZoneMapper mapper;

    public List<ZoneDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public Optional<ZoneDTO> findById(String id) {
        return repository.findById(id).map(mapper::toDto);
    }

    public Optional<Zone> findEntityById(String id) {
        return repository.findById(id);
    }

    public ZoneDTO save(ZoneDTO dto) {
        Zone entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }
}