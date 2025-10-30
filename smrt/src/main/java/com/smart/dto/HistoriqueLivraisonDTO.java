package com.smart.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HistoriqueLivraisonDTO {
    private String id;
    private String colisId;
    private String statut;
    private LocalDateTime dateChangement;
    private String commentaire;
}