package com.smart.mapper;

import com.smart.dto.ColisDTO;
import com.smart.entity.ClientExpediteur;
import com.smart.entity.Colis;
import com.smart.entity.Destinataire;
import com.smart.entity.Livreur;
import com.smart.entity.Zone;
import com.smart.entity.Enum.PrioriteEnum;
import com.smart.entity.Enum.StatutColis;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ColisMapperImpl.class, ZoneMapperImpl.class})
public class ColisMapperTest {

    @Autowired
    private ColisMapper mapper;

    @Test
    public void testToDTO() {
        Colis entity = new Colis();
        entity.setId("1");
        entity.setDescription("Test Description");
        entity.setPoids(BigDecimal.valueOf(2.5));
        entity.setPriorite(PrioriteEnum.MOYENNE);
        entity.setVilleDestination("Test Ville");
        entity.setStatut(StatutColis.CREE);

        Livreur livreur = new Livreur();
        livreur.setId("livreur1");
        entity.setLivreur(livreur);

        ClientExpediteur client = new ClientExpediteur();
        client.setId("client1");
        entity.setClientExpediteur(client);

        Destinataire dest = new Destinataire();
        dest.setId("dest1");
        entity.setDestinataire(dest);

        Zone zone = new Zone();
        zone.setId("zone1");
        entity.setZone(zone);

        ColisDTO dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getDescription(), dto.getDescription());
        assertEquals(entity.getPoids(), dto.getPoids());
        assertEquals(entity.getPriorite(), dto.getPriorite());
        assertEquals(entity.getVilleDestination(), dto.getVilleDestination());
        assertEquals(entity.getStatut(), dto.getStatut());
        assertEquals(entity.getLivreur().getId(), dto.getLivreurId());
        assertEquals(entity.getClientExpediteur().getId(), dto.getClientExpediteurId());
        assertEquals(entity.getDestinataire().getId(), dto.getDestinataireId());
        assertEquals(entity.getZone().getId(), dto.getZoneId());
    }

    @Test
    public void testToEntity() {
        ColisDTO dto = new ColisDTO();
        dto.setId("1");
        dto.setDescription("Test Description");
        dto.setPoids(BigDecimal.valueOf(2.5));
        dto.setPriorite(PrioriteEnum.MOYENNE);
        dto.setVilleDestination("Test Ville");
        dto.setStatut(StatutColis.CREE);
        dto.setLivreurId("livreur1");
        dto.setClientExpediteurId("client1");
        dto.setDestinataireId("dest1");
        dto.setZoneId("zone1");

        Colis entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getDescription(), entity.getDescription());
        assertEquals(dto.getPoids(), entity.getPoids());
        assertEquals(dto.getPriorite(), entity.getPriorite());
        assertEquals(dto.getVilleDestination(), entity.getVilleDestination());
        assertEquals(dto.getStatut(), entity.getStatut());
        assertEquals(dto.getLivreurId(), entity.getLivreur().getId());
        assertEquals(dto.getClientExpediteurId(), entity.getClientExpediteur().getId());
        assertEquals(dto.getDestinataireId(), entity.getDestinataire().getId());
        assertNotNull(entity.getZone());
        assertEquals(dto.getZoneId(), entity.getZone().getId());
    }
}
