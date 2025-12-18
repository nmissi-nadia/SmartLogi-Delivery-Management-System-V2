package com.smart.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.entity.*;
import com.smart.entity.Enum.PrioriteEnum;
import com.smart.entity.Enum.StatutColis;
import com.smart.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@WithMockUser(roles = "GESTIONNAIRE_LOGISTIQUE")
public class ColisIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

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
        // When & Then
        mockMvc.perform(get("/api/colis")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
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

        // When & Then
        mockMvc.perform(get("/api/colis")
                        .param("statut", "CREE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));

    }
}