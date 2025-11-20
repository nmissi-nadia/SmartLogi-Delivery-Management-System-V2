package com.smart.controller;


import com.smart.zone.src.main.java.com.smart.zone.controller.ZoneController;
import com.smart.zone.src.main.java.com.smart.zone.dto.ZoneDTO;
import com.smart.zone.src.main.java.com.smart.zone.service.ZoneService;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ZoneControllerTest {

    @Mock
    private ZoneService zoneService;

    @InjectMocks
    private ZoneController zoneController;

    private ZoneDTO zoneDTO;

    @BeforeEach
    void setUp() {
        zoneDTO = new ZoneDTO();
        zoneDTO.setId("zone1");
        zoneDTO.setNom("Zone Nord");
        zoneDTO.setCodePostal("46666");
    }

    @Test
    void getAll_ShouldReturnAllZones() {
        // Arrange
        when(zoneService.findAll()).thenReturn(Arrays.asList(zoneDTO));

        // Act
        List<ZoneDTO> result = zoneController.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("zone1", result.get(0).getId());
        verify(zoneService).findAll();
    }

    @Test
    void getById_WhenZoneExists_ShouldReturnZone() {
        // Arrange
        when(zoneService.findById("zone1")).thenReturn(Optional.of(zoneDTO));

        // Act
        ResponseEntity<ZoneDTO> response = zoneController.getById("zone1");

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals("zone1", response.getBody().getId());
        verify(zoneService).findById("zone1");
    }

    @Test
    void getById_WhenZoneNotExists_ShouldReturnNotFound() {
        // Arrange
        when(zoneService.findById("nonexistent")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ZoneDTO> response = zoneController.getById("nonexistent");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(zoneService).findById("nonexistent");
    }

    @Test
    void create_ShouldReturnCreatedZone() {
        // Arrange
        when(zoneService.save(any(ZoneDTO.class))).thenReturn(zoneDTO);

        // Act
        ZoneDTO result = zoneController.create(zoneDTO);

        // Assert
        assertNotNull(result);
        assertEquals("zone1", result.getId());
        verify(zoneService).save(any(ZoneDTO.class));
    }

    @Test
    void update_WhenZoneExists_ShouldReturnUpdatedZone() {
        // Arrange
        ZoneDTO updatedZone = new ZoneDTO();
        updatedZone.setId("zone1");
        updatedZone.setNom("Zone Nord Mise à jour");

        when(zoneService.findById("zone1")).thenReturn(Optional.of(zoneDTO));
        when(zoneService.save(any(ZoneDTO.class))).thenReturn(updatedZone);

        // Act
        ResponseEntity<ZoneDTO> response = zoneController.update("zone1", updatedZone);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals("Zone Nord Mise à jour", response.getBody().getNom());
        verify(zoneService).findById("zone1");
        verify(zoneService).save(any(ZoneDTO.class));
    }

    @Test
    void update_WhenZoneNotExists_ShouldReturnNotFound() {
        // Arrange
        when(zoneService.findById("nonexistent")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ZoneDTO> response = zoneController.update("nonexistent", zoneDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(zoneService).findById("nonexistent");
        verify(zoneService, never()).save(any(ZoneDTO.class));
    }

    @Test
    void delete_WhenZoneExists_ShouldReturnNoContent() {
        // Arrange
        when(zoneService.findById("zone1")).thenReturn(Optional.of(zoneDTO));
        doNothing().when(zoneService).deleteById("zone1");

        // Act
        ResponseEntity<Void> response = zoneController.delete("zone1");

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(zoneService).findById("zone1");
        verify(zoneService).deleteById("zone1");
    }

    @Test
    void delete_WhenZoneNotExists_ShouldReturnNotFound() {
        // Arrange
        when(zoneService.findById("nonexistent")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Void> response = zoneController.delete("nonexistent");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(zoneService).findById("nonexistent");
        verify(zoneService, never()).deleteById(anyString());
    }
}