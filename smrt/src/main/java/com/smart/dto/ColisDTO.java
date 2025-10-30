package com.smart.dto;

import lombok.Data;
import java.util.List;

@Data
public class ColisDTO {
    private String id;
    private String description;
    private Double poids;
    private String statut;
    private String priorite;
    private String villeDestination;
    private String livreurId;
    private String clientExpediteurId;
    private String destinataireId;
    private String zoneId;
    private List<HistoriqueLivraisonDTO> historique;
    // On pourrait aussi ajouter les produitsDTO ici
}