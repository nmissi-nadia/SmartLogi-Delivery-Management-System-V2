package com.smart.repository;

import com.smart.entity.HistoriqueLivraison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoriqueLivraisonRepository extends JpaRepository<HistoriqueLivraison, String> {
    List<HistoriqueLivraison> findByColisId(String colisId);
}