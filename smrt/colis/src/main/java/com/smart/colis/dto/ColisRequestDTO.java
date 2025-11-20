package com.smart.colis.dto;

import com.smart.utilisateur.src.main.java.com.smart.utilisateur.dto.ClientExpediteurDTO;
import com.smart.utilisateur.src.main.java.com.smart.utilisateur.dto.DestinataireDTO;
import com.smart.zone.src.main.java.com.smart.zone.dto.ZoneDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class ColisRequestDTO {
    @NotBlank(message = "La description est requise")
    private String description;
    
    @NotNull(message = "Le poids est requis")
    private Double poids;
    
    private String priorite; // HAUTE, MOYENNE, BASSE
    
    @NotBlank(message = "La ville de destination est requise")
    private String villeDestination;
    
    @Valid
    @NotNull(message = "Les informations du client expéditeur sont requises")
    private ClientExpediteurDTO clientExpediteur;
    
    @Valid
    @NotNull(message = "Les informations du destinataire sont requises")
    private DestinataireDTO destinataire;
    
    @Valid
    private ZoneDTO zone;
    
    @Valid
    private List<ColisProduitDTO> produits;
}