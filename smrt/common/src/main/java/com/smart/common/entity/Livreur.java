package com.smart.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@DiscriminatorValue("LIVREUR")
public class Livreur extends User {
    
    private String telephone;
    private String vehicule;

    @ManyToOne
    @JoinColumn(name = "zone_assignee_id")
    private Zone zoneAssignee;

    @OneToMany(mappedBy = "livreur")
    private List<Colis> colisALivrer;
}