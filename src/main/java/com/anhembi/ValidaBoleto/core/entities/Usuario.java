package com.anhembi.ValidaBoleto.core.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    private List<Boleto> boletos;
    private String resultadoUltimaValidacaoBoleto;

    public String getResultadoUltimaValidacaoBoleto() {
        return resultadoUltimaValidacaoBoleto;
    }

    public void setResultadoUltimaValidacaoBoleto(String resultadoUltimaValidacaoBoleto) {
        this.resultadoUltimaValidacaoBoleto = resultadoUltimaValidacaoBoleto;
    }
}


