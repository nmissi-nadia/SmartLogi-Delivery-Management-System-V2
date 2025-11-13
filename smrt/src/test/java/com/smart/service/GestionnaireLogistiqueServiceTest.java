package com.smart.service;

import com.smart.entity.GestionnaireLogistique;
import com.smart.repository.GestionnaireLogistiqueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GestionnaireLogistiqueServiceTest {

    @Mock
    private GestionnaireLogistiqueRepository repository;

    @InjectMocks
    private GestionnaireLogistiqueService service;

    private GestionnaireLogistique gestionnaire;
    private final String GESTIONNAIRE_ID = "123e4567-e89b-12d3-a456-426614174000";

    @BeforeEach
    void setUp() {
        gestionnaire = new GestionnaireLogistique();
        gestionnaire.setId(GESTIONNAIRE_ID);
        gestionnaire.setNom("Benali");
        gestionnaire.setPrenom("Ahmed");
        gestionnaire.setEmail("ahmed.benali@example.com");
    }

    @Test
    void findAll_ShouldReturnAllGestionnaires() {
        when(repository.findAll()).thenReturn(List.of(gestionnaire));

        List<GestionnaireLogistique> result = service.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(GESTIONNAIRE_ID);
        verify(repository).findAll();
    }

    @Test
    void findById_WhenGestionnaireExists_ShouldReturnGestionnaire() {
        when(repository.findById(GESTIONNAIRE_ID)).thenReturn(Optional.of(gestionnaire));

        Optional<GestionnaireLogistique> result = service.findById(GESTIONNAIRE_ID);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(GESTIONNAIRE_ID);
        verify(repository).findById(GESTIONNAIRE_ID);
    }

    @Test
    void findById_WhenGestionnaireNotExists_ShouldReturnEmpty() {
        when(repository.findById("unknown_id")).thenReturn(Optional.empty());

        Optional<GestionnaireLogistique> result = service.findById("unknown_id");

        assertThat(result).isEmpty();
        verify(repository).findById("unknown_id");
    }

    @Test
    void save_ShouldSaveAndReturnGestionnaire() {
        when(repository.save(gestionnaire)).thenReturn(gestionnaire);

        GestionnaireLogistique result = service.save(gestionnaire);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(GESTIONNAIRE_ID);
        verify(repository).save(gestionnaire);
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        service.deleteById(GESTIONNAIRE_ID);
        verify(repository).deleteById(GESTIONNAIRE_ID);
    }
}