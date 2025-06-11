package com.anhembi.ValidaBoleto.infrastructure.persistence;

<<<<<<< HEAD
import com.anhembi.ValidaBoleto.core.enuns.StatusValidacao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "USUARIOS")
@Getter
@Setter
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column
    private String telefone;

    // Dados do boleto
    @Column
    private String codigoDeBarra;

    @Column
    private String linhaDigitavel;

    @Column
    private String nomeBeneficiario;

    @Column
    private String bancoEmissor;

    @Column
    private BigDecimal valor;

    @Column
    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING)
    private StatusValidacao statusBoleto;

    @Column(length = 1000)
    private String observacoes;
} 
=======
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
>>>>>>> 69de5dba08673a97a8de5b414ec7e2097d96eba3
