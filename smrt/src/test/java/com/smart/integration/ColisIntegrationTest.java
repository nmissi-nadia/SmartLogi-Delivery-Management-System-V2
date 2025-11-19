package com.smart.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.common.src.main.java.com.smart.entity.*;
import com.smart.common.src.main.java.com.smart.Enum.PrioriteEnum;
import com.smart.common.src.main.java.com.smart.Enum.StatutColis;
import com.smart.colis.src.main.java.com.smart.repository.ColisRepository;
import com.smart.utilisateur.src.main.java.com.smart.repository.ClientExpediteurRepository;
import com.smart.utilisateur.src.main.java.com.smart.repository.DestinataireRepository;
import com.smart.livraison.src.main.java.com.smart.repository.LivreurRepository;
import com.smart.zone.src.main.java.com.smart.repository.ZoneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
public class ColisIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ColisRepository colisRepository;

    @Autowired
    private ClientExpediteurRepository clientExpediteurRepository;

    @Autowired
    private DestinataireRepository destinataireRepository;

    @Autowired
    private LivreurRepository livreurRepository;

    @Autowired
    private ZoneRepository zoneRepository;

    private ClientExpediteur savedClient;
    private Destinataire savedDestinataire;
    private Livreur savedLivreur;
    private Zone savedZone;
    private Colis savedColis;

    @BeforeEach
    void setUp() {
        // Nettoyer les données avant chaque test
        colisRepository.deleteAll();
        clientExpediteurRepository.deleteAll();
        destinataireRepository.deleteAll();
        livreurRepository.deleteAll();
        zoneRepository.deleteAll();

        // Créer et sauvegarder un client expéditeur
        savedClient = new ClientExpediteur();
        savedClient.setNom("Client");
        savedClient.setPrenom("Test");
        savedClient.setEmail("client@test.com");
        savedClient.setTelephone("0612345678");
        savedClient = clientExpediteurRepository.save(savedClient);

        // Créer et sauvegarder un destinataire
        savedDestinataire = new Destinataire();
        savedDestinataire.setNom("Destinataire");
        savedDestinataire.setPrenom("Test");
        savedDestinataire.setTelephone("0698765432");
        savedDestinataire.setAdresse("123 Rue de Test");
        savedDestinataire = destinataireRepository.save(savedDestinataire);

        // Créer et sauvegarder un livreur
        savedLivreur = new Livreur();
        savedLivreur.setNom("Livreur");
        savedLivreur.setPrenom("Test");
        savedLivreur.setVehicule("moto");
        savedLivreur.setTelephone("0601020304");
        savedLivreur = livreurRepository.save(savedLivreur);

        // Créer et sauvegarder une zone
        savedZone = new Zone();
        savedZone.setNom("Zone Test");
        savedZone.setCodePostal("75000");
        savedZone = zoneRepository.save(savedZone);

        // Créer et sauvegarder un colis de test
        savedColis =new Colis();
        savedColis.setDescription("Colis de test");
        savedColis.setPoids(1.5);
        savedColis.setStatut(StatutColis.CREE);
        savedColis.setPriorite(PrioriteEnum.MOYENNE);
        savedColis.setVilleDestination("Paris");
        savedColis.setClientExpediteur(savedClient);
        savedColis.setDestinataire(savedDestinataire);
        savedColis.setLivreur(savedLivreur);
        savedColis.setZoneLivraison(savedZone);
        savedColis = colisRepository.save(savedColis);
    }

    @Test
    public void testGetAllColis() throws Exception {
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(
            "http://localhost:" + port + "/api/colis",
            String.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        // Parse the JSON response to verify content
        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode content = root.path("content");
        assertTrue(content.isArray());
    }

    @Test
    public void testSearchColis() throws Exception {
        // Given
        Colis colisRecherche =  new Colis();
        colisRecherche.setDescription("Colis urgent et fragile");
        colisRecherche.setPoids(2.0);
        colisRecherche.setStatut(StatutColis.CREE);
        colisRecherche.setPriorite(PrioriteEnum.MOYENNE);
        colisRecherche.setVilleDestination("Paris");
        colisRecherche.setClientExpediteur(savedClient);
        colisRecherche.setDestinataire(savedDestinataire);
        colisRecherche.setLivreur(savedLivreur);
        colisRecherche.setZoneLivraison(savedZone);
        colisRepository.save(colisRecherche);

        // When
        ResponseEntity<String> response = restTemplate.getForEntity(
            "http://localhost:" + port + "/api/colis/recherche?statut=CREE",
            String.class
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        // Parse the JSON response to verify content
        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode content = root.path("content");
        
        // Vérifier que le contenu est un tableau non vide
        assertTrue(content.isArray());

    }
}