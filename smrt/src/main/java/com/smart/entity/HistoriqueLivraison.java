package com.smart.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class HistoriqueLivraison {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "colis_id")
    private Colis colis;

    private String statut;
    private LocalDateTime dateChangement;
    private String commentaire;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}