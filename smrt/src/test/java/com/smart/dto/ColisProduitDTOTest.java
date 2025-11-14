package com.smart.dto;

import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import static org.junit.jupiter.api.Assertions.*;

public class ColisProduitDTOTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    public void testColisProduitDTOValid() {
        // Given
        ProduitDTO produitDTO = new ProduitDTO();
        produitDTO.setId("prod1");
        produitDTO.setNom("Test Product");
        produitDTO.setPrix(100.0);

        ColisProduitDTO dto = new ColisProduitDTO();
        dto.setProduit(produitDTO);
        dto.setQuantite(5);

        // When
        var violations = validator.validate(dto);

        // Then
        assertTrue(violations.isEmpty());
        assertNotNull(dto.getProduit());
        assertEquals(5, dto.getQuantite());
        assertEquals(produitDTO, dto.getProduit());
    }

    @Test
    public void testColisProduitDTONullProduit() {
        // Given
        ColisProduitDTO dto = new ColisProduitDTO();
        dto.setQuantite(5);

        // When
        var violations = validator.validate(dto);

        // Then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("ne doit pas être nul", violations.iterator().next().getMessage());
    }

    @Test
    public void testColisProduitDTONegativeQuantite() {
        // Given
        ProduitDTO produitDTO = new ProduitDTO();
        produitDTO.setId("prod1");
        produitDTO.setNom("Test Product");
        produitDTO.setPrix(100.0);

        ColisProduitDTO dto = new ColisProduitDTO();
        dto.setProduit(produitDTO);
        dto.setQuantite(-1);

        // When
        var violations = validator.validate(dto);

        // Then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("La quantité doit être positive", violations.iterator().next().getMessage());
    }

    @Test
    public void testColisProduitDTOZeroQuantite() {
        // Given
        ProduitDTO produitDTO = new ProduitDTO();
        produitDTO.setId("prod1");
        produitDTO.setNom("Test Product");
        produitDTO.setPrix(100.0);

        ColisProduitDTO dto = new ColisProduitDTO();
        dto.setProduit(produitDTO);
        dto.setQuantite(0);

        // When
        var violations = validator.validate(dto);

        // Then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("La quantité doit être positive", violations.iterator().next().getMessage());
    }

    @Test
    public void testEqualsAndHashCode() {
        ProduitDTO produit1 = new ProduitDTO();
        produit1.setId("prod1");
        ProduitDTO produit2 = new ProduitDTO();
        produit2.setId("prod2");

        ColisProduitDTO dto1 = new ColisProduitDTO();
        dto1.setProduit(produit1);
        dto1.setQuantite(10);

        ColisProduitDTO dto2 = new ColisProduitDTO();
        dto2.setProduit(produit1);
        dto2.setQuantite(10);

        ColisProduitDTO dto3 = new ColisProduitDTO();
        dto3.setProduit(produit2);
dto3.setQuantite(20);

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1.hashCode(), dto3.hashCode());
    }

    @Test
    public void testToString() {
        ProduitDTO produit = new ProduitDTO();
        produit.setId("prod1");
        produit.setNom("Test");

        ColisProduitDTO dto = new ColisProduitDTO();
        dto.setProduit(produit);
        dto.setQuantite(10);

        assertTrue(dto.toString().contains("produit="));
        assertTrue(dto.toString().contains("quantite=10"));
    }
}
