package com.smart.colis.repository;


import com.smart.common.src.main.java.com.smart.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, String> {
    Optional<Produit> findByNomAndCategorie(String nom, String categorie);
}
