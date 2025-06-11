package com.anhembi.ValidaBoleto.core.entities;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder(toBuilder = true)
public class Usuario {
    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    private List<Boleto> boletos;
}
