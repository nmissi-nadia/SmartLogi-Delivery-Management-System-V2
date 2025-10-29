package com.smart.entity;

import com.smart.entity.Enum.StatutColis;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "historique_livraisons")
public class HistoriqueLivraison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateChangement;
    private String commentaire;

    @Enumerated(EnumType.STRING)
    private StatutColis statut;

    @ManyToOne
    @JoinColumn(name = "colis_id")
    private Colis colis;

    @ManyToOne
    @JoinColumn(name = "livreur_id")
    private Livreur livreur;
}
