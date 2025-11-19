package com.smart.common.src.main.java.com.smart.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import com.smart.entity.Enum.StatutColis;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class HistoriqueLivraison {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "id_colis")
    private Colis colis;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private StatutColis statut;
    private LocalDateTime dateChangement;
    private String commentaire;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
        if (this.dateChangement == null) {
            this.dateChangement = LocalDateTime.now();
        }
    }
}