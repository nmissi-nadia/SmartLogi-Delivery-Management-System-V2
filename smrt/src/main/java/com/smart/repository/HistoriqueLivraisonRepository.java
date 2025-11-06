package com.smart.repository;

import com.smart.entity.HistoriqueLivraison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoriqueLivraisonRepository extends JpaRepository<HistoriqueLivraison, String> {
    List<HistoriqueLivraison> findByColisId(String colisId);
    @Query("SELECT h FROM HistoriqueLivraison h WHERE h.colis.id = :colisId ORDER BY h.dateChangement DESC")
    List<HistoriqueLivraison> findHistoriqueByColisId(@Param("colisId") String colisId);
}