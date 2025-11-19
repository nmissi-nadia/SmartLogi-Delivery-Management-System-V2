package com.smart.common.src.main.java.com.smart.entity;

import com.smart.entity.Enum.Role; // Import de l'Enum
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user")
public class User {

    @Id
    private String id;

    private String username;
    private String password;
    private String email;

    // Modification pour utiliser l'Enum :
    @Enumerated(EnumType.STRING)
    @Column(name = "role") // Le nom de la colonne dans la base de données
    private Role role;
    // IMPORTANT : EnumType.STRING stocke la valeur exacte de l'enum (ex: "LIVREUR") en TEXT/VARCHAR, ce qui est simple et compatible avec PostgreSQL.

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
    }
}