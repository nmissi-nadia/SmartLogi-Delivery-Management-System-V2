package com.smart.entity;

import com.smart.entity.Enum.StatutColis;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "colis")
public class Colis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String description;
    private Double poids;

    @Enumerated(EnumType.STRING)
    private StatutColis statut;

    private String priorite;
    private String villeDestination;

    @ManyToOne
    @JoinColumn(name = "client_expediteur_id")
    private ClientExpediteur clientExpediteur;

    @ManyToOne
    @JoinColumn(name = "destinataire_id")
    private Destinataire destinataire;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;

    @ManyToOne
    @JoinColumn(name = "livreur_id")
    private Livreur livreur;

    @OneToMany(mappedBy = "colis", cascade = CascadeType.ALL)
    private List<ColisProduit> produits;

    @OneToMany(mappedBy = "colis", cascade = CascadeType.ALL)
    private List<HistoriqueLivraison> historiques;
}
