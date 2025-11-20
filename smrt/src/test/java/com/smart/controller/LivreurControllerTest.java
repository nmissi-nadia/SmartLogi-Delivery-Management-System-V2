package com.smart.controller;


import com.smart.colis.src.main.java.com.smart.colis.dto.ColisDTO;
import com.smart.colis.src.main.java.com.smart.colis.service.ColisService;
import com.smart.common.src.main.java.com.smart.Enum.StatutColis;
import com.smart.livraison.src.main.java.com.smart.livraison.controller.LivreurController;

import com.smart.livraison.src.main.java.com.smart.livraison.dto.LivreurDTO;
import com.smart.livraison.src.main.java.com.smart.livraison.service.LivreurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
class LivreurControllerTest {

    @Mock
    private LivreurService livreurService;

    @Mock
    private ColisService colisService;

    @InjectMocks
    private LivreurController livreurController;

    private LivreurDTO livreurDTO;
    private ColisDTO colisDTO;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);

        livreurDTO = new LivreurDTO();
        livreurDTO.setId("liv1");
        livreurDTO.setNom("Martin");
        livreurDTO.setTelephone("000000000");
        livreurDTO.setVehicule("car");

        colisDTO = new ColisDTO();
        colisDTO.setId("colis1");
        colisDTO.setStatut(StatutColis.LIVRE);
    }

    @Test
    void getAll_ShouldReturnAllLivreurs() {
        // Arrange
        when(livreurService.findAll()).thenReturn(Arrays.asList(livreurDTO));

        // Act
        List<LivreurDTO> result = livreurController.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("liv1", result.get(0).getId());
    }

    @Test
    void searchByKeyword_ShouldReturnMatchingLivreurs() {
        // Arrange
        Page<LivreurDTO> page = new PageImpl<>(Arrays.asList(livreurDTO));
        when(livreurService.searchByKeyword("Martin", pageable)).thenReturn(page);

        // Act
        Page<LivreurDTO> result = livreurController.searchByKeyword("Martin", pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Martin", result.getContent().get(0).getNom());
    }

    @Test
    void getById_WhenLivreurExists_ShouldReturnLivreur() {
        // Arrange
        when(livreurService.findById("liv1")).thenReturn(Optional.of(livreurDTO));

        // Act
        ResponseEntity<LivreurDTO> response = livreurController.getById("liv1");

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals("liv1", response.getBody().getId());
    }

    @Test
    void create_ShouldReturnCreatedLivreur() {
        // Arrange
        when(livreurService.save(any(LivreurDTO.class))).thenReturn(livreurDTO);

        // Act
        LivreurDTO result = livreurController.create(livreurDTO);

        // Assert
        assertNotNull(result);
        assertEquals("liv1", result.getId());
        verify(livreurService).save(any(LivreurDTO.class));
    }

    @Test
    void update_WhenLivreurExists_ShouldReturnUpdatedLivreur() {
        // Arrange
        when(livreurService.findById("liv1")).thenReturn(Optional.of(livreurDTO));
        when(livreurService.save(any(LivreurDTO.class))).thenReturn(livreurDTO);

        // Act
        ResponseEntity<LivreurDTO> response = livreurController.update("liv1", livreurDTO);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals("liv1", response.getBody().getId());
    }

    @Test
    void delete_WhenLivreurExists_ShouldReturnNoContent() {
        // Arrange
        when(livreurService.findById("liv1")).thenReturn(Optional.of(livreurDTO));
        doNothing().when(livreurService).deleteById("liv1");

        // Act
        ResponseEntity<Void> response = livreurController.delete("liv1");

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(livreurService).deleteById("liv1");
    }

    @Test
    void updateColisStatus_ShouldUpdateStatus() {
        // Arrange
        when(colisService.updateStatus("colis1", "LIVRE", null)).thenReturn(colisDTO);

        // Act
        ResponseEntity<ColisDTO> response =
                livreurController.updateColisStatus("liv1", "colis1", "LIVRE");

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals("colis1", response.getBody().getId());
    }

    @Test
    void getColisAssignes_ShouldReturnAssignedColis() {
        // Arrange
        Page<ColisDTO> page = new PageImpl<>(Arrays.asList(colisDTO));
        when(colisService.findByLivreurIdAndStatut("liv1", "EN_LIVRAISON", pageable))
                .thenReturn(page);

        // Act
        ResponseEntity<Page<ColisDTO>> response =
                livreurController.getColisAssignes("liv1", "EN_LIVRAISON", pageable);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void getStatistiquesLivreur_WhenLivreurExists_ShouldReturnStats() {
        // Arrange
        when(livreurService.findById("liv1")).thenReturn(Optional.of(livreurDTO));
        Map<String, Object> stats = Map.of("total", 5, "livres", 3);
        when(colisService.getStatistiques("liv1", "zone1")).thenReturn(stats);

        // Act
        ResponseEntity<Map<String, Object>> response =
                livreurController.getStatistiquesLivreur("liv1", "zone1");

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(5, response.getBody().get("total"));
    }

    @Test
    void getStatistiquesLivreur_WhenLivreurNotExists_ShouldReturnNotFound() {
        // Arrange
        when(livreurService.findById("nonexistent")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Map<String, Object>> response =
                livreurController.getStatistiquesLivreur("nonexistent", "zone1");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}