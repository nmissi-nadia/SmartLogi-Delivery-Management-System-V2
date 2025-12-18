package com.smart.service;

import com.smart.dto.LivreurDTO;
import com.smart.entity.Livreur;
import com.smart.entity.Zone;
import com.smart.mapper.LivreurMapper;
import com.smart.repository.LivreurRepository;
import com.smart.repository.ZoneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LivreurServiceTest {

    @Mock
    private LivreurRepository repository;

    @Mock
    private LivreurMapper mapper;

    @Mock
    private ZoneRepository zoneRepository;

    @InjectMocks
    private LivreurService livreurService;

    private Livreur livreur;
    private LivreurDTO livreurDTO;
    private Zone zone;
    private final String LIVREUR_ID = "123e4567-e89b-12d3-a456-426614174000";
    private final String ZONE_ID = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        zone = new Zone();
        zone.setId(ZONE_ID);

        livreur = new Livreur();
        livreur.setId(LIVREUR_ID);
        livreur.setNom("nmissi");
        livreur.setPrenom("nadia");
        livreur.setTelephone("0612345678");
        livreur.setVehicule("vehicule");
        livreur.setZoneAssignee(zone);

        livreurDTO = new LivreurDTO();
        livreurDTO.setId(LIVREUR_ID);
        livreurDTO.setNom("nmissi");
        livreurDTO.setPrenom("nadia");
        livreurDTO.setTelephone("0612345678");
        livreurDTO.setVehicule("vehicule");
        livreurDTO.setZoneAssigneeId(ZONE_ID);
        
    }

    @Test
    void findAll_ShouldReturnAllLivreurs() {
        // Arrange
        when(repository.findAll()).thenReturn(Collections.singletonList(livreur));
        when(mapper.toDto(any(Livreur.class))).thenReturn(livreurDTO);

        // Act
        List<LivreurDTO> result = livreurService.findAll();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(LIVREUR_ID);
        verify(repository).findAll();
        verify(mapper).toDto(livreur);
    }

    @Test
    void findById_WhenLivreurExists_ShouldReturnLivreur() {
        // Arrange
        when(repository.findById(LIVREUR_ID)).thenReturn(Optional.of(livreur));
        when(mapper.toDto(any(Livreur.class))).thenReturn(livreurDTO);

        // Act
        Optional<LivreurDTO> result = livreurService.findById(LIVREUR_ID);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(LIVREUR_ID);
        verify(repository).findById(LIVREUR_ID);
    }

    @Test
    void findById_WhenLivreurNotExists_ShouldReturnEmpty() {
        // Arrange
        when(repository.findById("unknown_id")).thenReturn(Optional.empty());

        // Act
        Optional<LivreurDTO> result = livreurService.findById("unknown_id");

        // Assert
        assertThat(result).isEmpty();
        verify(repository).findById("unknown_id");
    }

    @Test
    void save_ShouldSaveAndReturnLivreur() {
        // Arrange
        when(zoneRepository.findById(ZONE_ID)).thenReturn(Optional.of(zone));
        when(mapper.toEntity(livreurDTO)).thenReturn(livreur);
        when(repository.save(livreur)).thenReturn(livreur);
        when(mapper.toDto(livreur)).thenReturn(livreurDTO);

        // Act
        LivreurDTO result = livreurService.save(livreurDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(LIVREUR_ID);
        verify(mapper).toEntity(livreurDTO);
        verify(repository).save(livreur);
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        // Act
        livreurService.deleteById(LIVREUR_ID);

        // Assert
        verify(repository).deleteById(LIVREUR_ID);
    }

    @Test
    void searchByKeyword_ShouldReturnMatchingLivreurs() {
        // Arrange
        String keyword = "nmissi";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Livreur> page = new PageImpl<>(Collections.singletonList(livreur));

        when(repository.searchByKeyword(keyword, pageable)).thenReturn(page);
        when(mapper.toDto(any(Livreur.class))).thenReturn(livreurDTO);

        // Act
        Page<LivreurDTO> result = livreurService.searchByKeyword(keyword, pageable);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0).getNom()).isEqualTo("nmissi");
        verify(repository).searchByKeyword(keyword, pageable);
    }
}