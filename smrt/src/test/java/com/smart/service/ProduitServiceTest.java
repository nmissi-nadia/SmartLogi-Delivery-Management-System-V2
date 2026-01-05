package com.smart.service;

import com.smart.dto.ProduitDTO;
import com.smart.entity.Produit;
import com.smart.mapper.ProduitMapper;
import com.smart.repository.ProduitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProduitServiceTest {

    @Mock
    private ProduitRepository repository;

    @Mock
    private ProduitMapper mapper;

    @InjectMocks
    private ProduitService service;

    private Produit produit;
    private ProduitDTO produitDTO;
    private final String PRODUIT_ID = "123e4567-e89b-12d3-a456-426614174000";

    @BeforeEach
    void setUp() {
        produit = new Produit();
        produit.setId(PRODUIT_ID);
        produit.setNom("Smartphone");
        produit.setCategorie("éléctronique");
        produit.setPrix(999.99);
        produit.setPoids(0.2);

        produitDTO = new ProduitDTO();
        produitDTO.setId(PRODUIT_ID);
        produitDTO.setNom("Smartphone");
        produitDTO.setCategorie("éléctronique");
        produitDTO.setPrix(999.99);
        produitDTO.setPoids(0.2);
    }

    @Test
    void findAll_ShouldReturnAllProduits() {
        when(repository.findAll()).thenReturn(List.of(produit));
        when(mapper.toDto(any(Produit.class))).thenReturn(produitDTO);

        List<ProduitDTO> result = service.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(PRODUIT_ID);
        verify(repository).findAll();
    }

    @Test
    void findById_WhenProduitExists_ShouldReturnProduit() {
        when(repository.findById(PRODUIT_ID)).thenReturn(Optional.of(produit));
        when(mapper.toDto(any(Produit.class))).thenReturn(produitDTO);

        Optional<ProduitDTO> result = service.findById(PRODUIT_ID);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(PRODUIT_ID);
        verify(repository).findById(PRODUIT_ID);
    }

    @Test
    void findById_WhenProduitNotExists_ShouldReturnEmpty() {
        when(repository.findById("unknown_id")).thenReturn(Optional.empty());

        Optional<ProduitDTO> result = service.findById("unknown_id");

        assertThat(result).isEmpty();
        verify(repository).findById("unknown_id");
    }

    @Test
    void save_ShouldSaveAndReturnProduit() {
        when(mapper.toEntity(produitDTO)).thenReturn(produit);
        when(repository.save(produit)).thenReturn(produit);
        when(mapper.toDto(produit)).thenReturn(produitDTO);

        ProduitDTO result = service.save(produitDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(PRODUIT_ID);
        verify(repository).save(produit);
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        service.deleteById(PRODUIT_ID);
        verify(repository).deleteById(PRODUIT_ID);
    }
}