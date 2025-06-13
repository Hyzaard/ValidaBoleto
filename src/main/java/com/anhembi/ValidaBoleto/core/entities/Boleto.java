package com.anhembi.ValidaBoleto.core.entities;

import com.anhembi.ValidaBoleto.core.enuns.StatusValidacao;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
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
    private List<String> avisos;
    private List<String> erros;
    private String recomendacao;

    public boolean isVencido() {
        return dataVencimento != null && dataVencimento.isBefore(LocalDate.now());
    }

    public boolean temCodigoDeBarraValido() {
        return codigoDeBarra != null && codigoDeBarra.length() == 44;
    }

    public boolean temValorValido() {
        return valor != null && valor.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean temCpfCnpjBeneficiarioValido() {
        return cpfCnpjBeneficiario != null && 
               (cpfCnpjBeneficiario.length() == 11 || cpfCnpjBeneficiario.length() == 14);
    }
}
