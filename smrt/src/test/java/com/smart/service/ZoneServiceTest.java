package com.smart.service;


import com.smart.common.src.main.java.com.smart.entity.Zone;
import com.smart.zone.src.main.java.com.smart.zone.dto.ZoneDTO;
import com.smart.zone.src.main.java.com.smart.zone.mapper.ZoneMapper;
import com.smart.zone.src.main.java.com.smart.zone.repository.ZoneRepository;
import com.smart.zone.src.main.java.com.smart.zone.service.ZoneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ZoneServiceTest {

    @Mock
    private ZoneRepository repository;

    @Mock
    private ZoneMapper mapper;

    @InjectMocks
    private ZoneService service;

    private Zone zone;
    private ZoneDTO zoneDTO;
    private final String ZONE_ID = "123e4567-e89b-12d3-a456-426614174000";

    @BeforeEach
    void setUp() {
        zone = new Zone();
        zone.setId(ZONE_ID);
        zone.setNom("Zone Nord");
        zone.setCodePostal("12345");
        

        zoneDTO = new ZoneDTO();
        zoneDTO.setId(ZONE_ID);
        zoneDTO.setNom("Zone Nord");
        zoneDTO.setCodePostal("12345");
    }

    @Test
    void findAll_ShouldReturnAllZones() {
        when(repository.findAll()).thenReturn(List.of(zone));
        when(mapper.toDto(any(Zone.class))).thenReturn(zoneDTO);

        List<ZoneDTO> result = service.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(ZONE_ID);
        verify(repository).findAll();
    }

    @Test
    void findById_WhenZoneExists_ShouldReturnZone() {
        when(repository.findById(ZONE_ID)).thenReturn(Optional.of(zone));
        when(mapper.toDto(any(Zone.class))).thenReturn(zoneDTO);

        Optional<ZoneDTO> result = service.findById(ZONE_ID);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(ZONE_ID);
        verify(repository).findById(ZONE_ID);
    }

    @Test
    void findById_WhenZoneNotExists_ShouldReturnEmpty() {
        when(repository.findById("unknown_id")).thenReturn(Optional.empty());

        Optional<ZoneDTO> result = service.findById("unknown_id");

        assertThat(result).isEmpty();
        verify(repository).findById("unknown_id");
    }

    @Test
    void save_ShouldSaveAndReturnZone() {
        when(mapper.toEntity(zoneDTO)).thenReturn(zone);
        when(repository.save(zone)).thenReturn(zone);
        when(mapper.toDto(zone)).thenReturn(zoneDTO);

        ZoneDTO result = service.save(zoneDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(ZONE_ID);
        verify(repository).save(zone);
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        service.deleteById(ZONE_ID);
        verify(repository).deleteById(ZONE_ID);
    }
}