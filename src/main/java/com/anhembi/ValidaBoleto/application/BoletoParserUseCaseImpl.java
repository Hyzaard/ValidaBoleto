package com.anhembi.ValidaBoleto.application;

import com.anhembi.ValidaBoleto.adapters.BoletoParserGateway;
import com.anhembi.ValidaBoleto.core.entities.Boleto;
import com.anhembi.ValidaBoleto.core.usecases.boleto.BoletoParserUseCase;
import com.anhembi.ValidaBoleto.infrastructure.exception.ValidacaoException;
import com.anhembi.ValidaBoleto.infrastructure.util.DigitoVerificador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class BoletoParserUseCaseImpl implements BoletoParserUseCase {

    private final BoletoParserGateway boletoParserGateway;

    private static final LocalDate DATA_BASE_FATOR_VENCIMENTO = LocalDate.of(1997, 10, 7);

    @Autowired
    public BoletoParserUseCaseImpl(BoletoParserGateway boletoParserGateway) {
        this.boletoParserGateway = boletoParserGateway;
    }

    @Override
    public Boleto execute(String linhaDigitavel) throws ValidacaoException {
        // Validação dos dígitos verificadores dos campos
        validarDigitosVerificadoresCampos(linhaDigitavel);

        // Reconstrói o código de barras a partir da linha digitável
        String codigoDeBarras = construirCodigoDeBarras(linhaDigitavel);

        // Valida o dígito verificador geral do código de barras
        validarDigitoVerificadorGeral(codigoDeBarras);

        // Extrai as informações do código de barras
        String banco = codigoDeBarras.substring(0, 3);
        int fatorVencimento = Integer.parseInt(codigoDeBarras.substring(5, 9));
        BigDecimal valor = new BigDecimal(codigoDeBarras.substring(9, 19)).divide(new BigDecimal(100));
        LocalDate dataVencimento = DATA_BASE_FATOR_VENCIMENTO.plus(fatorVencimento, ChronoUnit.DAYS);

        Boleto boleto = boletoParserGateway.parse(linhaDigitavel);
        return boleto.toBuilder()
                .bancoEmissor(banco)
                .valor(valor)
                .dataVencimento(dataVencimento)
                .build();
    }

    private String construirCodigoDeBarras(String linhaDigitavel) {
        return new StringBuilder()
                .append(linhaDigitavel, 0, 4)    // Campo 1: Banco e Moeda
                .append(linhaDigitavel, 32, 47)  // Campo 5: Fator de Vencimento e Valor
                .append(linhaDigitavel, 4, 9)    // Campo 2: Parte do campo livre
                .append(linhaDigitavel, 10, 20)  // Campo 3: Parte do campo livre
                .append(linhaDigitavel, 21, 31)  // Campo 4: Parte do campo livre
                .toString();
    }

    private void validarDigitosVerificadoresCampos(String linhaDigitavel) throws ValidacaoException {
        String campo1 = linhaDigitavel.substring(0, 9);
        int digitoVerificadorCampo1 = Integer.parseInt(linhaDigitavel.substring(9, 10));
        if (DigitoVerificador.calcularMod10(campo1) != digitoVerificadorCampo1) {
            throw new ValidacaoException("Dígito verificador do campo 1 é inválido.");
        }

        String campo2 = linhaDigitavel.substring(10, 20);
        int digitoVerificadorCampo2 = Integer.parseInt(linhaDigitavel.substring(20, 21));
        if (DigitoVerificador.calcularMod10(campo2) != digitoVerificadorCampo2) {
            throw new ValidacaoException("Dígito verificador do campo 2 é inválido.");
        }

        String campo3 = linhaDigitavel.substring(21, 31);
        int digitoVerificadorCampo3 = Integer.parseInt(linhaDigitavel.substring(31, 32));
        if (DigitoVerificador.calcularMod10(campo3) != digitoVerificadorCampo3) {
            throw new ValidacaoException("Dígito verificador do campo 3 é inválido.");
        }
    }

    private void validarDigitoVerificadorGeral(String codigoDeBarras) throws ValidacaoException {
        char digitoVerificadorGeral = codigoDeBarras.charAt(4);
        String dadosParaCalculo = codigoDeBarras.substring(0, 4) + codigoDeBarras.substring(5);

        int digitoVerificadorCalculado = DigitoVerificador.calcularMod11(dadosParaCalculo);

        if (digitoVerificadorCalculado != Character.getNumericValue(digitoVerificadorGeral)) {
            throw new ValidacaoException("Dígito verificador geral do código de barras é inválido.");
        }
    }

}
