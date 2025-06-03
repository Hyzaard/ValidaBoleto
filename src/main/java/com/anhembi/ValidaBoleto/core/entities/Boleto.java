package com.anhembi.ValidaBoleto.core.entities;

import java.time.LocalDate;

public record Boleto(Long id,
                     String nome,
                     String documento,
                     String endereco,
                     String bancoDestino,
                     String conta,
                     String agencia,
                     String codigoDeBarras,
                     String qrCode,
                     LocalDate dataVencimento,
                     LocalDate dataEmissao
) {}
