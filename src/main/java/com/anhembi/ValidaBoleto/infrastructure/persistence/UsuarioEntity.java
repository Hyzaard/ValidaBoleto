package com.anhembi.ValidaBoleto.infrastructure.persistence;

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