package com.smart.repository;

import com.smart.entity.GestionnaireLogistique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GestionnaireLogistiqueRepository extends JpaRepository<GestionnaireLogistique, Long> {
}