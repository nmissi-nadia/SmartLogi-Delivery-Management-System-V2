package com.smart.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "client_expediteurs")
@PrimaryKeyJoinColumn(name = "id")
public class ClientExpediteur extends User {

    private String entreprise;
    private String adresse;
}
