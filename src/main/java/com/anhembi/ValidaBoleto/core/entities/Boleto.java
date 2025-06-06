package com.anhembi.ValidaBoleto.core.entities;

import com.anhembi.ValidaBoleto.core.enuns.StatusValidacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Boleto(Long id,
                     String nomeBeneficiario,
                     String cpfCnpjBeneficiario,
                     String endereco,
                     String bancoEmissor,
                     String conta,
                     String agencia,
                     String codigoDeBarra,
                     String tipoPagamento,
                     BigDecimal valor,
                     LocalDate dataVencimento,
                     LocalDate dataEmissao,
                     StatusValidacao status) {}
