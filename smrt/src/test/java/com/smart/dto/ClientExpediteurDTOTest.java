package com.smart.dto;

import com.smart.utilisateur.src.main.java.com.smart.utilisateur.dto.ClientExpediteurDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ClientExpediteurDTOTest {

    @Test
    public void testClientExpediteurDTO() {
        ClientExpediteurDTO dto = new ClientExpediteurDTO();
        dto.setId("1");
        dto.setNom("Test Nom");
        dto.setPrenom("Test Prenom");
        dto.setEmail("test@example.com");
        dto.setTelephone("123456789");
        dto.setAdresse("Test Adresse");

        assertEquals("1", dto.getId());
        assertEquals("Test Nom", dto.getNom());
        assertEquals("Test Prenom", dto.getPrenom());
        assertEquals("test@example.com", dto.getEmail());
        assertEquals("123456789", dto.getTelephone());
        assertEquals("Test Adresse", dto.getAdresse());

        ClientExpediteurDTO dto2 = new ClientExpediteurDTO();
        dto2.setId("1");
        dto2.setNom("Test Nom");
        dto2.setPrenom("Test Prenom");
        dto2.setEmail("test@example.com");
        dto2.setTelephone("123456789");
        dto2.setAdresse("Test Adresse");

        assertEquals(dto, dto2);
        assertEquals(dto.hashCode(), dto2.hashCode());
        assertNotNull(dto.toString());

        ClientExpediteurDTO dto3 = new ClientExpediteurDTO();
        dto3.setId("2");

        assertNotEquals(dto, dto3);
        assertNotEquals(dto.hashCode(), dto3.hashCode());
    }
}
