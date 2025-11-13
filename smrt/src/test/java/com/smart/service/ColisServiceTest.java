package com.smart.service;
import com.smart.entity.Enum.StatutColis;
import static com.smart.entity.Enum.StatutColis.CREE;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import com.smart.dto.*;
import com.smart.entity.*;
import com.smart.mapper.*;
import com.smart.repository.*;
import com.smart.service.ColisService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ColisServiceTest {

    @InjectMocks
    private ColisService colisService;

    @Mock
    private ClientExpediteurRepository clientExpediteurRepository;

    @Mock
    private DestinataireRepository destinataireRepository;

    @Mock
    private ColisRepository colisRepository;

    @Mock
    private LivreurRepository livreurRepository;

    @Mock
    private HistoriqueLivraisonRepository historiqueLivraisonRepository;

    @Mock
    private ColisMapper colisMapper;

    @Mock
    private ClientExpediteurMapper clientExpediteurMapper;

    @Mock
    private DestinataireMapper destinataireMapper;

    @Mock
    private HistoriqueLivraisonMapper historiqueLivraisonMapper;

    @BeforeEach
    void setup() {
        // Les autres mocks sont déjà injectés via @InjectMocks
    }
    @Test
    void testAssignLivreur_Success() {
        // Données
        Colis colis = new Colis();
        colis.setId("c1");

        Livreur livreur = new Livreur();
        livreur.setId("l1");

        // Configuration des mocks
        when(colisRepository.findById("c1")).thenReturn(Optional.of(colis));
        when(livreurRepository.findById("l1")).thenReturn(Optional.of(livreur));
        when(colisRepository.save(any(Colis.class))).thenAnswer(i -> i.getArgument(0));
        
        // Configuration spécifique pour le mapper
        ColisDTO expectedDto = new ColisDTO();
        expectedDto.setId("c1");
        when(colisMapper.toDto(any(Colis.class))).thenReturn(expectedDto);

        // Appel
        ColisDTO result = colisService.assignLivreur("c1", "l1");

        // Vérifications
        verify(colisRepository).save(colis);
        assertThat(colis.getLivreur()).isEqualTo(livreur);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("c1");
    }

    @Test
    void testAssignLivreur_ColisNotFound() {
        when(colisRepository.findById("c1")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> colisService.assignLivreur("c1", "l1"));
    }

    @Test
    void testAssignLivreur_LivreurNotFound() {
        when(colisRepository.findById("c1")).thenReturn(Optional.of(new Colis()));
        when(livreurRepository.findById("l1")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> colisService.assignLivreur("c1", "l1"));
    }

    @Test
    void testCreateColisWithDetails() {
        // Préparer DTOs
        ClientExpediteurDTO clientDTO = new ClientExpediteurDTO();
        clientDTO.setNom("Client Test");
        DestinataireDTO destinataireDTO = new DestinataireDTO();
        destinataireDTO.setEmail("dest@test.com");
        ColisRequestDTO requestDTO = new ColisRequestDTO();
        requestDTO.setDescription("Colis test");
        requestDTO.setPoids(5.0);
        requestDTO.setPriorite("HAUTE");
        requestDTO.setVilleDestination("Casablanca");
        requestDTO.setClientExpediteur(clientDTO);
        requestDTO.setDestinataire(destinataireDTO);

        // Mock findById et save
        when(clientExpediteurRepository.findById(anyString())).thenReturn(Optional.empty());
        when(clientExpediteurMapper.toEntity(any(ClientExpediteurDTO.class)))
                .thenAnswer(i -> {
                    ClientExpediteur c = new ClientExpediteur();
                    c.setNom(((ClientExpediteurDTO) i.getArgument(0)).getNom());
                    return c;
                });
        when(clientExpediteurRepository.save(any(ClientExpediteur.class)))
                .thenAnswer(i -> i.getArgument(0));

        when(destinataireRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(destinataireMapper.toEntity(any(DestinataireDTO.class)))
                .thenAnswer(i -> {
                    Destinataire d = new Destinataire();
                    d.setEmail(((DestinataireDTO) i.getArgument(0)).getEmail());
                    return d;
                });
        when(destinataireRepository.save(any(Destinataire.class))).thenAnswer(i -> i.getArgument(0));

        when(colisRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(colisMapper.toDto((Colis) any())).thenReturn(new ColisDTO());


        // Appel
        ColisDTO result = colisService.createColisWithDetails("clientIdTest", requestDTO);

        // Vérifications
        verify(clientExpediteurRepository, times(1)).save(any());
        verify(destinataireRepository, times(1)).save(any());
        verify(colisRepository, times(1)).save(any());
        verify(historiqueLivraisonRepository, times(1)).save(any());

        assertThat(result).isNotNull();
    }

    @Test
    void testUpdateStatus_Success() {
        // Arrange
        Colis colis = new Colis();
        colis.setId("c1");
        colis.setStatut(CREE); // Définir un statut initial

        ColisDTO expectedDto = new ColisDTO();
        expectedDto.setId("c1");
        expectedDto.setStatut(CREE);

        // Configuration des mocks
        when(colisRepository.findById("c1")).thenReturn(Optional.of(colis));
        when(colisRepository.save(any(Colis.class))).thenAnswer(i -> i.getArgument(0));
        when(historiqueLivraisonRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(colisMapper.toDto(any(Colis.class))).thenReturn(expectedDto); // <-- Important !

        // Act
        ColisDTO result = colisService.updateStatus("c1", "CREE", "Test changement");

        // Assert
        verify(colisRepository).save(colis);
        verify(historiqueLivraisonRepository).save(any());
        assertThat(result).isNotNull();
        assertThat(result.getStatut()).isEqualTo(CREE);
    }

    @Test
    void testUpdateStatus_ColisNotFound() {
        when(colisRepository.findById("c1")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> colisService.updateStatus("c1", "CREE", "Test"));
    }

    @Test
    void testUpdateStatus_InvalidStatus() {
        when(colisRepository.findById("c1")).thenReturn(Optional.of(new Colis()));

        assertThrows(IllegalArgumentException.class,
                () -> colisService.updateStatus("c1", "XYZ", "Commentaire"));
    }

    @Test
    void testDeleteColis_Success() {
        // Arrange
        String colisId = "c1";
        when(colisRepository.existsById(colisId)).thenReturn(true);
        
        // Act
        colisService.deleteById(colisId);
        
        // Assert
        verify(colisRepository).deleteById(colisId);
    }

    @Test
    void testDeleteColis_NotFound() {
        // Arrange
        String colisId = "c1";
        when(colisRepository.existsById(colisId)).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> colisService.deleteById(colisId),
            "Une exception aurait dû être levée car le colis n'existe pas");
        verify(colisRepository, never()).deleteById(anyString());
    }

    @Test
    void testUpdateColis_Success() {
        // Arrange
        String colisId = "c1";
        Colis existingColis = new Colis();
        existingColis.setId(colisId);
        existingColis.setDescription("Ancienne description");

        ColisDTO updatedDto = new ColisDTO();
        updatedDto.setDescription("Nouvelle description");

        when(colisRepository.findById(colisId)).thenReturn(Optional.of(existingColis));
        when(colisRepository.save(any(Colis.class))).thenAnswer(i -> i.getArgument(0));
        when(colisMapper.toDto(any(Colis.class))).thenAnswer(i -> {
            Colis c = i.getArgument(0);
            ColisDTO dto = new ColisDTO();
            dto.setId(c.getId());
            dto.setDescription(c.getDescription());
            return dto;
        });

        // Act
        ColisDTO result = colisService.update(colisId, updatedDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo("Nouvelle description");
        verify(colisRepository).save(existingColis);
    }

    @Test
    void testUpdateColis_NotFound() {
        // Arrange
        String colisId = "c1";
        ColisDTO updatedDto = new ColisDTO();
        when(colisRepository.findById(colisId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> colisService.update(colisId, updatedDto));
        verify(colisRepository, never()).save(any());
    }

    @Test
    void testFindById_Success() {
        // Arrange
        String colisId = "c1";
        Colis colis = new Colis();
        colis.setId(colisId);
        ColisDTO colisDTO = new ColisDTO();
        colisDTO.setId(colisId);

        when(colisRepository.findById(colisId)).thenReturn(Optional.of(colis));
        when(colisMapper.toDto(colis)).thenReturn(colisDTO);

        // Act
        Optional<ColisDTO> result = colisService.findById(colisId);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(colisId);
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        String colisId = "c1";
        when(colisRepository.findById(colisId)).thenReturn(Optional.empty());

        // Act
        Optional<ColisDTO> result = colisService.findById(colisId);

        // Assert
        assertThat(result).isNotPresent();
    }

    @Test
    void testGetHistoriqueForColis_Success() {
        // Arrange
        String colisId = "c1";
        Colis colis = new Colis();
        colis.setId(colisId);

        HistoriqueLivraison historique = new HistoriqueLivraison();
        historique.setId("h1");
        historique.setColis(colis); // Set the colis object
        historique.setStatut(StatutColis.CREE); // Set the statut
        List<HistoriqueLivraison> historiques = Collections.singletonList(historique);

        when(historiqueLivraisonRepository.findByColisIdOrderByDateChangementDesc(colisId)).thenReturn(historiques);

        // Act
        List<HistoriqueLivraisonDTO> result = colisService.getHistoriqueForColis(colisId);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo("h1");
        assertThat(result.get(0).getColisId()).isEqualTo(colisId);
        assertThat(result.get(0).getStatut()).isEqualTo(StatutColis.CREE.name());
    }

    @Test
    void testGetHistoriqueForColis_Empty() {
        // Arrange
        String colisId = "c1";
        when(historiqueLivraisonRepository.findByColisIdOrderByDateChangementDesc(colisId)).thenReturn(Collections.emptyList());

        // Act
        List<HistoriqueLivraisonDTO> result = colisService.getHistoriqueForColis(colisId);

        // Assert
        assertThat(result).isEmpty();
    }
}
