package com.smart.service;

import com.smart.entity.GestionnaireLogistique;
import com.smart.repository.GestionnaireLogistiqueRepository;
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
class GestionnaireLogistiqueServiceTest {

    @Mock
    private GestionnaireLogistiqueRepository repository;

    @InjectMocks
    private GestionnaireLogistiqueService service;

    private GestionnaireLogistique gestionnaire;

    @BeforeEach
    void setUp() {
        gestionnaire = new GestionnaireLogistique();
        gestionnaire.setId("1");
        gestionnaire.setNom("Test");
        
    }

    @Test
    void testFindAll() {
        when(repository.findAll()).thenReturn(Collections.singletonList(gestionnaire));
        List<GestionnaireLogistique> result = service.findAll();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testFindById_Success() {
        when(repository.findById("1")).thenReturn(Optional.of(gestionnaire));
        Optional<GestionnaireLogistique> result = service.findById("1");
        assertTrue(result.isPresent());
        assertEquals(gestionnaire, result.get());
        verify(repository, times(1)).findById("1");
    }

    @Test
    void testFindById_NotFound() {
        when(repository.findById("2")).thenReturn(Optional.empty());
        Optional<GestionnaireLogistique> result = service.findById("2");
        assertFalse(result.isPresent());
        verify(repository, times(1)).findById("2");
    }

    @Test
    void testSave() {
        when(repository.save(any(GestionnaireLogistique.class))).thenReturn(gestionnaire);
        GestionnaireLogistique result = service.save(new GestionnaireLogistique());
        assertNotNull(result);
        assertEquals("1", result.getId());
        verify(repository, times(1)).save(any(GestionnaireLogistique.class));
    }

    @Test
    void testDeleteById() {
        doNothing().when(repository).deleteById("1");
        service.deleteById("1");
        verify(repository, times(1)).deleteById("1");
    }
}
