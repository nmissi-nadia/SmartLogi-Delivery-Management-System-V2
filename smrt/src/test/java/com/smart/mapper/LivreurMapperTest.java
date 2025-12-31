package com.smart.mapper;

import com.smart.dto.LivreurDTO;
import com.smart.entity.Livreur;
import com.smart.entity.Zone;
import com.smart.entity.User;
import com.smart.repository.UserRepository;
import com.smart.service.ZoneService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {LivreurMapperImpl.class})
public class LivreurMapperTest {

    @Autowired
    private LivreurMapper mapper;

    @MockBean
    private ZoneService zoneService;

    @MockBean
    private UserRepository userRepository;

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
        dto.setUserId("user1");

        Zone zone = new Zone();
        zone.setId("zone1");
        User user = new User();
        user.setId("user1");
        when(zoneService.findEntityById("zone1")).thenReturn(Optional.of(zone));
        when(userRepository.findById("user1")).thenReturn(Optional.of(user));

        Livreur entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getNom(), entity.getNom());
        assertEquals(dto.getPrenom(), entity.getPrenom());
        assertEquals(dto.getTelephone(), entity.getTelephone());
        assertEquals(dto.getVehicule(), entity.getVehicule());
        assertNotNull(entity.getZone());
        assertEquals(dto.getZoneId(), entity.getZone().getId());
        assertNotNull(entity.getUser());
        assertEquals(dto.getUserId(), entity.getUser().getId());
    }
}
