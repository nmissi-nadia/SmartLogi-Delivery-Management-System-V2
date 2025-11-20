package com.smart.controller;


import com.smart.common.src.main.java.com.smart.Enum.StatutColis;
import com.smart.livraison.src.main.java.com.smart.livraison.controller.HistoriqueLivraisonController;
import com.smart.livraison.src.main.java.com.smart.livraison.dto.HistoriqueLivraisonDTO;
import com.smart.livraison.src.main.java.com.smart.livraison.service.HistoriqueLivraisonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistoriqueLivraisonControllerTest {

    @Mock
    private HistoriqueLivraisonService historiqueService;

    @InjectMocks
    private HistoriqueLivraisonController historiqueController;

    private HistoriqueLivraisonDTO historiqueDTO;

    @BeforeEach
    void setUp() {
        historiqueDTO = new HistoriqueLivraisonDTO();
        historiqueDTO.setId("hist1");
        historiqueDTO.setStatut(StatutColis.EN_STOCK.toString());
        historiqueDTO.setDateChangement(LocalDateTime.now());
        historiqueDTO.setCommentaire("En cours de livraison");
    }

    @Test
    void getAll_ShouldReturnAllHistoriques() {
        // Arrange
        when(historiqueService.findAll()).thenReturn(Arrays.asList(historiqueDTO));

        // Act
        List<HistoriqueLivraisonDTO> result = historiqueController.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("hist1", result.get(0).getId());
        verify(historiqueService).findAll();
    }

    @Test
    void getById_WhenHistoriqueExists_ShouldReturnHistorique() {
        // Arrange
        when(historiqueService.findById("hist1")).thenReturn(Optional.of(historiqueDTO));

        // Act
        ResponseEntity<HistoriqueLivraisonDTO> response = historiqueController.getById("hist1");

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals("hist1", response.getBody().getId());
        verify(historiqueService).findById("hist1");
    }

    @Test
    void getById_WhenHistoriqueNotExists_ShouldReturnNotFound() {
        // Arrange
        when(historiqueService.findById("nonexistent")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<HistoriqueLivraisonDTO> response = historiqueController.getById("nonexistent");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(historiqueService).findById("nonexistent");
    }

    @Test
    void create_ShouldReturnCreatedHistorique() {
        // Arrange
        when(historiqueService.save(any(HistoriqueLivraisonDTO.class))).thenReturn(historiqueDTO);

        // Act
        HistoriqueLivraisonDTO result = historiqueController.create(historiqueDTO);

        // Assert
        assertNotNull(result);
        assertEquals("hist1", result.getId());
        verify(historiqueService).save(any(HistoriqueLivraisonDTO.class));
    }

    @Test
    void update_WhenHistoriqueExists_ShouldReturnUpdatedHistorique() {
        // Arrange
        when(historiqueService.findById("hist1")).thenReturn(Optional.of(historiqueDTO));
        when(historiqueService.save(any(HistoriqueLivraisonDTO.class))).thenReturn(historiqueDTO);

        // Act
        ResponseEntity<HistoriqueLivraisonDTO> response =
                historiqueController.update("hist1", historiqueDTO);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals("hist1", response.getBody().getId());
        verify(historiqueService).findById("hist1");
        verify(historiqueService).save(any(HistoriqueLivraisonDTO.class));
    }

    @Test
    void update_WhenHistoriqueNotExists_ShouldReturnNotFound() {
        // Arrange
        when(historiqueService.findById("nonexistent")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<HistoriqueLivraisonDTO> response =
                historiqueController.update("nonexistent", historiqueDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(historiqueService).findById("nonexistent");
        verify(historiqueService, never()).save(any(HistoriqueLivraisonDTO.class));
    }

    @Test
    void delete_WhenHistoriqueExists_ShouldReturnNoContent() {
        // Arrange
        when(historiqueService.findById("hist1")).thenReturn(Optional.of(historiqueDTO));
        doNothing().when(historiqueService).deleteById("hist1");

        // Act
        ResponseEntity<Void> response = historiqueController.delete("hist1");

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(historiqueService).findById("hist1");
        verify(historiqueService).deleteById("hist1");
    }

    @Test
    void delete_WhenHistoriqueNotExists_ShouldReturnNotFound() {
        // Arrange
        when(historiqueService.findById("nonexistent")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Void> response = historiqueController.delete("nonexistent");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(historiqueService).findById("nonexistent");
        verify(historiqueService, never()).deleteById(anyString());
    }
}