package com.smart.repository;

import com.smart.entity.Destinataire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DestinataireRepository extends JpaRepository<Destinataire, String> {
    Optional<Destinataire> findByEmail(String email);
    
    /**
     * Recherche un destinataire par nom ET email (exact match, case-sensitive)
     * Utilisé pour l'authentification publique des destinataires
     */
    Optional<Destinataire> findByNomAndEmail(String nom, String email);
}