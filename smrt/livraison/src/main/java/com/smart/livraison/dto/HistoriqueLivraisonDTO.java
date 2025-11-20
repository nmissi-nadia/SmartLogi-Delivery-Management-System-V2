package com.smart.livraison.dto;

import com.smart.common.src.main.java.com.smart.entity.HistoriqueLivraison;
import lombok.Data;
import java.time.LocalDateTime;


@Data
public class HistoriqueLivraisonDTO {
    private String id;
    private String colisId;
    private String statut;
    private LocalDateTime dateChangement;
    private String commentaire;
    private String statutLibelle; // Pour l'affichage
    
    // Ajoutez ce constructeur statique si nécessaire
    public static HistoriqueLivraisonDTO fromEntity(HistoriqueLivraison entity) {
        HistoriqueLivraisonDTO dto = new HistoriqueLivraisonDTO();
        dto.setId(entity.getId());
        dto.setColisId(entity.getColis().getId());
        dto.setStatut(entity.getStatut().name());
        dto.setStatutLibelle(entity.getStatut().toString()); // Pour l'affichage
        dto.setDateChangement(entity.getDateChangement());
        dto.setCommentaire(entity.getCommentaire());
        return dto;
    }
}