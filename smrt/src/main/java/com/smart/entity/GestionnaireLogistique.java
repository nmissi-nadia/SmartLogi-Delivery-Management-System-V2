package com.smart.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gestionnaires_logistiques")
@PrimaryKeyJoinColumn(name = "id")
public class GestionnaireLogistique extends User {

    private String poste;
    private String bureau;
}
