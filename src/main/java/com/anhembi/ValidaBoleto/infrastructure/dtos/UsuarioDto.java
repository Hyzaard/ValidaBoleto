package com.anhembi.ValidaBoleto.infrastructure.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDto {
    private Long id;
    private String nome;
    private String email;
    private List<ResultadoValidacaoDto> resultadoBoletos;
    private List<BoletoDto> boletos;
    private String resultadoUltimaValidacaoBoleto;

    public String getResultadoUltimaValidacaoBoleto() {
        return resultadoUltimaValidacaoBoleto;
    }

    public void setResultadoUltimaValidacaoBoleto(String resultadoUltimaValidacaoBoleto) {
        this.resultadoUltimaValidacaoBoleto = resultadoUltimaValidacaoBoleto;
    }
} 