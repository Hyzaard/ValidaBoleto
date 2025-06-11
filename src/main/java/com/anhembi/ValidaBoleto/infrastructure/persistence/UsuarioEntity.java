package com.anhembi.ValidaBoleto.infrastructure.persistence;

import jakarta.persistence.*;

@Entity
@Table(name = "DADOS_USUARIO")
public class UsuarioEntity {
    public Long CPF() {
        return CPF;
    }

    public UsuarioEntity setCPF(Long CPF) {
        this.CPF = CPF;
        return this;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    // campos para o banco de dados automatico

    @Column(nullable = false, unique = true)
    private Long CPF;
    @Column(nullable = false)
    private String nomePagador;
    @Column(nullable = false)
    private String sobrenomePagador;
    @Column(nullable = false)
    private Double datNasc;
    @Column(nullable = false)
    private String nomeBanco;

    public String nomePagador() {
        return nomePagador;
    }

    public UsuarioEntity setNomePagador(String nomePagador) {
        this.nomePagador = nomePagador;
        return this;
    }

    public String sobrenomePagador() {
        return sobrenomePagador;
    }

    public UsuarioEntity setSobrenomePagador(String sobrenomePagador) {
        this.sobrenomePagador = sobrenomePagador;
        return this;
    }

    public Double datNasc() {
        return datNasc;
    }

    public UsuarioEntity setDatNasc(Double datNasc) {
        this.datNasc = datNasc;
        return this;
    }

    public String nomeBanco() {
        return nomeBanco;
    }

    public UsuarioEntity setNomeBanco(String nomeBanco) {
        this.nomeBanco = nomeBanco;
        return this;
    }



}
