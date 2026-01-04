package com.smart.controller;

import com.smart.dto.ProduitDTO;
import com.smart.service.ProduitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProduitControllerTest {

    @Mock
    private ProduitService produitService;

    @InjectMocks
    private ProduitController produitController;

    private ProduitDTO produitDTO;

    @BeforeEach
    void setUp() {
        produitDTO = new ProduitDTO();
        produitDTO.setId("prod1");
        produitDTO.setNom("Téléphone");
        produitDTO.setCategorie("Electronics");
        produitDTO.setPrix(999.99);
        produitDTO.setPoids(BigDecimal.valueOf(0.2));
    }

    @Test
    void getAll_ShouldReturnAllProduits() {
        // Arrange
        when(produitService.findAll()).thenReturn(Arrays.asList(produitDTO));

        // Act
        List<ProduitDTO> result = produitController.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("prod1", result.get(0).getId());
        verify(produitService).findAll();
    }

    @Test
    void getById_WhenProduitExists_ShouldReturnProduit() {
        // Arrange
        when(produitService.findById("prod1")).thenReturn(Optional.of(produitDTO));

        // Act
        ResponseEntity<ProduitDTO> response = produitController.getById("prod1");

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals("prod1", response.getBody().getId());
        verify(produitService).findById("prod1");
    }

    @Test
    void getById_WhenProduitNotExists_ShouldReturnNotFound() {
        // Arrange
        when(produitService.findById("nonexistent")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ProduitDTO> response = produitController.getById("nonexistent");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(produitService).findById("nonexistent");
    }

    @Test
    void create_ShouldReturnCreatedProduit() {
        // Arrange
        when(produitService.save(any(ProduitDTO.class))).thenReturn(produitDTO);

        // Act
        ProduitDTO result = produitController.create(produitDTO);

        // Assert
        assertNotNull(result);
        assertEquals("prod1", result.getId());
        verify(produitService).save(any(ProduitDTO.class));
    }

    @Test
    void update_WhenProduitExists_ShouldReturnUpdatedProduit() {
        // Arrange
        when(produitService.findById("prod1")).thenReturn(Optional.of(produitDTO));
        when(produitService.save(any(ProduitDTO.class))).thenReturn(produitDTO);

        // Act
        ResponseEntity<ProduitDTO> response = produitController.update("prod1", produitDTO);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals("prod1", response.getBody().getId());
        verify(produitService).findById("prod1");
        verify(produitService).save(any(ProduitDTO.class));
    }

    @Test
    void update_WhenProduitNotExists_ShouldReturnNotFound() {
        // Arrange
        when(produitService.findById("nonexistent")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ProduitDTO> response = produitController.update("nonexistent", produitDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(produitService).findById("nonexistent");
        verify(produitService, never()).save(any(ProduitDTO.class));
    }

    @Test
    void delete_WhenProduitExists_ShouldReturnNoContent() {
        // Arrange
        when(produitService.findById("prod1")).thenReturn(Optional.of(produitDTO));
        doNothing().when(produitService).deleteById("prod1");

        // Act
        ResponseEntity<Void> response = produitController.delete("prod1");

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(produitService).findById("prod1");
        verify(produitService).deleteById("prod1");
    }

    @Test
    void delete_WhenProduitNotExists_ShouldReturnNotFound() {
        // Arrange
        when(produitService.findById("nonexistent")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Void> response = produitController.delete("nonexistent");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(produitService).findById("nonexistent");
        verify(produitService, never()).deleteById(anyString());
    }
}