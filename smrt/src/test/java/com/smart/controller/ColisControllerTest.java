package com.smart.controller;

import com.smart.dto.ColisDTO;
import com.smart.dto.HistoriqueLivraisonDTO;
import com.smart.entity.Enum.StatutColis;
import com.smart.entity.Enum.PrioriteEnum;
import com.smart.service.ColisService;
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
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ColisControllerTest {

    @Mock
    private ColisService colisService;

    @InjectMocks
    private ColisController colisController;

    private ColisDTO colisDTO;
    private HistoriqueLivraisonDTO historiqueDTO;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);

        colisDTO = new ColisDTO();
        colisDTO.setId("1");
        colisDTO.setStatut(StatutColis.LIVRE);
        colisDTO.setVilleDestination("SAFI");
        colisDTO.setPriorite(PrioriteEnum.HAUTE);

        historiqueDTO = new HistoriqueLivraisonDTO();
        historiqueDTO.setId("h1");
        historiqueDTO.setStatut("EN_COURS");
        historiqueDTO.setDateChangement(LocalDateTime.now());
    }

    @Test
    void getAllColis_ShouldReturnPageOfColis() {
        // Arrange
        Page<ColisDTO> page = new PageImpl<>(List.of(colisDTO));
        when(colisService.findAll(any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(page);

        // Act
        Page<ColisDTO> result = colisController.getAll(
                "EN_COURS", "Paris", "HAUTE", "zone1",
                LocalDate.now(), LocalDate.now(), "client1",
                "dest1", "liv1", pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(colisService).findAll(any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    void updateColis_ShouldReturnUpdatedColis() {
        // Arrange
        when(colisService.update("1", colisDTO)).thenReturn(colisDTO);

        // Act
        ResponseEntity<ColisDTO> response = colisController.updateColis("1", colisDTO);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("1", response.getBody().getId());
        verify(colisService).update("1", colisDTO);
    }

    @Test
    void searchColis_ShouldReturnFilteredColis() {
        // Arrange
        Page<ColisDTO> page = new PageImpl<>(List.of(colisDTO));
        when(colisService.searchByKeyword(any(), any())).thenReturn(page);

        // Act
        ResponseEntity<Page<ColisDTO>> response = colisController.searchColis(
                "keyword", "EN_COURS", "Paris", "HAUTE", pageable);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getTotalElements());
        verify(colisService).searchByKeyword(any(), any());
    }

    @Test
    void getHistoriqueColis_ShouldReturnHistorique() {
        // Arrange
        when(colisService.getHistoriqueForColis("1")).thenReturn(List.of(historiqueDTO));

        // Act
        ResponseEntity<List<HistoriqueLivraisonDTO>> response = colisController.getHistoriqueColis("1");

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("h1", response.getBody().get(0).getId());
        verify(colisService).getHistoriqueForColis("1");
    }

    @Test
    void getAllColis_WithNullParameters_ShouldPass() {
        // Arrange
        Page<ColisDTO> page = new PageImpl<>(List.of(colisDTO));
        when(colisService.findAll(any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(page);

        // Act
        Page<ColisDTO> result = colisController.getAll(
                null, null, null, null,
                null, null, null, null, null, pageable);

        // Assert
        assertNotNull(result);
        verify(colisService).findAll(any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
    }
}