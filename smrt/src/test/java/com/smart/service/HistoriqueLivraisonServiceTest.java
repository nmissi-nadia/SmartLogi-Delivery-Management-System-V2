package com.smart.service;


import com.smart.common.src.main.java.com.smart.Enum.StatutColis;
import com.smart.common.src.main.java.com.smart.entity.Colis;
import com.smart.common.src.main.java.com.smart.entity.HistoriqueLivraison;
import com.smart.livraison.src.main.java.com.smart.livraison.dto.HistoriqueLivraisonDTO;
import com.smart.livraison.src.main.java.com.smart.livraison.mapper.HistoriqueLivraisonMapper;
import com.smart.livraison.src.main.java.com.smart.livraison.repository.HistoriqueLivraisonRepository;
import com.smart.livraison.src.main.java.com.smart.livraison.service.HistoriqueLivraisonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoriqueLivraisonServiceTest {

    @Mock
    private HistoriqueLivraisonRepository repository;

    @Mock
    private HistoriqueLivraisonMapper mapper;

    @InjectMocks
    private HistoriqueLivraisonService service;

    private HistoriqueLivraison historique;
    private HistoriqueLivraisonDTO historiqueDTO;
    private final String HISTORIQUE_ID = "123e4567-e89b-12d3-a456-426614174000";

    @BeforeEach
    void setUp() {
        Colis colis = new Colis();
        colis.setId("colis-123");

        historique = new HistoriqueLivraison();
        historique.setId(HISTORIQUE_ID);
        historique.setColis(colis);
        historique.setStatut(StatutColis.LIVRE);
        historique.setDateChangement(LocalDateTime.now());
        historique.setCommentaire("Livraison effectuée avec succès");

        historiqueDTO = new HistoriqueLivraisonDTO();
        historiqueDTO.setId(HISTORIQUE_ID);
        historiqueDTO.setColisId("colis-123");
        historiqueDTO.setStatut(StatutColis.LIVRE.toString());
        historiqueDTO.setDateChangement(LocalDateTime.now());
        historiqueDTO.setCommentaire("Livraison effectuée avec succès");
    }

    @Test
    void findAll_ShouldReturnAllHistoriques() {
        when(repository.findAll()).thenReturn(List.of(historique));
        when(mapper.toDto(any(HistoriqueLivraison.class))).thenReturn(historiqueDTO);

        List<HistoriqueLivraisonDTO> result = service.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(HISTORIQUE_ID);
        verify(repository).findAll();
    }

    @Test
    void findById_WhenHistoriqueExists_ShouldReturnHistorique() {
        when(repository.findById(HISTORIQUE_ID)).thenReturn(Optional.of(historique));
        when(mapper.toDto(any(HistoriqueLivraison.class))).thenReturn(historiqueDTO);

        Optional<HistoriqueLivraisonDTO> result = service.findById(HISTORIQUE_ID);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(HISTORIQUE_ID);
        verify(repository).findById(HISTORIQUE_ID);
    }

    @Test
    void findById_WhenHistoriqueNotExists_ShouldReturnEmpty() {
        when(repository.findById("unknown_id")).thenReturn(Optional.empty());

        Optional<HistoriqueLivraisonDTO> result = service.findById("unknown_id");

        assertThat(result).isEmpty();
        verify(repository).findById("unknown_id");
    }

    @Test
    void save_ShouldSaveAndReturnHistorique() {
        when(mapper.toEntity(historiqueDTO)).thenReturn(historique);
        when(repository.save(historique)).thenReturn(historique);
        when(mapper.toDto(historique)).thenReturn(historiqueDTO);

        HistoriqueLivraisonDTO result = service.save(historiqueDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(HISTORIQUE_ID);
        verify(repository).save(historique);
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        service.deleteById(HISTORIQUE_ID);
        verify(repository).deleteById(HISTORIQUE_ID);
    }

    @Test
    void save_ShouldSetDateChangementIfNotProvided() {
        // Given
        historiqueDTO.setDateChangement(null);
        when(mapper.toEntity(historiqueDTO)).thenReturn(historique);
        when(repository.save(any(HistoriqueLivraison.class))).thenAnswer(invocation -> {
            HistoriqueLivraison h = invocation.getArgument(0);
            h.setDateChangement(LocalDateTime.now());
            return h;
        });
        when(mapper.toDto(any(HistoriqueLivraison.class))).thenReturn(historiqueDTO);

        // When
        service.save(historiqueDTO);

        // Then
        verify(repository).save(any(HistoriqueLivraison.class));
        assertThat(historique.getDateChangement()).isNotNull();
    }
}