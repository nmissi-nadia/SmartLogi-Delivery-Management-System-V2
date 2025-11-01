package com.smart.repository;

import com.smart.entity.ColisProduit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColisProduitRepository extends JpaRepository<ColisProduit, String> {
}