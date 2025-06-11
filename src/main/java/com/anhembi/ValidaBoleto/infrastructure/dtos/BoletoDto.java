package com.anhembi.ValidaBoleto.infrastructure.dtos;

import com.anhembi.ValidaBoleto.core.enuns.StatusValidacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoletoDto {
    private Long id;
    private String codigoDeBarra;
    private String linhaDigitavel;
    private String nomeBeneficiario;
    private String cpfCnpjBeneficiario;
    private String bancoEmissor;
    private String codigoDoBanco;
    private BigDecimal valor;
    private LocalDate dataVencimento;
    private StatusValidacao status;
    private String observacoes;
} 