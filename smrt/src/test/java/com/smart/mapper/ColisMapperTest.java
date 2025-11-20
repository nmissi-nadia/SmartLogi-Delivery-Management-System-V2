package com.smart.mapper;

import com.smart.colis.src.main.java.com.smart.colis.dto.ColisDTO;
import com.smart.colis.src.main.java.com.smart.colis.mapper.ColisMapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ColisMapperTest {

    private final ColisMapper mapper = Mappers.getMapper(ColisMapper.class);

    @Test
    public void testToDTO() {
        Colis entity = new Colis();
        entity.setId("1");
        entity.setDescription("Test Description");
        entity.setPoids(2.5);
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
        entity.setZoneLivraison(zone);

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
        assertEquals(entity.getZoneLivraison().getId(), dto.getZoneId());
    }

    @Test
    public void testToEntity() {
        ColisDTO dto = new ColisDTO();
        dto.setId("1");
        dto.setDescription("Test Description");
        dto.setPoids(2.5);
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
        assertEquals(dto.getZoneId(), entity.getZoneLivraison().getId());
    }
}
