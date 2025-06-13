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
        // Validação inicial do formato
        if (linhaDigitavel == null || linhaDigitavel.trim().isEmpty()) {
            throw new ValidacaoException("A linha digitável não pode estar vazia");
        }

        // Remove caracteres não numéricos
        String linhaDigitavelNumerica = linhaDigitavel.replaceAll("[^0-9]", "");
        
        // Validação do tamanho
        int length = linhaDigitavelNumerica.length();
        if (length < 47 || length > 54) {
            throw new ValidacaoException(
                    "A linha digitável deve conter entre 47 e 54 dígitos numéricos.");
        }

        // Reconstrói o código de barras a partir da linha digitável
        String codigoDeBarra = construirCodigoDeBarra(linhaDigitavelNumerica);
        System.out.println("[DEBUG] Codigo de Barras: " + codigoDeBarra);
        String fatorStr = codigoDeBarra.substring(5, 9);
        System.out.println("[DEBUG] Fator de Vencimento extraído: " + fatorStr);

        LocalDate dataVencimento = null;
        if (!"0000".equals(fatorStr)) {
            int fatorVencimento = Integer.parseInt(fatorStr);
            dataVencimento = DATA_BASE_FATOR_VENCIMENTO.plusDays(fatorVencimento);
            System.out.println("[DEBUG] Data de Vencimento calculada: " + dataVencimento);
        } else {
            System.out.println("[DEBUG] Boleto sem data de vencimento (fator 0000)");
        }

        BigDecimal valor = new BigDecimal(codigoDeBarra.substring(9, 19)).divide(new BigDecimal(100));
        Boleto boleto = boletoParserGateway.parse(linhaDigitavelNumerica);
        return boleto.toBuilder()
                .codigoDeBarra(codigoDeBarra)
                .bancoEmissor(codigoDeBarra.substring(0, 3))
                .valor(valor)
                .dataVencimento(dataVencimento)
                .build();
    }

    private String construirCodigoDeBarra(String linhaDigitavel) {
        // Padrão Febraban para boletos bancários (47 dígitos):
        // 0-3: banco/moeda
        // 4: dígito verificador geral
        // 5-8: fator de vencimento
        // 9-18: valor
        // 19-43: campo livre
        // Montagem: 0-3 + 32 + 33-36 + 37-46 + 4-8 + 10-19 + 21-30
        return new StringBuilder()
            .append(linhaDigitavel, 0, 4)    // banco e moeda
            .append(linhaDigitavel, 32, 33)  // dígito verificador geral
            .append(linhaDigitavel, 33, 37)  // fator de vencimento
            .append(linhaDigitavel, 37, 47)  // valor
            .append(linhaDigitavel, 4, 9)    // campo livre parte 1
            .append(linhaDigitavel, 10, 20)  // campo livre parte 2
            .append(linhaDigitavel, 21, 31)  // campo livre parte 3
            .toString();
    }

    private void validarDigitosVerificadoresCampos(String linhaDigitavel) throws ValidacaoException {
        // Campo 1: posições 1-9, dígito 10
        String campo1 = linhaDigitavel.substring(0, 9);
        int digitoVerificadorCampo1 = Integer.parseInt(linhaDigitavel.substring(9, 10));
        int digitoCalculadoCampo1 = DigitoVerificador.modulo10(campo1);
        if (digitoCalculadoCampo1 != digitoVerificadorCampo1) {
            throw new ValidacaoException(String.format(
                "Dígito verificador do campo 1 é inválido. Esperado: %d, Calculado: %d, Campo: %s, Linha completa: %s",
                digitoVerificadorCampo1, digitoCalculadoCampo1, campo1, linhaDigitavel));
        }

        // Campo 2: posições 11-20, dígito 21
        String campo2 = linhaDigitavel.substring(10, 20);
        int digitoVerificadorCampo2 = Integer.parseInt(linhaDigitavel.substring(20, 21));
        int digitoCalculadoCampo2 = DigitoVerificador.modulo10(campo2);
          if (digitoCalculadoCampo2 != digitoVerificadorCampo2) {
            throw new ValidacaoException(String.format(
                "Dígito verificador do campo 2 é inválido. Esperado: %d, Calculado: %d, Campo: %s, Linha completa: %s",
                digitoVerificadorCampo2, digitoCalculadoCampo2, campo2, linhaDigitavel));
        }

        // Campo 3: posições 22-31, dígito 32
        String campo3 = linhaDigitavel.substring(21, 31);
        int digitoVerificadorCampo3 = Integer.parseInt(linhaDigitavel.substring(31, 32));
        int digitoCalculadoCampo3 = DigitoVerificador.modulo10(campo3);
        if (digitoCalculadoCampo3 != digitoVerificadorCampo3) {
            throw new ValidacaoException(String.format(
                "Dígito verificador do campo 3 é inválido. Esperado: %d, Calculado: %d, Campo: %s, Linha completa: %s",
                digitoVerificadorCampo3, digitoCalculadoCampo3, campo3, linhaDigitavel));
        }
    }

    private void validarDigitoVerificadorGeral(String codigoDeBarras) throws ValidacaoException {
        char digitoVerificadorGeral = codigoDeBarras.charAt(4);
        String dadosParaCalculo = codigoDeBarras.substring(0, 4) + codigoDeBarras.substring(5);

        int digitoVerificadorCalculado = DigitoVerificador.modulo11Bancario(dadosParaCalculo);

        if (digitoVerificadorCalculado != Character.getNumericValue(digitoVerificadorGeral)) {
            throw new ValidacaoException(String.format(
                "Dígito verificador geral do código de barras é inválido. Esperado: %c, Calculado: %d, Código: %s",
                digitoVerificadorGeral, digitoVerificadorCalculado, codigoDeBarras));
        }
    }

}
