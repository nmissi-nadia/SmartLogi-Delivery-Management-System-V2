package com.smart.service;


import com.smart.common.src.main.java.com.smart.entity.ClientExpediteur;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.dto.ClientExpediteurDTO;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.mapper.ClientExpediteurMapper;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.repository.ClientExpediteurRepository;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.service.ClientExpediteurService;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientExpediteurServiceTest {

    @Mock
    private ClientExpediteurRepository clientExpediteurRepository;

    @Mock
    private ClientExpediteurMapper clientExpediteurMapper;

    @InjectMocks
    private ClientExpediteurService clientExpediteurService;

    private ClientExpediteur clientExpediteur;
    private ClientExpediteurDTO clientExpediteurDTO;
    private final String CLIENT_ID = "123e4567-e89b-12d3-a456-426614174000";

    @BeforeEach
    void setUp() {
        clientExpediteur = new ClientExpediteur();
        clientExpediteur.setId(CLIENT_ID);
        clientExpediteur.setNom("Dupont");
        clientExpediteur.setEmail("dupont@example.com");

        clientExpediteurDTO = new ClientExpediteurDTO();
        clientExpediteurDTO.setId(CLIENT_ID);
        clientExpediteurDTO.setNom("Dupont");
        clientExpediteurDTO.setEmail("dupont@example.com");
    }

    @Test
    void findAll_ShouldReturnAllClients() {
        // Arrange
        List<ClientExpediteur> clients = Arrays.asList(clientExpediteur);
        when(clientExpediteurRepository.findAll()).thenReturn(clients);
        when(clientExpediteurMapper.toDto(any(ClientExpediteur.class))).thenReturn(clientExpediteurDTO);

        // Act
        List<ClientExpediteurDTO> result = clientExpediteurService.findAll();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(CLIENT_ID);
        verify(clientExpediteurRepository).findAll();
        verify(clientExpediteurMapper).toDto(clientExpediteur);
    }

    @Test
    void findById_WhenClientExists_ShouldReturnClient() {
        // Arrange
        when(clientExpediteurRepository.findById(CLIENT_ID)).thenReturn(Optional.of(clientExpediteur));
        when(clientExpediteurMapper.toDto(any(ClientExpediteur.class))).thenReturn(clientExpediteurDTO);

        // Act
        Optional<ClientExpediteurDTO> result = clientExpediteurService.findById(CLIENT_ID);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(CLIENT_ID);
        verify(clientExpediteurRepository).findById(CLIENT_ID);
    }

    @Test
    void findById_WhenClientNotExists_ShouldReturnEmpty() {
        // Arrange
        when(clientExpediteurRepository.findById(CLIENT_ID)).thenReturn(Optional.empty());

        // Act
        Optional<ClientExpediteurDTO> result = clientExpediteurService.findById(CLIENT_ID);

        // Assert
        assertThat(result).isEmpty();
        verify(clientExpediteurRepository).findById(CLIENT_ID);
    }

    @Test
    void save_ShouldSaveAndReturnClient() {
        // Arrange
        when(clientExpediteurMapper.toEntity(clientExpediteurDTO)).thenReturn(clientExpediteur);
        when(clientExpediteurRepository.save(clientExpediteur)).thenReturn(clientExpediteur);
        when(clientExpediteurMapper.toDto(clientExpediteur)).thenReturn(clientExpediteurDTO);

        // Act
        ClientExpediteurDTO result = clientExpediteurService.save(clientExpediteurDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(CLIENT_ID);
        verify(clientExpediteurMapper).toEntity(clientExpediteurDTO);
        verify(clientExpediteurRepository).save(clientExpediteur);
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        // Act
        clientExpediteurService.deleteById(CLIENT_ID);

        // Assert
        verify(clientExpediteurRepository).deleteById(CLIENT_ID);
    }

    @Test
    void searchByKeyword_ShouldReturnMatchingClients() {
        // Arrange
        String keyword = "Dupont";
        Pageable pageable = PageRequest.of(0, 10);
        Page<ClientExpediteur> page = new PageImpl<>(Arrays.asList(clientExpediteur));

        when(clientExpediteurRepository.searchByKeyword(keyword, pageable)).thenReturn(page);
        when(clientExpediteurMapper.toDto(any(ClientExpediteur.class))).thenReturn(clientExpediteurDTO);

        // Act
        Page<ClientExpediteurDTO> result = clientExpediteurService.searchByKeyword(keyword, pageable);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0).getNom()).isEqualTo("Dupont");
        verify(clientExpediteurRepository).searchByKeyword(keyword, pageable);
    }
}