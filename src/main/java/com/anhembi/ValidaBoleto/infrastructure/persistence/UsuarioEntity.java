package com.anhembi.ValidaBoleto.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<BoletoEntity> boletos;

    @Column(length = 2000)
    private String resultadoUltimaValidacaoBoleto;

    public String getResultadoUltimaValidacaoBoleto() {
        return resultadoUltimaValidacaoBoleto;
    }

    public void setResultadoUltimaValidacaoBoleto(String resultadoUltimaValidacaoBoleto) {
        this.resultadoUltimaValidacaoBoleto = resultadoUltimaValidacaoBoleto;
    }

}
