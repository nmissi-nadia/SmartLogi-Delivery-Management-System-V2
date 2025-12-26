package com.smart.repository;

import com.smart.entity.ClientExpediteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ClientExpediteurRepository extends JpaRepository<ClientExpediteur, String> {
    @Query("SELECT c FROM ClientExpediteur c WHERE lower(c.nom) LIKE lower(concat('%', :keyword, '%')) OR lower(c.prenom) LIKE lower(concat('%', :keyword, '%')) OR lower(c.email) LIKE lower(concat('%', :keyword, '%')) OR lower(c.telephone) LIKE lower(concat('%', :keyword, '%'))")
    Page<ClientExpediteur> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT c FROM ClientExpediteur c WHERE c.user.username = :username")
    Optional<ClientExpediteur> findByUsername(@Param("username") String username);
}