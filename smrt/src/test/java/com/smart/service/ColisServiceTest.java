package com.smart.service;

import com.smart.dto.*;
import com.smart.entity.*;
import com.smart.entity.Enum.PrioriteEnum;
import com.smart.entity.Enum.StatutColis;
import com.smart.mapper.*;
import com.smart.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
@Slf4j
class ColisServiceTest {

    @Mock
    private ColisRepository colisRepository;
    @Mock
    private LivreurRepository livreurRepository;
    @Mock
    private HistoriqueLivraisonRepository historiqueLivraisonRepository;
    @Mock
    private ColisMapper colisMapper;
    @Mock
    private ColisProduitRepository colisProduitRepository;
    @Mock
    private ProduitRepository produitRepository;
    @Mock
    private DestinataireRepository destinataireRepository;
    @Mock
    private ClientExpediteurRepository clientExpediteurRepository;
    @Mock
    private ZoneRepository zoneRepository;
    @Mock
    private ProduitMapper produitMapper;
    @Mock
    private ColisProduitMapper colisProduitMapper;
    @Mock
    private ClientExpediteurMapper clientExpediteurMapper;
    @Mock
    private DestinataireMapper destinataireMapper;
    @Mock
    private ZoneMapper zoneMapper;
    @Mock
    private HistoriqueLivraisonMapper historiqueLivraisonMapper;

    @InjectMocks
    private ColisService colisService;

    private Colis colis;
    private ColisDTO colisDTO;
    private ColisRequestDTO colisRequestDTO;
    private ClientExpediteur client;
    private Destinataire destinataire;
    private Zone zone;
    private Produit produit;
    private ColisProduit colisProduit;
    private HistoriqueLivraison historique;

    @BeforeEach
    void setUp() {
        // Initialisation des entités de test
        client = new ClientExpediteur();
        client.setId("client1");
        client.setNom("Client Test");
        client.setEmail("client@test.com");

        destinataire = new Destinataire();
        destinataire.setId("dest1");
        destinataire.setNom("Destinataire Test");
        destinataire.setEmail("dest@test.com");

        zone = new Zone();
        zone.setId("zone1");
        zone.setNom("Zone Test");
        zone.setCodePostal("75000");

        produit = new Produit();
        produit.setId("prod1");
        produit.setNom("Produit Test");
        produit.setPrix(100.0);
        ProduitDTO produitDTO = new ProduitDTO();
        produitDTO.setNom("Produit Test");
        produitDTO.setCategorie("TEST");
        when(produitMapper.toEntity(any(ProduitDTO.class))).thenReturn(produit);
        when(produitMapper.toDto(any(Produit.class))).thenReturn(produitDTO);

        colis = new Colis();
        colis.setId("colis1");
        colis.setDescription("Colis de test");
        colis.setPoids(1.5);
        colis.setPriorite(PrioriteEnum.HAUTE);
        colis.setVilleDestination("Paris");
        colis.setStatut(StatutColis.CREE);
        colis.setClientExpediteur(client);
        colis.setDestinataire(destinataire);
        colis.setZoneLivraison(zone);

        colisDTO = new ColisDTO();
        colisDTO.setId("colis1");
        colisDTO.setDescription("Colis de test");
        colisDTO.setStatut(StatutColis.CREE);

        colisRequestDTO = new ColisRequestDTO();
        colisRequestDTO.setDescription("Colis de test");
        colisRequestDTO.setPoids(1.5);
        colisRequestDTO.setPriorite("HAUTE");
        colisRequestDTO.setVilleDestination("Paris");
        
        ClientExpediteurDTO clientExpediteurDTO = new ClientExpediteurDTO();
        clientExpediteurDTO.setId(client.getId());
        clientExpediteurDTO.setNom(client.getNom());
        clientExpediteurDTO.setEmail(client.getEmail());
        colisRequestDTO.setClientExpediteur(clientExpediteurDTO);

        DestinataireDTO destinataireDTO = new DestinataireDTO();
        destinataireDTO.setId(destinataire.getId());
        destinataireDTO.setNom(destinataire.getNom());
        destinataireDTO.setEmail(destinataire.getEmail());
        colisRequestDTO.setDestinataire(destinataireDTO);

        ZoneDTO zoneDTO = new ZoneDTO();
        zoneDTO.setId(zone.getId());
        zoneDTO.setNom(zone.getNom());
        zoneDTO.setCodePostal(zone.getCodePostal());
        colisRequestDTO.setZone(zoneDTO);
        
        colisRequestDTO.setProduits(Collections.emptyList());

        historique = new HistoriqueLivraison();
        historique.setId("hist1");
        historique.setColis(colis);
        historique.setStatut(StatutColis.CREE);
        historique.setDateChangement(LocalDateTime.now());
        historique.setCommentaire("Colis créé");
    }

    @Test
    @Transactional
    void createColisWithDetails_ShouldCreateNewColis() {
        // Arrange
        // 1. Configuration du client expéditeur
        when(clientExpediteurRepository.findById(anyString())).thenReturn(Optional.of(client));
        
        // 2. Configuration du destinataire
        DestinataireDTO destinataireDTO = new DestinataireDTO();
        destinataireDTO.setEmail("test@example.com");
        destinataireDTO.setNom("Destinataire Test");
        colisRequestDTO.setDestinataire(destinataireDTO);
        
        // 3. Configuration de la zone
        ZoneDTO zoneDTO = new ZoneDTO();
        zoneDTO.setNom("Zone Test");
        zoneDTO.setCodePostal("75000");
        colisRequestDTO.setZone(zoneDTO);
        
        // 4. Configuration du produit
        ProduitDTO produitDTO = new ProduitDTO();
        produitDTO.setNom("Produit Test");
        produitDTO.setCategorie("TEST");
        
        ColisProduitDTO colisProduitDTO = new ColisProduitDTO();
        colisProduitDTO.setProduit(produitDTO);
        colisProduitDTO.setQuantite(2);
        colisRequestDTO.setProduits(Collections.singletonList(colisProduitDTO));
        
        // 5. Configuration des mocks
        when(destinataireRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(destinataireMapper.toEntity(any(DestinataireDTO.class))).thenReturn(destinataire);
        
        // Configuration du mock pour la zone
        when(zoneRepository.findByNomAndCodePostal("Zone Test", "75000"))
            .thenReturn(Optional.empty());
        when(zoneMapper.toEntity(any(ZoneDTO.class))).thenReturn(zone);
        when(zoneRepository.save(any(Zone.class))).thenReturn(zone);
        
        when(colisRepository.save(any(Colis.class))).thenReturn(colis);
        
        when(produitRepository.findByNomAndCategorie(anyString(), anyString()))
            .thenReturn(Optional.empty());
        when(produitRepository.save(any(Produit.class))).thenReturn(produit);
        
        // Configuration des mappers
        when(produitMapper.toEntity(any(ProduitDTO.class))).thenReturn(produit);
        when(colisMapper.toDto(any(Colis.class))).thenReturn(colisDTO);
        
        // Configuration du mock pour la création de l'historique
        when(historiqueLivraisonRepository.save(any(HistoriqueLivraison.class)))
            .thenAnswer(i -> i.getArgument(0));

        // Act
        ColisDTO result = colisService.createColisWithDetails("client1", colisRequestDTO);

        // Assert
        assertNotNull(result, "Le résultat ne devrait pas être null");
        assertEquals("colis1", result.getId(), "L'ID du colis ne correspond pas");
        
        // Vérification des appels aux repositories
        verify(clientExpediteurRepository).findById(anyString());
        verify(destinataireRepository).findByEmail(anyString());
        verify(destinataireMapper).toEntity(any(DestinataireDTO.class));
        verify(zoneRepository).findByNomAndCodePostal("Zone Test", "75000");
        verify(zoneMapper).toEntity(any(ZoneDTO.class));
        verify(zoneRepository).save(any(Zone.class));
        verify(colisRepository).save(any(Colis.class));
        verify(produitRepository).findByNomAndCategorie(anyString(), anyString());
        verify(produitRepository).save(any(Produit.class));
        verify(historiqueLivraisonRepository).save(any(HistoriqueLivraison.class));
        
        // Vérification des mappers
        verify(colisMapper).toDto(any(Colis.class));
        verify(produitMapper).toEntity(any(ProduitDTO.class));
    }

    @Test
    void findColisByClientExpediteur_ShouldReturnColisPage() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Colis> colisPage = new PageImpl<>(List.of(colis));
        when(colisRepository.findByClientExpediteurId(anyString(), any(Pageable.class))).thenReturn(colisPage);
        when(colisMapper.toDto(any(Colis.class))).thenReturn(colisDTO);

        // Act
        Page<ColisDTO> result = colisService.findColisByClientExpediteur("client1", pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(colisRepository).findByClientExpediteurId("client1", pageable);
    }

    @Test
    void findColisByClientExpediteurAndStatut_ShouldReturnFilteredColis() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Colis> colisPage = new PageImpl<>(List.of(colis));
        when(colisRepository.findByClientExpediteurIdAndStatut(anyString(), any(StatutColis.class), any(Pageable.class)))
                .thenReturn(colisPage);
        when(colisMapper.toDto(any(Colis.class))).thenReturn(colisDTO);

        // Act
        Page<ColisDTO> result = colisService.findColisByClientExpediteurAndStatut(
                "client1", "CREE", pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(colisRepository).findByClientExpediteurIdAndStatut("client1", StatutColis.CREE, pageable);
    }

    @Test
    void findById_WhenColisExists_ShouldReturnColis() {
        // Arrange
        when(colisRepository.findById("colis1")).thenReturn(Optional.of(colis));
        when(colisMapper.toDto(colis)).thenReturn(colisDTO);

        // Act
        Optional<ColisDTO> result = colisService.findById("colis1");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("colis1", result.get().getId());
        verify(colisRepository).findById("colis1");
    }

    @Test
    @Transactional
    void updateStatus_ShouldUpdateColisStatus() {
        // Arrange
        when(colisRepository.findById("colis1")).thenReturn(Optional.of(colis));
        when(colisRepository.save(any(Colis.class))).thenReturn(colis);
        when(historiqueLivraisonRepository.save(any(HistoriqueLivraison.class))).thenReturn(historique);
        when(colisMapper.toDto(colis)).thenReturn(colisDTO);

        // Act
        ColisDTO result = colisService.updateStatus("colis1", "EN_STOCK", "En cours de livraison");

        // Assert
        assertNotNull(result);
        assertEquals(StatutColis.CREE, result.getStatut());
        verify(colisRepository).save(any(Colis.class));
        verify(historiqueLivraisonRepository).save(any(HistoriqueLivraison.class));
    }

        @Test
    void getStatistiques_ShouldReturnStatisticsMap() {
        // Arrange
        String livreurId = "liv1";
        String zoneId = "zone1";
        Double poidsTotal = 50.5;
        Long nombreColis = 5L;

        when(colisRepository.getTotalPoidsByLivreurAndZone(livreurId, zoneId)).thenReturn(poidsTotal);
        when(colisRepository.getNombreColisByLivreurAndZone(livreurId, zoneId)).thenReturn(nombreColis);

        // Act
        Map<String, Object> result = colisService.getStatistiques(livreurId, zoneId);

        // Assert
        assertNotNull(result);
        assertEquals(poidsTotal, result.get("poidsTotal"));
        assertEquals(nombreColis, result.get("nombreColis"));
        
        verify(colisRepository).getTotalPoidsByLivreurAndZone(livreurId, zoneId);
        verify(colisRepository).getNombreColisByLivreurAndZone(livreurId, zoneId);
    }

    @Test
    void groupBy_WithZone_ShouldReturnGroupedByZone() {
        // Arrange
        String field = "zone";
        List<Object[]> zoneResults = List.of(
            new Object[]{"Zone 1", 3L},
            new Object[]{"Zone 2", 2L}
        );
        
        when(colisRepository.groupByZone()).thenReturn(zoneResults);
        when(colisRepository.findByZoneNom("Zone 1")).thenReturn(List.of(colis));
        when(colisRepository.findByZoneNom("Zone 2")).thenReturn(List.of(colis, colis));
        when(colisMapper.toDto(colis)).thenReturn(colisDTO);

        // Act
        Map<String, Object> result = colisService.groupBy(field);

        // Assert
        assertNotNull(result);
        assertEquals("zone", result.get("groupBy"));
        assertTrue(result.containsKey("data"));
        
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) result.get("data");
        assertEquals(2, data.size());
        assertTrue(data.containsKey("Zone 1"));
        assertTrue(data.containsKey("Zone 2"));
    }

    @Test
    void groupBy_WithStatut_ShouldReturnGroupedByStatut() {
        // Arrange
        String field = "statut";
        colis.setStatut(StatutColis.LIVRE);
        List<Object[]> statutResults = List.of(
            new Object[]{StatutColis.CREE, 5L},
            new Object[]{StatutColis.LIVRE, 3L}
        );
        
        when(colisRepository.groupByStatut()).thenReturn(statutResults);
        when(colisRepository.findByStatut(StatutColis.CREE)).thenReturn(List.of(colis, colis, colis, colis, colis));
        when(colisRepository.findByStatut(StatutColis.LIVRE)).thenReturn(List.of(colis, colis, colis));
        when(colisMapper.toDto(colis)).thenReturn(colisDTO);

        // Act
        Map<String, Object> result = colisService.groupBy(field);

        // Assert
        assertNotNull(result);
        assertEquals("statut", result.get("groupBy"));
        assertTrue(result.containsKey("data"));
    }

    @Test
    void groupBy_WithPriorite_ShouldReturnGroupedByPriorite() {
        // Arrange
        String field = "priorite";
        colis.setPriorite(PrioriteEnum.HAUTE);
        List<Object[]> prioriteResults = List.of(
            new Object[]{PrioriteEnum.HAUTE, 4L},
            new Object[]{PrioriteEnum.MOYENNE, 6L}
        );
        
        when(colisRepository.groupByPriorite()).thenReturn(prioriteResults);
        when(colisRepository.findByPriorite(PrioriteEnum.HAUTE)).thenReturn(List.of(colis, colis, colis, colis));
        when(colisRepository.findByPriorite(PrioriteEnum.MOYENNE)).thenReturn(List.of(colis, colis, colis, colis, colis, colis));
        when(colisMapper.toDto(colis)).thenReturn(colisDTO);

        // Act
        Map<String, Object> result = colisService.groupBy(field);

        // Assert
        assertNotNull(result);
        assertEquals("priorite", result.get("groupBy"));
        assertTrue(result.containsKey("data"));
    }

    @Test
    void groupBy_WithInvalidField_ShouldThrowIllegalArgumentException() {
        // Arrange
        String invalidField = "invalid";
        
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> colisService.groupBy(invalidField)
        );
        
        assertEquals("Champ de regroupement invalide: " + invalidField, exception.getMessage());
    }

    @Test
    void findByLivreurIdAndStatut_WithStatut_ShouldReturnFilteredColis() {
        // Arrange
        String livreurId = "liv1";
        String statut = "LIVRE";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Colis> colisPage = new PageImpl<>(List.of(colis));
        
        when(colisRepository.findByLivreurIdAndStatut(livreurId, StatutColis.LIVRE, pageable))
            .thenReturn(colisPage);
        when(colisMapper.toDto(colis)).thenReturn(colisDTO);

        // Act
        Page<ColisDTO> result = colisService.findByLivreurIdAndStatut(livreurId, statut, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(colisRepository).findByLivreurIdAndStatut(livreurId, StatutColis.LIVRE, pageable);
    }

    @Test
    void findByLivreurIdAndStatut_WithoutStatut_ShouldReturnAllColisForLivreur() {
        // Arrange
        String livreurId = "liv1";
        String statut = "";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Colis> colisPage = new PageImpl<>(List.of(colis, colis));
        
        when(colisRepository.findByLivreurId(livreurId, pageable)).thenReturn(colisPage);
        when(colisMapper.toDto(colis)).thenReturn(colisDTO);

        // Act
        Page<ColisDTO> result = colisService.findByLivreurIdAndStatut(livreurId, statut, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(colisRepository).findByLivreurId(livreurId, pageable);
        verify(colisRepository, never()).findByLivreurIdAndStatut(anyString(), any(StatutColis.class), any(Pageable.class));
    }

    @Test
    void findByLivreurIdAndStatut_WithNullStatut_ShouldReturnAllColisForLivreur() {
        // Arrange
        String livreurId = "liv1";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Colis> colisPage = new PageImpl<>(List.of(colis, colis, colis));
        
        when(colisRepository.findByLivreurId(livreurId, pageable)).thenReturn(colisPage);
        when(colisMapper.toDto(colis)).thenReturn(colisDTO);

        // Act
        Page<ColisDTO> result = colisService.findByLivreurIdAndStatut(livreurId, null, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getTotalElements());
        verify(colisRepository).findByLivreurId(livreurId, pageable);
        verify(colisRepository, never()).findByLivreurIdAndStatut(anyString(), any(StatutColis.class), any(Pageable.class));
    }
}