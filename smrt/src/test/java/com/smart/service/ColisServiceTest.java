package com.smart.service;

import com.smart.dto.*;
import com.smart.entity.*;
import com.smart.entity.Enum.PrioriteEnum;
import com.smart.entity.Enum.StatutColis;
import com.smart.mapper.*;
import com.smart.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ColisServiceTest {

    @Mock private ColisRepository colisRepository;
    @Mock private LivreurRepository livreurRepository;
    @Mock private HistoriqueLivraisonRepository historiqueLivraisonRepository;
    @Mock private ColisMapper colisMapper;
    @Mock private ColisProduitRepository colisProduitRepository;
    @Mock private ProduitRepository produitRepository;
    @Mock private DestinataireRepository destinataireRepository;
    @Mock private ClientExpediteurRepository clientExpediteurRepository;
    @Mock private ZoneRepository zoneRepository;
    @Mock private ProduitMapper produitMapper;
    @Mock private ColisProduitMapper colisProduitMapper;
    @Mock private ClientExpediteurMapper clientExpediteurMapper;
    @Mock private DestinataireMapper destinataireMapper;
    @Mock private ZoneMapper zoneMapper;
    @Mock private HistoriqueLivraisonMapper historiqueLivraisonMapper;

    @InjectMocks
    private ColisService colisService;

    private ColisRequestDTO colisRequestDTO;
    private Colis colisEntity;
    private ColisDTO colisDTO;
    private ClientExpediteur clientExpediteur;
    private Destinataire destinataire;

    @BeforeEach
    void setUp() {
        // Initialisation des données de test
        clientExpediteur = new ClientExpediteur();
        clientExpediteur.setId("client-123");

        destinataire = new Destinataire();
        destinataire.setId("dest-456");
        destinataire.setEmail("dest@test.com");

        colisRequestDTO = new ColisRequestDTO();
        colisRequestDTO.setDescription("Test Colis");
        colisRequestDTO.setPoids(10.5); // Utilisation de Double ici
        colisRequestDTO.setPriorite("HAUTE");
        colisRequestDTO.setVilleDestination("Paris");

        ClientExpediteurDTO clientDTO = new ClientExpediteurDTO();
        clientDTO.setId("client-123");
        colisRequestDTO.setClientExpediteur(clientDTO);

        DestinataireDTO destDTO = new DestinataireDTO();
        destDTO.setEmail("dest@test.com");
        colisRequestDTO.setDestinataire(destDTO);

        colisEntity = new Colis();
        colisEntity.setId("colis-789");
        colisEntity.setPoids(10.5); // Utilisation de Double
        colisEntity.setStatut(StatutColis.CREE);
        colisEntity.setClientExpediteur(clientExpediteur);

        colisDTO = new ColisDTO();
        colisDTO.setId("colis-789");
        colisDTO.setPoids(10.5); // Utilisation de Double
    }

    @Test
    void createColisWithDetails_ShouldSucceed() {
        // Given
        when(clientExpediteurRepository.findById(any())).thenReturn(Optional.of(clientExpediteur));
        when(destinataireRepository.findByEmail(any())).thenReturn(Optional.of(destinataire));
        when(colisRepository.save(any(Colis.class))).thenAnswer(invocation -> {
            Colis c = invocation.getArgument(0);
            c.setId("generated-id");
            return c;
        });
        when(colisMapper.toDto(any(Colis.class))).thenReturn(colisDTO);

        // When
        ColisDTO result = colisService.createColisWithDetails("client-123", colisRequestDTO);

        // Then
        assertNotNull(result);
        verify(colisRepository).save(any(Colis.class));
        verify(historiqueLivraisonRepository).save(any(HistoriqueLivraison.class));

        // Vérification que le poids est bien traité comme un Double
        assertEquals(10.5, result.getPoids());
    }

    @Test
    void createColisWithDetails_WithProduits_ShouldCalculatePriceCorrectly() {
        // Given
        ColisProduitDTO prodDTO = new ColisProduitDTO();
        ProduitDTO pDto = new ProduitDTO();
        pDto.setNom("Produit Test");
        pDto.setCategorie("Cat");
        prodDTO.setProduit(pDto);
        prodDTO.setQuantite(2);
        colisRequestDTO.setProduits(List.of(prodDTO));

        Produit produitEntity = new Produit();
        produitEntity.setId("prod-1");
        produitEntity.setPrix(50.0); // Prix unitaire Double

        when(clientExpediteurRepository.findById(any())).thenReturn(Optional.of(clientExpediteur));
        when(destinataireRepository.findByEmail(any())).thenReturn(Optional.of(destinataire));
        when(colisRepository.save(any(Colis.class))).thenReturn(colisEntity);
        when(produitRepository.findByNomAndCategorie(any(), any())).thenReturn(Optional.of(produitEntity));
        when(colisMapper.toDto(any(Colis.class))).thenReturn(colisDTO);

        // When
        colisService.createColisWithDetails("client-123", colisRequestDTO);

        // Then
        verify(colisProduitRepository).save(argThat(cp ->
                cp.getQuantite() == 2 &&
                        cp.getPrix() == 100.0 // 50.0 * 2 = 100.0 (Double check)
        ));
    }

    @Test
    void update_ShouldUpdatePoidsCorrectly() {
        // Given
        String colisId = "colis-789";
        ColisDTO updateDTO = new ColisDTO();
        updateDTO.setPoids(20.0); // Nouveau poids en Double
        updateDTO.setDescription("Updated Desc");

        when(colisRepository.findById(colisId)).thenReturn(Optional.of(colisEntity));
        when(colisRepository.save(any(Colis.class))).thenAnswer(i -> i.getArgument(0));
        when(colisMapper.toDto(any(Colis.class))).thenAnswer(i -> {
            Colis c = i.getArgument(0);
            ColisDTO d = new ColisDTO();
            d.setPoids(c.getPoids());
            return d;
        });

        // When
        ColisDTO result = colisService.update(colisId, updateDTO);

        // Then
        assertEquals(20.0, result.getPoids());
        verify(colisRepository).save(argThat(c -> c.getPoids().equals(20.0)));
    }

    @Test
    void updateStatus_ShouldUpdateStatusAndHistory() {
        // Given
        String colisId = "colis-789";
        String newStatus = "EN_TRANSIT";
        String commentaire = "En route";

        when(colisRepository.findById(colisId)).thenReturn(Optional.of(colisEntity));
        when(colisRepository.save(any(Colis.class))).thenReturn(colisEntity);
        when(colisMapper.toDto(any(Colis.class))).thenReturn(colisDTO);

        // When
        colisService.updateStatus(colisId, newStatus, commentaire);

        // Then
        verify(colisRepository).save(argThat(c -> c.getStatut() == StatutColis.EN_TRANSIT));
        verify(historiqueLivraisonRepository).save(argThat(h ->
                h.getStatut() == StatutColis.EN_TRANSIT &&
                        h.getCommentaire().equals(commentaire)
        ));
    }

    @Test
    void deleteById_ShouldDeleteIfExists() {
        // Given
        String id = "exist-id";
        when(colisRepository.existsById(id)).thenReturn(true);

        // When
        colisService.deleteById(id);

        // Then
        verify(colisRepository).deleteById(id);
    }

    @Test
    void deleteById_ShouldThrowIfNotFound() {
        String id = "unknown-id";
        when(colisRepository.existsById(id)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> colisService.deleteById(id));
    }
}
