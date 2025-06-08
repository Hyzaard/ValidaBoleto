package com.anhembi.ValidaBoleto.infrastructure.persistence;

import jakarta.persistence.*;

@Entity
@Table(name = "DADOS_BOLETO")
public class BoletoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    // campos para o banco de dados automatico

    @Column(nullable = false, unique = true)
    private Long linhaDigitavel;

    @Column(nullable = false, unique = true)
    private Long codBarras;

    @Column(nullable = false)
    private String nomeEmpresa;

    @Column(nullable = false)
    private String nomePagador;

    @Column(nullable = false)
    private Double datVencimento;

    @Column(nullable = false)
    private String bancoEmissor;

    @Column(nullable = false)
    private int agencia;

    @Column(nullable = false)
    private int conta;


    public Long linhaDigitavel() {
        return linhaDigitavel;
    }

    public BoletoEntity
    setLinhaDigitavel(Long linhaDigitavel) {
        this.linhaDigitavel = linhaDigitavel;
        return this;
    }

    public Long codBarras() {
        return codBarras;
    }

    public BoletoEntity
    setCodBarras(Long codBarras) {
        this.codBarras = codBarras;
        return this;
    }

    public String nomeEmpresa() {
        return nomeEmpresa;
    }

    public BoletoEntity
    setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
        return this;
    }

    public String nomePagador() {
        return nomePagador;
    }

    public BoletoEntity
    setNomePagador(String nomePagador) {
        this.nomePagador = nomePagador;
        return this;
    }

    public Double datVencimento() {
        return datVencimento;
    }

    public BoletoEntity
    setDatVencimento(Double datVencimento) {
        this.datVencimento = datVencimento;
        return this;
    }

    public String bancoEmissor() {
        return bancoEmissor;
    }

    public BoletoEntity
    setBancoEmissor(String bancoEmissor) {
        this.bancoEmissor = bancoEmissor;
        return this;
    }

    public int agencia() {
        return agencia;
    }

    public BoletoEntity
    setAgencia(int agencia) {
        this.agencia = agencia;
        return this;
    }

    public int conta() {
        return conta;
    }

    public BoletoEntity
    setConta(int conta) {
        this.conta = conta;
        return this;
    }




}
