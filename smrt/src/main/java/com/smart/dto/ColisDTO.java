package com.smart.dto;

import lombok.Data;

import java.util.List;

import com.smart.entity.Enum.PrioriteEnum;
import com.smart.entity.Enum.StatutColis;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


@Data
public class ColisDTO {
    private String id;
    
    @NotBlank(message = "La description est requise")
    private String description;
    
    @NotNull(message = "Le poids est requis")
    @Positive(message = "Le poids doit être positif")
    private Double poids;
    
    private PrioriteEnum priorite;
    
    @NotBlank(message = "La ville de destination est requise")
    private String villeDestination;
    
    private StatutColis statut;
    
    private String livreurId;  // Optionnel lors de la création
    
    @NotBlank(message = "L'ID du client expéditeur est requis")
    private String clientExpediteurId;
    
    @NotBlank(message = "L'ID du destinataire est requis")
    private String destinataireId;
    
    private String zoneId;  // Optionnel selon votre logique métier
    
    private List<HistoriqueLivraisonDTO> historique;
}