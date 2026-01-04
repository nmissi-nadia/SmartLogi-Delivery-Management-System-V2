package com.smart.entity;

import com.smart.entity.Enum.PrioriteEnum;
import com.smart.entity.Enum.StatutColis;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "colis")
public class Colis {

    @Id
    @Column(name = "id") 
    private String id;

    @Column(name = "description")
    private String description;

    @Column(name = "poids")
    private BigDecimal poids;

    @Enumerated(EnumType.STRING)
    @Column(name = "priorite")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private PrioriteEnum priorite;

    @Column(name = "ville_destination")
    private String villeDestination;

    
    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private StatutColis statut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_livreur")
    private Livreur livreur;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client_expediteur", nullable = false)
    private ClientExpediteur clientExpediteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_destinataire", nullable = false)
    private Destinataire destinataire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_zone")
    private Zone zone;


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