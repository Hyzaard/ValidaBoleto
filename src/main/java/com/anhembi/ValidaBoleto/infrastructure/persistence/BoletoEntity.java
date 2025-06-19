package com.anhembi.ValidaBoleto.infrastructure.persistence;

import com.anhembi.ValidaBoleto.core.enuns.StatusValidacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "boletos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoletoEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_de_barra", nullable = false, length = 44)
    private String codigoDeBarra;

    @Column(name = "linha_digitavel", nullable = false, length = 47)
    private String linhaDigitavel;

    @Column(name = "nome_beneficiario", nullable = false)
    private String nomeBeneficiario;

    @Column(name = "cpf_cnpj_beneficiario")
    private String cpfCnpjBeneficiario;

    @Column(name = "banco_emissor", nullable = false)
    private String bancoEmissor;

    @Column(name = "codigo_do_banco")
    private String codigoDoBanco;

    @Column(name = "valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusValidacao status;

    @Column(name = "observacao")
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity usuario;
}
