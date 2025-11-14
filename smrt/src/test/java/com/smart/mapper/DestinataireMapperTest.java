package com.smart.mapper;

import com.smart.dto.DestinataireDTO;
import com.smart.entity.Destinataire;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DestinataireMapperTest {

    private final DestinataireMapper mapper = Mappers.getMapper(DestinataireMapper.class);

    @Test
    public void testToDTO() {
        Destinataire entity = new Destinataire();
        entity.setId("1");
        entity.setNom("Test Nom");
        entity.setPrenom("Test Prenom");
        entity.setEmail("test@example.com");
        entity.setTelephone("123456789");
        entity.setAdresse("Test Adresse");

        DestinataireDTO dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getNom(), dto.getNom());
        assertEquals(entity.getPrenom(), dto.getPrenom());
        assertEquals(entity.getEmail(), dto.getEmail());
        assertEquals(entity.getTelephone(), dto.getTelephone());
        assertEquals(entity.getAdresse(), dto.getAdresse());
    }

    @Test
    public void testToEntity() {
        DestinataireDTO dto = new DestinataireDTO();
        dto.setId("1");
        dto.setNom("Test Nom");
        dto.setPrenom("Test Prenom");
        dto.setEmail("test@example.com");
        dto.setTelephone("123456789");
        dto.setAdresse("Test Adresse");

        Destinataire entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getNom(), entity.getNom());
        assertEquals(dto.getPrenom(), entity.getPrenom());
        assertEquals(dto.getEmail(), entity.getEmail());
        assertEquals(dto.getTelephone(), entity.getTelephone());
        assertEquals(dto.getAdresse(), entity.getAdresse());
        assertNull(entity.getColisRecus());
    }
}
