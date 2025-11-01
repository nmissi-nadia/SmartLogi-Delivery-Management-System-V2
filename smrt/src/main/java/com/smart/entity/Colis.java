package com.smart.entity;

import com.smart.entity.Enum.StatutColis;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "colis")
public class Colis {

    @Id
    @Column(name = "id") // Bonne pratique: Spécifier le nom de colonne de l'ID
    private String id;

    @Column(name = "description")
    private String description;

    @Column(name = "poids")
    private Double poids;

    @Column(name = "priorite")
    private String priorite;

    @Column(name = "ville_destination")
    private String villeDestination;

    // Utilisation de l'Enum Java pour correspondre au type 'colis_status_enum' de la DB
    // CORRECTION: J'ai corrigé l'import, en supposant que l'Enum s'appelle ColisStatus
    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private StatutColis statut;


    // RELATION LIVREUR (NULLABLE) : Correction pour une meilleure pratique JPA
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livreur_id") // La colonne 'livreur_id' sera créée par Liquibase et est NULLABLE par défaut ici
    private Livreur livreur;


    // Relation Client Expéditeur (NON NULLABLE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_expediteur_id", nullable = false)
    private ClientExpediteur clientExpediteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinataire_id", nullable = false)
    private Destinataire destinataire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zoneLivraison;


    @OneToMany(mappedBy = "colis", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoriqueLivraison> historique;

    @OneToMany(mappedBy = "colis", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ColisProduit> produitsContenus;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}