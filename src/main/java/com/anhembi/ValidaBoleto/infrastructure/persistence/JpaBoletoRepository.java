package com.anhembi.ValidaBoleto.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaBoletoRepository extends JpaRepository<BoletoEntity, Long> {
    Optional<BoletoEntity> findByCodigoDeBarra(String codigoDeBarra);
    Optional<BoletoEntity> findByLinhaDigitavel(String linhaDigitavel);
} 