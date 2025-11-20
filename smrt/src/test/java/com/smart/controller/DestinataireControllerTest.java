package com.smart.controller;


import com.smart.colis.src.main.java.com.smart.colis.dto.ColisDTO;
import com.smart.colis.src.main.java.com.smart.colis.service.ColisService;
import com.smart.common.src.main.java.com.smart.Enum.StatutColis;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.controller.DestinataireController;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.dto.DestinataireDTO;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.service.DestinataireService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DestinataireControllerTest {

    @Mock
    private DestinataireService destinataireService;

    @Mock
    private ColisService colisService;

    @InjectMocks
    private DestinataireController destinataireController;

    private DestinataireDTO destinataireDTO;
    private ColisDTO colisDTO;

    @BeforeEach
    void setUp() {
        destinataireDTO = new DestinataireDTO();
        destinataireDTO.setId("dest1");
        destinataireDTO.setNom("Dupont");
        destinataireDTO.setEmail("destinataire@example.com");

        colisDTO = new ColisDTO();
        colisDTO.setId("colis1");
        colisDTO.setStatut(StatutColis.EN_STOCK);
    }

    @Test
    void getAll_ShouldReturnAllDestinataires() {
        // Arrange
        when(destinataireService.findAll()).thenReturn(Arrays.asList(destinataireDTO));

        // Act
        List<DestinataireDTO> result = destinataireController.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("dest1", result.get(0).getId());
    }

    @Test
    void getById_WhenDestinataireExists_ShouldReturnDestinataire() {
        // Arrange
        when(destinataireService.findById("dest1")).thenReturn(Optional.of(destinataireDTO));

        // Act
        ResponseEntity<DestinataireDTO> response = destinataireController.getById("dest1");

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals("dest1", response.getBody().getId());
    }

    @Test
    void getById_WhenDestinataireNotExists_ShouldReturnNotFound() {
        // Arrange
        when(destinataireService.findById("nonexistent")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<DestinataireDTO> response = destinataireController.getById("nonexistent");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void create_ShouldReturnCreatedDestinataire() {
        // Arrange
        when(destinataireService.save(any(DestinataireDTO.class))).thenReturn(destinataireDTO);

        // Act
        DestinataireDTO result = destinataireController.create(destinataireDTO);

        // Assert
        assertNotNull(result);
        assertEquals("dest1", result.getId());
        verify(destinataireService).save(any(DestinataireDTO.class));
    }

    @Test
    void update_WhenDestinataireExists_ShouldReturnUpdatedDestinataire() {
        // Arrange
        when(destinataireService.findById("dest1")).thenReturn(Optional.of(destinataireDTO));
        when(destinataireService.save(any(DestinataireDTO.class))).thenReturn(destinataireDTO);

        // Act
        ResponseEntity<DestinataireDTO> response = destinataireController.update("dest1", destinataireDTO);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals("dest1", response.getBody().getId());
    }

    @Test
    void delete_WhenDestinataireExists_ShouldReturnNoContent() {
        // Arrange
        when(destinataireService.findById("dest1")).thenReturn(Optional.of(destinataireDTO));
        doNothing().when(destinataireService).deleteById("dest1");

        // Act
        ResponseEntity<Void> response = destinataireController.delete("dest1");

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(destinataireService).deleteById("dest1");
    }

    @Test
    void viewColis_WhenColisExists_ShouldReturnColis() {
        // Arrange
        when(colisService.findById("colis1")).thenReturn(Optional.of(colisDTO));

        // Act
        ResponseEntity<ColisDTO> response = destinataireController.viewColis("dest1", "colis1");

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals("colis1", response.getBody().getId());
    }

    @Test
    void viewColis_WhenColisNotExists_ShouldThrowException() {
        // Arrange
        when(colisService.findById("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            destinataireController.viewColis("dest1", "nonexistent");
        });
    }

    @Test
    void confirmReception_ShouldUpdateColisStatus() {
        // Arrange
        ColisDTO updatedColis = new ColisDTO();
        updatedColis.setId("colis1");
        updatedColis.setStatut(StatutColis.CREE);

        when(colisService.updateStatus("colis1", "LIVRE", "Livré avec succès")).thenReturn(updatedColis);

        // Act
        ResponseEntity<ColisDTO> response = destinataireController.confirmReception("dest1", "colis1");

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(StatutColis.CREE, response.getBody().getStatut());
        verify(colisService).updateStatus("colis1", "LIVRE", "Livré avec succès");
    }
}