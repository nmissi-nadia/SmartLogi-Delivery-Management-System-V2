package com.smart.service;

import com.smart.dto.DestinataireDTO;
import com.smart.entity.Destinataire;
import com.smart.mapper.DestinataireMapper;
import com.smart.repository.DestinataireRepository;
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
class DestinataireServiceTest {

    @Mock
    private DestinataireRepository repository;

    @Mock
    private DestinataireMapper mapper;

    @InjectMocks
    private DestinataireService service;

    private Destinataire destinataire;
    private DestinataireDTO destinataireDTO;
    private final String DESTINATAIRE_ID = "123e4567-e89b-12d3-a456-426614174000";

    @BeforeEach
    void setUp() {
        destinataire = new Destinataire();
        destinataire.setId(DESTINATAIRE_ID);
        destinataire.setNom("Benali");
        destinataire.setPrenom("Ahmed");
        destinataire.setAdresse("123 Rue de Paris");
        destinataire.setTelephone("0612345678");

        destinataireDTO = new DestinataireDTO();
        destinataireDTO.setId(DESTINATAIRE_ID);
        destinataireDTO.setNom("Benali");
        destinataireDTO.setPrenom("Ahmed");
        destinataireDTO.setAdresse("123 Rue de Paris");
        destinataireDTO.setTelephone("0612345678");
    }

    @Test
    void findAll_ShouldReturnAllDestinataires() {
        when(repository.findAll()).thenReturn(List.of(destinataire));
        when(mapper.toDto(any(Destinataire.class))).thenReturn(destinataireDTO);

        List<DestinataireDTO> result = service.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(DESTINATAIRE_ID);
        verify(repository).findAll();
    }

    @Test
    void findById_WhenDestinataireExists_ShouldReturnDestinataire() {
        when(repository.findById(DESTINATAIRE_ID)).thenReturn(Optional.of(destinataire));
        when(mapper.toDto(any(Destinataire.class))).thenReturn(destinataireDTO);

        Optional<DestinataireDTO> result = service.findById(DESTINATAIRE_ID);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(DESTINATAIRE_ID);
        verify(repository).findById(DESTINATAIRE_ID);
    }

    @Test
    void findById_WhenDestinataireNotExists_ShouldReturnEmpty() {
        when(repository.findById("unknown_id")).thenReturn(Optional.empty());

        Optional<DestinataireDTO> result = service.findById("unknown_id");

        assertThat(result).isEmpty();
        verify(repository).findById("unknown_id");
    }

    @Test
    void save_ShouldSaveAndReturnDestinataire() {
        when(mapper.toEntity(destinataireDTO)).thenReturn(destinataire);
        when(repository.save(destinataire)).thenReturn(destinataire);
        when(mapper.toDto(destinataire)).thenReturn(destinataireDTO);

        DestinataireDTO result = service.save(destinataireDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(DESTINATAIRE_ID);
        verify(repository).save(destinataire);
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        service.deleteById(DESTINATAIRE_ID);
        verify(repository).deleteById(DESTINATAIRE_ID);
    }
}