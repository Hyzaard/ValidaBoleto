package com.anhembi.ValidaBoleto.infrastructure.dtos;

import com.anhembi.ValidaBoleto.core.enuns.StatusValidacao;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class ResultadoValidacaoDto {
    private String codigoDeBarras;
    private StatusValidacao status;
    private List<String> avisos;
    private List<String> erros;
    private String recomendacao;
    private LocalDate dataVencimento;
    private BigDecimal valor;
    private String nomeBeneficiario;
    private String bancoEmissor;
}
