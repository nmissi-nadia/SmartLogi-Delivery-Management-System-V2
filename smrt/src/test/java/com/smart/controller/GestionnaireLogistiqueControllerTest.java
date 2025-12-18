package com.smart.controller;

import com.smart.dto.ColisDTO;
import com.smart.entity.Enum.StatutColis;
import com.smart.entity.GestionnaireLogistique;
import com.smart.service.ColisService;
import com.smart.service.GestionnaireLogistiqueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GestionnaireLogistiqueControllerTest {

    @Mock
    private GestionnaireLogistiqueService gestionnaireService;

    @Mock
    private ColisService colisService;

    @InjectMocks
    private GestionnaireLogistiqueController controller;

    private GestionnaireLogistique gestionnaire;
    private ColisDTO colisDTO;

    @BeforeEach
    void setUp() {
        gestionnaire = new GestionnaireLogistique();
        gestionnaire.setId("gest1");
        gestionnaire.setNom("Dupont");
        gestionnaire.setEmail("gestionnaire@example.com");

        colisDTO = new ColisDTO();
        colisDTO.setId("colis1");
        colisDTO.setStatut(StatutColis.CREE);
    }

    @Test
    void getAll_ShouldReturnAllGestionnaires() {
        // Arrange
        when(gestionnaireService.findAll()).thenReturn(Arrays.asList(gestionnaire));

        // Act
        List<GestionnaireLogistique> result = controller.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("gest1", result.get(0).getId());
    }

    @Test
    void getById_WhenGestionnaireExists_ShouldReturnGestionnaire() {
        // Arrange
        when(gestionnaireService.findById("gest1")).thenReturn(Optional.of(gestionnaire));

        // Act
        ResponseEntity<GestionnaireLogistique> response = controller.getById("gest1");

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals("gest1", response.getBody().getId());
    }

    @Test
    void create_ShouldReturnCreatedGestionnaire() {
        // Arrange
        when(gestionnaireService.save(any(GestionnaireLogistique.class))).thenReturn(gestionnaire);

        // Act
        GestionnaireLogistique result = controller.create(gestionnaire);

        // Assert
        assertNotNull(result);
        assertEquals("gest1", result.getId());
        verify(gestionnaireService).save(any(GestionnaireLogistique.class));
    }

    @Test
    void update_WhenGestionnaireExists_ShouldReturnUpdatedGestionnaire() {
        // Arrange
        when(gestionnaireService.findById("gest1")).thenReturn(Optional.of(gestionnaire));
        when(gestionnaireService.save(any(GestionnaireLogistique.class))).thenReturn(gestionnaire);

        // Act
        ResponseEntity<GestionnaireLogistique> response = controller.update("gest1", gestionnaire);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals("gest1", response.getBody().getId());
    }

    @Test
    void delete_WhenGestionnaireExists_ShouldReturnNoContent() {
        // Arrange
        when(gestionnaireService.findById("gest1")).thenReturn(Optional.of(gestionnaire));
        doNothing().when(gestionnaireService).deleteById("gest1");

        // Act
        ResponseEntity<Void> response = controller.delete("gest1");

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(gestionnaireService).deleteById("gest1");
    }

    @Test
    void assignerLivreur_ShouldUpdateColisWithLivreur() {
        // Arrange
        doNothing().when(colisService).assignLivreurToColis("colis1", "livreur1");

        // Act
        ResponseEntity<Void> response = controller.assignerLivreur("colis1", "livreur1");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(colisService).assignLivreurToColis("colis1", "livreur1");
    }

    @Test
    void getStatistiques_ShouldReturnStatistics() {
        // Arrange
        Map<String, Object> stats = Map.of("total", 10, "livres", 5);
        when(colisService.getStatistiques("livreur1", "zone1")).thenReturn(stats);

        // Act
        ResponseEntity<Map<String, Object>> response = controller.getStatistiques("livreur1", "zone1");

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(10, response.getBody().get("total"));
    }

    @Test
    void groupColisBy_ShouldReturnGroupedColis() {
        // Arrange
        Map<String, Object> groups = Map.of("group1", 5, "group2", 3);
        when(colisService.groupBy("status")).thenReturn(groups);

        // Act
        ResponseEntity<Map<String, Object>> response = controller.groupColisBy("status");

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void rechercherColis_ShouldReturnFilteredColis() {
        // Arrange
        when(colisService.findByCritere("EN_STOCK", "Paris", "HAUTE"))
                .thenReturn(Arrays.asList(colisDTO));

        // Act
        ResponseEntity<List<ColisDTO>> response =
                controller.rechercherColis("EN_STOCK", "Paris", "HAUTE");

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals("colis1", response.getBody().get(0).getId());
    }

//    @Test
//    void traiterColis_ShouldUpdateColisStatus() {
//        // Arrange
//        String colisId = "colis1";
//        String newStatus = "EN_STOCK";
//        String commentaire = "En cours de livraison";
//
//        ColisDTO updatedColis = new ColisDTO();
//        updatedColis.setId(colisId);
//        updatedColis.setStatut(StatutColis.valueOf(newStatus));
//
//        when(colisService.updateStatus(colisId, newStatus, commentaire))
//                .thenReturn(updatedColis);
//
//        // Act
//        ResponseEntity<ColisDTO> response = controller.traiterColis(colisId, StatutColis.valueOf(newStatus), commentaire);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals(colisId, response.getBody().getId());
//        assertEquals(newStatus, response.getBody().getStatut().name());
//
//        verify(colisService).updateStatus(colisId, newStatus, commentaire);
//    }
}