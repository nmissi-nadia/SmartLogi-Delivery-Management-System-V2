package com.smart.repository;

import com.smart.entity.ClientExpediteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientExpediteurRepository extends JpaRepository<ClientExpediteur, Long> {
}