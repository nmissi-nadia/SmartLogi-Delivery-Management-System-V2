package com.smart.repository;

import com.smart.entity.Colis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColisRepository extends JpaRepository<Colis, String> {
    List<Colis> findByStatut(String statut);
    List<Colis> findByVilleDestination(String villeDestination);
    List<Colis> findByPriorite(String priorite);
}