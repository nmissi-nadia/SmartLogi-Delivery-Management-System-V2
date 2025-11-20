package com.smart.common.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
@DiscriminatorValue("MANAGER")
public class GestionnaireLogistique extends User {
    // Les champs spécifiques au gestionnaire peuvent être ajoutés ici.
}