package com.anhembi.ValidaBoleto.core.domain;

import com.anhembi.ValidaBoleto.core.enuns.StatusValidacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Boleto {

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


}