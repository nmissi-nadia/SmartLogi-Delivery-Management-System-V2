package com.smart.mapper;

import com.smart.dto.LivreurDTO;
import com.smart.entity.Livreur;
import com.smart.entity.Zone;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LivreurMapperTest {

    private final LivreurMapper mapper = Mappers.getMapper(LivreurMapper.class);

    @Test
    public void testToDTO() {
        Livreur entity = new Livreur();
        entity.setId("1");
        entity.setNom("Test Nom");
        entity.setPrenom("Test Prenom");
        entity.setTelephone("123456789");
        entity.setVehicule("Test Vehicule");

        Zone zone = new Zone();
        zone.setId("zone1");
        entity.setZone(zone);

        LivreurDTO dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getNom(), dto.getNom());
        assertEquals(entity.getPrenom(), dto.getPrenom());
        assertEquals(entity.getTelephone(), dto.getTelephone());
        assertEquals(entity.getVehicule(), dto.getVehicule());
        assertEquals(entity.getZone().getId(), dto.getZoneId());
    }

    @Test
    public void testToEntity() {
        LivreurDTO dto = new LivreurDTO();
        dto.setId("1");
        dto.setNom("Test Nom");
        dto.setPrenom("Test Prenom");
        dto.setTelephone("123456789");
        dto.setVehicule("Test Vehicule");
        dto.setZoneId("zone1");

        Livreur entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getNom(), entity.getNom());
        assertEquals(dto.getPrenom(), entity.getPrenom());
        assertEquals(dto.getTelephone(), entity.getTelephone());
        assertEquals(dto.getVehicule(), entity.getVehicule());
        assertNotNull(entity.getZone());
        assertEquals(dto.getZoneId(), entity.getZone().getId());
    }
}
