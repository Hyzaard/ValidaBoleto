package com.anhembi.ValidaBoleto.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
@Repository
public interface JpaUsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    boolean existsByEmail(String email);
} 