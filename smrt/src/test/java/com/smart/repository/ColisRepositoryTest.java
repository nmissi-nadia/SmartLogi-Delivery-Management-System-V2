package com.smart.repository;

import com.smart.entity.*;
import com.smart.entity.Enum.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@ActiveProfiles("test")
public class ColisRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ColisRepository colisRepository;

    @Autowired
    private ClientExpediteurRepository clientExpediteurRepository;

    @Autowired
    private LivreurRepository livreurRepository;

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private DestinataireRepository destinataireRepository;

    private Colis createTestColis() {
        return createTestColis(null, null, null, null);
    }

    private Colis createTestColis(ClientExpediteur client, Destinataire destinataire, 
                                Livreur livreur, Zone zone) {
        // Créer ou utiliser le client fourni
        if (client == null) {
            client = new ClientExpediteur();
            client.setId("client" + UUID.randomUUID().toString().substring(0, 8));
            client.setNom("Client Test");
            client.setEmail("client" + UUID.randomUUID().toString().substring(0, 4) + "@test.com");
            client.setPrenom("Prénom");
            client.setTelephone("01" + (10000000 + new Random().nextInt(90000000)));
            client = clientExpediteurRepository.saveAndFlush(client);
        }
        
        // Créer ou utiliser le destinataire fourni
        if (destinataire == null) {
            destinataire = new Destinataire();
            destinataire.setId("dest" + UUID.randomUUID().toString().substring(0, 8));
            destinataire.setNom("Destinataire Test");
            destinataire.setPrenom("Prénom");
            destinataire.setTelephone("06" + (10000000 + new Random().nextInt(90000000)));
            destinataire.setAdresse("123 Rue Test, Paris");
            destinataire.setEmail("dest" + UUID.randomUUID().toString().substring(0, 4) + "@test.com");
            destinataire = destinataireRepository.saveAndFlush(destinataire);
        }
        
        // Créer ou utiliser le livreur fourni
        if (livreur == null) {
            livreur = new Livreur();
            livreur.setId("liv" + UUID.randomUUID().toString().substring(0, 8));
            livreur.setNom("Livreur Test");
            livreur.setPrenom("Prénom");
            livreur.setTelephone("06" + (10000000 + new Random().nextInt(90000000)));
            livreur = livreurRepository.saveAndFlush(livreur);
        }
        
        // Créer ou utiliser la zone fournie
        if (zone == null) {
            zone = new Zone();
            zone.setId("zone" + UUID.randomUUID().toString().substring(0, 8));
            zone.setNom("Zone " + UUID.randomUUID().toString().substring(0, 4));
            zone.setCodePostal("75" + String.format("%03d", new Random().nextInt(1000)));
            zone = zoneRepository.saveAndFlush(zone);
        }
        
        // Créer le colis avec toutes les relations
        Colis colis = new Colis();
        colis.setId("colis" + UUID.randomUUID().toString().substring(0, 8));
        colis.setVilleDestination("Paris");
        colis.setStatut(StatutColis.CREE);
        colis.setPoids(BigDecimal.valueOf(2.5));
        colis.setDescription("Description test");
        colis.setPriorite(PrioriteEnum.MOYENNE);
        colis.setClientExpediteur(client);
        colis.setDestinataire(destinataire);
        colis.setLivreur(livreur);
        colis.setZone(zone);
        
        return colis;
    }

    @Test
    void findByStatut_ShouldReturnColisList() {
        // Arrange
        Colis colis = createTestColis();
        entityManager.persistAndFlush(colis);

        // Act
        List<Colis> result = colisRepository.findByStatut(StatutColis.CREE);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());  // Maintenant, cela devrait passer
        assertEquals(StatutColis.CREE, result.get(0).getStatut());
    }

    
}