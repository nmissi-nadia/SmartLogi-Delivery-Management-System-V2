package com.smart.mapper;


import com.smart.common.src.main.java.com.smart.Enum.StatutColis;
import com.smart.common.src.main.java.com.smart.entity.Colis;
import com.smart.common.src.main.java.com.smart.entity.HistoriqueLivraison;
import com.smart.livraison.src.main.java.com.smart.livraison.dto.HistoriqueLivraisonDTO;
import com.smart.livraison.src.main.java.com.smart.livraison.mapper.HistoriqueLivraisonMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class HistoriqueLivraisonMapperTest {

    private final HistoriqueLivraisonMapper mapper = Mappers.getMapper(HistoriqueLivraisonMapper.class);

    @Test
    public void testToDTO() {
        HistoriqueLivraison entity = new HistoriqueLivraison();
        entity.setId("1");
        entity.setStatut(StatutColis.CREE);
        entity.setDateChangement(LocalDateTime.now());
        entity.setCommentaire("Test Commentaire");

        Colis colis = new Colis();
        colis.setId("colis1");
        entity.setColis(colis);

        HistoriqueLivraisonDTO dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getColis().getId(), dto.getColisId());
        assertEquals(entity.getStatut().name(), dto.getStatut());
        assertEquals(entity.getStatut().toString(), dto.getStatutLibelle());
        assertEquals(entity.getDateChangement(), dto.getDateChangement());
        assertEquals(entity.getCommentaire(), dto.getCommentaire());
    }

    @Test
    public void testToEntity() {
        HistoriqueLivraisonDTO dto = new HistoriqueLivraisonDTO();
        dto.setColisId("colis1");
        dto.setStatut(StatutColis.CREE.name());
        dto.setCommentaire("Test Commentaire");

        HistoriqueLivraison entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertNotNull(entity.getColis());
        assertEquals(dto.getColisId(), entity.getColis().getId());
        assertEquals(dto.getStatut(), entity.getStatut().name());
        assertEquals(dto.getCommentaire(), entity.getCommentaire());
        assertNull(entity.getId());
        assertNull(entity.getDateChangement());
    }
}
