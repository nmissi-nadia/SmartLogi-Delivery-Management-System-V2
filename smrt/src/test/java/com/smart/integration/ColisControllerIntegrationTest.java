package com.smart.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.dto.ColisDTO;
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

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@WithMockUser(roles = "GESTIONNAIRE_LOGISTIQUE")
public class ColisControllerIntegrationTest {

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
        savedColis = createTestColis("Colis de test", BigDecimal.valueOf(1.5),
                                   savedClient, savedDestinataire,
                                   savedLivreur, savedZone);
        savedColis = colisRepository.save(savedColis);
    }

    @Test
    public void testGetAllColis() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/colis")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").isNotEmpty())
                .andExpect(jsonPath("$.content[0].description").value(savedColis.getDescription()));
    }

    @Test
    public void testGetAllColisWithFilters() throws Exception {
        // Given
        Colis secondColis = createTestColis("Colis livré", BigDecimal.valueOf(3.0), savedClient, savedDestinataire, savedLivreur, savedZone);
        secondColis.setStatut(StatutColis.LIVRE);
        secondColis.setVilleDestination("Lyon");
        secondColis.setPriorite(PrioriteEnum.HAUTE);
        colisRepository.save(secondColis);

        // When & Then - Test with status filter
        mockMvc.perform(get("/api/colis")
                .param("statut", StatutColis.LIVRE.name())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].statut").value(StatutColis.LIVRE.name()));

        // When & Then - Test with ville filter
        mockMvc.perform(get("/api/colis")
                .param("ville", "Lyon")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].villeDestination").value("Lyon"));
    }

    @Test
    public void testSearchColis() throws Exception {
        // Given
        Colis colisRecherche = createTestColis("Colis urgent", BigDecimal.valueOf(2.0), savedClient, savedDestinataire, savedLivreur, savedZone);
        colisRepository.save(colisRecherche);

        // When & Then - Test search by keyword
        mockMvc.perform(get("/api/colis/recherche")
                .param("keyword", "urgent")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].description").value("Colis urgent"));
    }

    @Test
    public void testUpdateColis() throws Exception {
        // Given
        ColisDTO updateDto = new ColisDTO();
        updateDto.setDescription("Description mise à jour");
        updateDto.setPoids(BigDecimal.valueOf(5.0));
        updateDto.setStatut(StatutColis.COLLECTE);
        updateDto.setVilleDestination("Marseille");
        updateDto.setPriorite(PrioriteEnum.HAUTE);

        // When & Then
        mockMvc.perform(put("/api/colis/" + savedColis.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Description mise à jour"))
                .andExpect(jsonPath("$.statut").value(StatutColis.COLLECTE.name()))
                .andExpect(jsonPath("$.villeDestination").value("Marseille"));
    }

    @Test
    public void testGetHistoriqueColis() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/colis/" + savedColis.getId() + "/historique")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(empty())));
    }

    private Colis createTestColis(String description, BigDecimal poids,
                                 ClientExpediteur client, Destinataire destinataire,
                                 Livreur livreur, Zone zone) {
        Colis colis = new Colis();
        colis.setDescription(description);
        colis.setPoids(poids);
        colis.setStatut(StatutColis.CREE);
        colis.setPriorite(PrioriteEnum.MOYENNE);
        colis.setVilleDestination("Paris");
        colis.setClientExpediteur(client);
        colis.setDestinataire(destinataire);
        colis.setLivreur(livreur);
        colis.setZone(zone);
        return colis;
    }
}