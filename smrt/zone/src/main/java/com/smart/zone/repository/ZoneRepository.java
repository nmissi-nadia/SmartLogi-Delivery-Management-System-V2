package com.smart.zone.repository;


import com.smart.common.src.main.java.com.smart.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, String> {
    Zone findByNom(String nom);
    Optional<Zone> findByNomAndCodePostal(String nom, String codePostal);
}