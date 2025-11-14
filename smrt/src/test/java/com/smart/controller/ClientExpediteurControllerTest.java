package com.smart.controller;

import com.smart.dto.ClientExpediteurDTO;
import com.smart.dto.ColisDTO;
import com.smart.dto.ColisRequestDTO;
import com.smart.entity.Enum.StatutColis;
import com.smart.service.ClientExpediteurService;
import com.smart.service.ColisService;
import com.smart.repository.ClientExpediteurRepository;
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

import jakarta.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientExpediteurControllerTest {

    @Mock
    private ClientExpediteurService service;

    @Mock
    private ColisService colisService;

    @Mock
    private ClientExpediteurRepository clientExpediteurRepository;

    @InjectMocks
    private ClientExpediteurController controller;

    private ClientExpediteurDTO clientDTO;
    private ColisDTO colisDTO;
    private ColisRequestDTO colisRequestDTO;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
        
        clientDTO = new ClientExpediteurDTO();
        clientDTO.setId("client1");
        clientDTO.setNom("Dupont");
        clientDTO.setEmail("client@example.com");
        
        colisDTO = new ColisDTO();
        colisDTO.setId("colis1");
        colisDTO.setStatut(StatutColis.CREE);
        
        colisRequestDTO = new ColisRequestDTO();
        // Initialiser les champs nécessaires
    }

    @Test
    void getAll_ShouldReturnAllClients() {
        // Arrange
        when(service.findAll()).thenReturn(Arrays.asList(clientDTO));

        // Act
        List<ClientExpediteurDTO> result = controller.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("client1", result.get(0).getId());
    }

    @Test
    void getById_WhenClientExists_ShouldReturnClient() {
        // Arrange
        when(service.findById("client1")).thenReturn(Optional.of(clientDTO));

        // Act
        ResponseEntity<ClientExpediteurDTO> response = controller.getById("client1");

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals("client1", response.getBody().getId());
    }

    @Test
    void getById_WhenClientNotExists_ShouldReturnNotFound() {
        // Arrange
        when(service.findById("nonexistent")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ClientExpediteurDTO> response = controller.getById("nonexistent");

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void create_ShouldReturnCreatedClient() {
        // Arrange
        when(service.save(any(ClientExpediteurDTO.class))).thenReturn(clientDTO);

        // Act
        ClientExpediteurDTO result = controller.create(clientDTO);

        // Assert
        assertNotNull(result);
        assertEquals("client1", result.getId());
        verify(service).save(any(ClientExpediteurDTO.class));
    }

    @Test
    void update_WhenClientExists_ShouldReturnUpdatedClient() {
        // Arrange
        when(service.findById("client1")).thenReturn(Optional.of(clientDTO));
        when(service.save(any(ClientExpediteurDTO.class))).thenReturn(clientDTO);

        // Act
        ResponseEntity<ClientExpediteurDTO> response = controller.update("client1", clientDTO);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals("client1", response.getBody().getId());
    }

    @Test
    void delete_WhenClientExists_ShouldReturnNoContent() {
        // Arrange
        when(service.findById("client1")).thenReturn(Optional.of(clientDTO));
        doNothing().when(service).deleteById("client1");

        // Act
        ResponseEntity<Void> response = controller.delete("client1");

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service).deleteById("client1");
    }

    @Test
    void searchByKeyword_ShouldReturnMatchingClients() {
        // Arrange
        Page<ClientExpediteurDTO> page = new PageImpl<>(Arrays.asList(clientDTO));
        when(service.searchByKeyword("Dupont", pageable)).thenReturn(page);

        // Act
        Page<ClientExpediteurDTO> result = controller.searchByKeyword("Dupont", pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Dupont", result.getContent().get(0).getNom());
    }

    @Test
    void createColis_WhenClientExists_ShouldCreateColis() {
        // Arrange
        when(clientExpediteurRepository.existsById("client1")).thenReturn(true);
        when(colisService.createColisWithDetails("client1", colisRequestDTO)).thenReturn(colisDTO);

        // Act
        ResponseEntity<ColisDTO> response = controller.createColis("client1", colisRequestDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("colis1", response.getBody().getId());
    }

    @Test
    void getColisByClient_ShouldReturnClientColis() {
        // Arrange
        Page<ColisDTO> page = new PageImpl<>(Arrays.asList(colisDTO));
        when(colisService.findColisByClientExpediteur("client1", pageable)).thenReturn(page);

        // Act
        ResponseEntity<Page<ColisDTO>> response = controller.getColisByClient("client1", pageable);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void trackColis_WhenColisExists_ShouldReturnColis() {
        // Arrange
        when(colisService.findById("colis1")).thenReturn(Optional.of(colisDTO));

        // Act
        ResponseEntity<ColisDTO> response = controller.trackColis("client1", "colis1");

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals("colis1", response.getBody().getId());
    }

    @Test
    void getColisByClientAndStatus_ShouldReturnFilteredColis() {
        // Arrange
        Page<ColisDTO> page = new PageImpl<>(Arrays.asList(colisDTO));
        when(colisService.findColisByClientExpediteurAndStatut("client1", "CREE", pageable)).thenReturn(page);

        // Act
        ResponseEntity<Page<ColisDTO>> response = controller.getColisByClientAndStatus("client1", "CREE", pageable);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals(StatutColis.CREE, response.getBody().getContent().get(0).getStatut());
    }
}