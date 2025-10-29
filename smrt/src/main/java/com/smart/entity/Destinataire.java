package com.smart.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "destinataires")
@PrimaryKeyJoinColumn(name = "id")
public class Destinataire extends User {

    private String adresse;
}
