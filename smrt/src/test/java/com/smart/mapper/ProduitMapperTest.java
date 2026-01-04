package com.smart.mapper;

import com.smart.dto.ProduitDTO;
import com.smart.entity.Produit;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProduitMapperTest {

    private final ProduitMapper mapper = Mappers.getMapper(ProduitMapper.class);

    @Test
    public void testToDTO() {
        Produit entity = new Produit();
        entity.setId("1");
        entity.setNom("Test Nom");
        entity.setCategorie("Test Categorie");
        entity.setPoids(BigDecimal.valueOf(2.5));
        entity.setPrix(BigDecimal.valueOf(10.0));

        ProduitDTO dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getNom(), dto.getNom());
        assertEquals(entity.getCategorie(), dto.getCategorie());
        assertEquals(entity.getPoids(), dto.getPoids());
        assertEquals(entity.getPrix(), dto.getPrix());
    }

    @Test
    public void testToEntity() {
        ProduitDTO dto = new ProduitDTO();
        dto.setId("1");
        dto.setNom("Test Nom");
        dto.setCategorie("Test Categorie");
        dto.setPoids(BigDecimal.valueOf(2.5));
        dto.setPrix(10.0);

        Produit entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getNom(), entity.getNom());
        assertEquals(dto.getCategorie(), entity.getCategorie());
        assertEquals(dto.getPoids(), entity.getPoids());
        assertEquals(dto.getPrix(), entity.getPrix());
    }
}
