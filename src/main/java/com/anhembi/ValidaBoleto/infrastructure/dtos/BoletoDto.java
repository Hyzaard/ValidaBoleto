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
    private String codigoDeBarra;
    private String nomeBeneficiario;
    private String bancoEmissor;
    private BigDecimal valor;
    private LocalDate dataVencimento;
    private StatusValidacao status;
    private java.util.List<String> avisos;
    private java.util.List<String> erros;
    private String recomendacao;
}