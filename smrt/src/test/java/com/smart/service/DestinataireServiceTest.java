package com.smart.service;


import com.smart.common.src.main.java.com.smart.entity.Destinataire;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.dto.DestinataireDTO;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.mapper.DestinataireMapper;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.repository.DestinataireRepository;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.service.DestinataireService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DestinataireServiceTest {

    @Mock
    private DestinataireRepository destinataireRepository;

    @Mock
    private DestinataireMapper destinataireMapper;

    @InjectMocks
    private DestinataireService destinataireService;

    private Destinataire destinataire;
    private DestinataireDTO destinataireDTO;

    @BeforeEach
    void setUp() {
        destinataire = new Destinataire();
        destinataire.setId("1");
        destinataire.setNom("Test Nom");
        destinataire.setAdresse("Test Adresse");
        destinataire.setTelephone("123456789");

        destinataireDTO = new DestinataireDTO();
        destinataireDTO.setId("1");
        destinataireDTO.setNom("Test Nom");
        destinataireDTO.setAdresse("Test Adresse");
        destinataireDTO.setTelephone("123456789");
    }

    @Test
    void testFindAll() {
        when(destinataireRepository.findAll()).thenReturn(Collections.singletonList(destinataire));
        when(destinataireMapper.toDto(destinataire)).thenReturn(destinataireDTO);

        List<DestinataireDTO> result = destinataireService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(destinataireDTO.getNom(), result.get(0).getNom());
        verify(destinataireRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Success() {
        when(destinataireRepository.findById("1")).thenReturn(Optional.of(destinataire));
        when(destinataireMapper.toDto(destinataire)).thenReturn(destinataireDTO);

        Optional<DestinataireDTO> result = destinataireService.findById("1");

        assertTrue(result.isPresent());
        assertEquals(destinataireDTO.getNom(), result.get().getNom());
        verify(destinataireRepository, times(1)).findById("1");
    }

    @Test
    void testFindById_NotFound() {
        when(destinataireRepository.findById("1")).thenReturn(Optional.empty());

        Optional<DestinataireDTO> result = destinataireService.findById("1");

        assertFalse(result.isPresent());
        verify(destinataireRepository, times(1)).findById("1");
    }

    @Test
    void testSave() {
        when(destinataireMapper.toEntity(destinataireDTO)).thenReturn(destinataire);
        when(destinataireRepository.save(destinataire)).thenReturn(destinataire);
        when(destinataireMapper.toDto(destinataire)).thenReturn(destinataireDTO);

        DestinataireDTO result = destinataireService.save(destinataireDTO);

        assertNotNull(result);
        assertEquals(destinataireDTO.getNom(), result.getNom());
        verify(destinataireRepository, times(1)).save(destinataire);
    }

    @Test
    void testDeleteById() {
        doNothing().when(destinataireRepository).deleteById("1");
        destinataireService.deleteById("1");
        verify(destinataireRepository, times(1)).deleteById("1");
    }
}