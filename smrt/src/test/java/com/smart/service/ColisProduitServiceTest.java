package com.smart.service;

import com.smart.entity.ColisProduit;
import com.smart.entity.ColisProduitKey;
import com.smart.entity.Colis;
import com.smart.entity.Produit;
import com.smart.repository.ColisProduitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ColisProduitServiceTest {

    @Mock
    private ColisProduitRepository colisProduitRepository;

    @InjectMocks
    private ColisProduitService colisProduitService;

    private ColisProduit colisProduit1;
    private ColisProduit colisProduit2;
    private ColisProduitKey key1;
    private ColisProduitKey key2;

    @BeforeEach
    void setUp() {
        // Création des clés composées avec des String pour les IDs
        key1 = new ColisProduitKey();
        key1.setColisId("colis1");
        key1.setProduitId("prod1");

        key2 = new ColisProduitKey();
        key2.setColisId("colis2");
        key2.setProduitId("prod2");  

        // Création des entités de test
        colisProduit1 = new ColisProduit();
        colisProduit1.setId(key1);
        colisProduit1.setQuantite(2);

        colisProduit2 = new ColisProduit();
        colisProduit2.setId(key2);
        colisProduit2.setQuantite(3);
    }

    @Test
    void getAllColisProduits_ShouldReturnAllColisProduits() {
        // Arrange
        List<ColisProduit> expectedColisProduits = Arrays.asList(colisProduit1, colisProduit2);
        when(colisProduitRepository.findAll()).thenReturn(expectedColisProduits);

        // Act
        List<ColisProduit> result = colisProduitService.getAllColisProduits();

        // Assert
        assertThat(result)
            .isNotNull()
            .hasSize(2)
            .containsExactlyInAnyOrder(colisProduit1, colisProduit2);
        
        verify(colisProduitRepository, times(1)).findAll();
    }

    @Test
    void createColisProduit_ShouldSaveAndReturnColisProduit() {
        // Arrange
        when(colisProduitRepository.save(any(ColisProduit.class))).thenReturn(colisProduit1);

        // Act
        ColisProduit result = colisProduitService.createColisProduit(colisProduit1);

        // Assert
        assertThat(result)
            .isNotNull()
            .isEqualTo(colisProduit1);
        
        verify(colisProduitRepository, times(1)).save(colisProduit1);
    }

    @Test
    void createColisProduit_WithNullColisProduit_ShouldThrowException() {
        // Act & Assert
        assertThatThrownBy(() -> colisProduitService.createColisProduit(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("L'objet ColisProduit ne peut pas être null");
    }
}