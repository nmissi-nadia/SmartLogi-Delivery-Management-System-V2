package com.smart.repository;

import com.smart.entity.Livreur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface LivreurRepository extends JpaRepository<Livreur, String> {
    @Query("SELECT l FROM Livreur l WHERE lower(l.nom) LIKE lower(concat('%', :keyword, '%')) OR lower(l.prenom) LIKE lower(concat('%', :keyword, '%')) OR lower(l.telephone) LIKE lower(concat('%', :keyword, '%'))")
    Page<Livreur> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}