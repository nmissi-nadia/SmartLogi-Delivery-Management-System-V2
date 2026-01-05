package com.smart.mapper;

import com.smart.dto.ClientExpediteurDTO;
import com.smart.entity.ClientExpediteur;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ClientExpediteurMapperImpl.class})
public class ClientExpediteurMapperTest {

    @Autowired
    private ClientExpediteurMapper mapper;

    @Test
    public void testToDTO() {
        ClientExpediteur entity = new ClientExpediteur();
        entity.setId("1");
        entity.setNom("Test Nom");
        entity.setPrenom("Test Prenom");
        entity.setEmail("test@example.com");
        entity.setTelephone("123456789");
        entity.setAdresse("Test Adresse");

        ClientExpediteurDTO dto = mapper.toDto(entity);

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
        ClientExpediteurDTO dto = new ClientExpediteurDTO();
        dto.setId("1");
        dto.setNom("Test Nom");
        dto.setPrenom("Test Prenom");
        dto.setEmail("test@example.com");
        dto.setTelephone("123456789");
        dto.setAdresse("Test Adresse");

        ClientExpediteur entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getNom(), entity.getNom());
        assertEquals(dto.getPrenom(), entity.getPrenom());
        assertEquals(dto.getEmail(), entity.getEmail());
        assertEquals(dto.getTelephone(), entity.getTelephone());
        assertEquals(dto.getAdresse(), entity.getAdresse());
    }
}
