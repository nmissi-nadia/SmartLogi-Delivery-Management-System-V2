package com.smart.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "zones")
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String ville;
    private String codePostal;

    @OneToMany(mappedBy = "zone")
    private List<Livreur> livreurs;

    @OneToMany(mappedBy = "zone")
    private List<Colis> colis;
}
