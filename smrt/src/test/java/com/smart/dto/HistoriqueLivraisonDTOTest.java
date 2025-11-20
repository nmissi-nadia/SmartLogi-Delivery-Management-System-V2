package com.smart.dto;


import com.smart.common.src.main.java.com.smart.Enum.StatutColis;
import com.smart.livraison.src.main.java.com.smart.livraison.dto.HistoriqueLivraisonDTO;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class HistoriqueLivraisonDTOTest {

    @Test
    void testFromEntity() {
        HistoriqueLivraison entity = new HistoriqueLivraison();
        entity.setId("1");
        Colis colis = new Colis();
        colis.setId("colis1");
        entity.setColis(colis);
        entity.setStatut(StatutColis.EN_TRANSIT);
        entity.setDateChangement(LocalDateTime.now());
        entity.setCommentaire("Test comment");

        HistoriqueLivraisonDTO dto = HistoriqueLivraisonDTO.fromEntity(entity);

        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getColis().getId(), dto.getColisId());
        assertEquals(entity.getStatut().name(), dto.getStatut());
        assertEquals(entity.getStatut().toString(), dto.getStatutLibelle());
        assertEquals(entity.getDateChangement(), dto.getDateChangement());
        assertEquals(entity.getCommentaire(), dto.getCommentaire());
    }

    @Test
    void testGettersAndSetters() {
        HistoriqueLivraisonDTO dto = new HistoriqueLivraisonDTO();
        String id = "1";
        String colisId = "colis1";
        String statut = "LIVRE";
        LocalDateTime date = LocalDateTime.now();
        String commentaire = "Delivered";
        String statutLibelle = "Livré";

        dto.setId(id);
        dto.setColisId(colisId);
        dto.setStatut(statut);
        dto.setDateChangement(date);
        dto.setCommentaire(commentaire);
        dto.setStatutLibelle(statutLibelle);

        assertEquals(id, dto.getId());
        assertEquals(colisId, dto.getColisId());
        assertEquals(statut, dto.getStatut());
        assertEquals(date, dto.getDateChangement());
        assertEquals(commentaire, dto.getCommentaire());
        assertEquals(statutLibelle, dto.getStatutLibelle());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDateTime date = LocalDateTime.now();
        HistoriqueLivraisonDTO dto1 = new HistoriqueLivraisonDTO();
        dto1.setId("1");
        dto1.setColisId("colis1");
        dto1.setStatut("EN_TRANSIT");
        dto1.setDateChangement(date);
        dto1.setCommentaire("Comment");
        dto1.setStatutLibelle("En transit");

        HistoriqueLivraisonDTO dto2 = new HistoriqueLivraisonDTO();
        dto2.setId("1");
        dto2.setColisId("colis1");
        dto2.setStatut("EN_TRANSIT");
        dto2.setDateChangement(date);
        dto2.setCommentaire("Comment");
        dto2.setStatutLibelle("En transit");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }
    
    @Test
    void testToString() {
        HistoriqueLivraisonDTO dto = new HistoriqueLivraisonDTO();
        dto.setId("1");
        assertTrue(dto.toString().contains("id=1"));
    }
}
