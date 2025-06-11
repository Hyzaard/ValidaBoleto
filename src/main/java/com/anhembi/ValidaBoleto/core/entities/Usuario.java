package com.anhembi.ValidaBoleto.core.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
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


