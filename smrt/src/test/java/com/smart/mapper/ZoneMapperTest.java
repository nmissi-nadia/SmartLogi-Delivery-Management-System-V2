package com.smart.mapper;

import com.smart.dto.ZoneDTO;
import com.smart.entity.Zone;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ZoneMapperImpl.class})
public class ZoneMapperTest {

    @Autowired
    private ZoneMapper mapper;

    @Test
    public void testToDTO() {
        Zone entity = new Zone();
        entity.setId("1");
        entity.setNom("Test Nom");
        entity.setCodePostal("12345");

        ZoneDTO dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getNom(), dto.getNom());
        assertEquals(entity.getCodePostal(), dto.getCodePostal());
    }

    @Test
    public void testToEntity() {
        ZoneDTO dto = new ZoneDTO();
        dto.setId("1");
        dto.setNom("Test Nom");
        dto.setCodePostal("12345");

        Zone entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getNom(), entity.getNom());
        assertEquals(dto.getCodePostal(), entity.getCodePostal());
    }
}
