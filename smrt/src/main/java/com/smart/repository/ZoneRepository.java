package com.smart.repository;

import com.smart.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, String> {
    Zone findByNom(String nom);
}