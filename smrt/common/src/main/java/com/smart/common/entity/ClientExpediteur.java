package com.smart.common.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
@Entity
@DiscriminatorValue("CLIENT")
public class ClientExpediteur extends User {

    private String telephone;
    private String adresse;

    @OneToMany(mappedBy = "clientExpediteur")
    private List<Colis> colisEnvoyes;
}
