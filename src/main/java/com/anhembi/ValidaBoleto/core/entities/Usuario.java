package com.anhembi.ValidaBoleto.core.entities;

import com.anhembi.ValidaBoleto.core.enuns.StatusValidacao;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder(toBuilder = true)
public class Usuario {
    private Long id;
    private String nome;
    private String email;
    private List<Boleto> boletos;
}
