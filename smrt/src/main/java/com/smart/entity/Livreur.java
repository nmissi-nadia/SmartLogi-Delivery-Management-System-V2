package com.smart.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "livreurs")
@PrimaryKeyJoinColumn(name = "id")
public class Livreur extends User {

    private String vehicule;
    private boolean disponibilite;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;
}
