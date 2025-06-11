package com.anhembi.ValidaBoleto.application;

import com.anhembi.ValidaBoleto.adapters.BoletoGateway;
import com.anhembi.ValidaBoleto.core.entities.Boleto;
import com.anhembi.ValidaBoleto.core.enuns.StatusValidacao;
import com.anhembi.ValidaBoleto.core.usecases.boleto.ValidarBoletoUseCase;
import com.anhembi.ValidaBoleto.infrastructure.dtos.ResultadoValidacaoDto;
import com.anhembi.ValidaBoleto.infrastructure.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ValidarBoletoUseCaseImpl implements ValidarBoletoUseCase {

    private final BoletoGateway boletoGateway;

    @Autowired
    public ValidarBoletoUseCaseImpl(
            BoletoGateway boletoGateway) {
        this.boletoGateway = boletoGateway;
    }

    @Override
    public ResultadoValidacaoDto execute(String linhaDigitavel) {
        List<String> avisos = new ArrayList<>();
        List<String> erros = new ArrayList<>();

        try {
            // Remove caracteres não numéricos da linha digitável
            String linhaDigitavelNumerica = linhaDigitavel.replaceAll("[^0-9]", "");

            if (linhaDigitavelNumerica.length() != 47) {
                throw new ValidacaoException("A linha digitável deve conter 47 dígitos numéricos.");
            }

            // Cria um novo boleto com a linha digitável
            Boleto boleto = Boleto.builder()
                    .linhaDigitavel(linhaDigitavelNumerica)
                    .build();

            // Validações básicas
            validarCamposObrigatorios(boleto, erros);
            validarCodigoDeBarra(boleto, erros);
            validarLinhaDigitavel(boleto, erros);
            validarDataVencimento(boleto, avisos);
            validarValor(boleto, erros);
            validarBeneficiario(boleto, erros);
            validarBancoEmissor(boleto, erros);

            // Determina status e nível de risco
            StatusValidacao status = determinarStatus(erros, avisos);

            // Gera recomendação
            String recomendacao = gerarRecomendacao(status, avisos, erros);

            return ResultadoValidacaoDto.builder()
                    .codigoDeBarras(boleto.getCodigoDeBarra())
                    .status(status)
                    .avisos(avisos)
                    .erros(erros)
                    .recomendacao(recomendacao)
                    .dataVencimento(boleto.getDataVencimento())
                    .valor(boleto.getValor())
                    .nomeBeneficiario(boleto.getNomeBeneficiario())
                    .bancoEmissor(boleto.getBancoEmissor())
                    .build();

        } catch (ValidacaoException e) {
            erros.add("Erro na validação: " + e.getMessage());
            return ResultadoValidacaoDto.builder()
                    .status(StatusValidacao.FRAUDULENTO)
                    .avisos(avisos)
                    .erros(erros)
                    .recomendacao("Boleto inválido: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            erros.add("Ocorreu um erro inesperado durante a validação.");
            return ResultadoValidacaoDto.builder()
                    .status(StatusValidacao.FRAUDULENTO)
                    .avisos(avisos)
                    .erros(erros)
                    .recomendacao("Erro inesperado durante a validação")
                    .build();
        }
    }

    private void validarCamposObrigatorios(Boleto boleto, List<String> erros) {
        if (boleto.getCodigoDeBarra() == null || boleto.getCodigoDeBarra().trim().isEmpty()) {
            erros.add("Código de barras é obrigatório");
        }
        if (boleto.getLinhaDigitavel() == null || boleto.getLinhaDigitavel().trim().isEmpty()) {
            erros.add("Linha digitável é obrigatória");
        }
        if (boleto.getNomeBeneficiario() == null || boleto.getNomeBeneficiario().trim().isEmpty()) {
            erros.add("Nome do beneficiário é obrigatório");
        }
        if (boleto.getBancoEmissor() == null || boleto.getBancoEmissor().trim().isEmpty()) {
            erros.add("Banco emissor é obrigatório");
        }
        if (boleto.getValor() == null) {
            erros.add("Valor é obrigatório");
        }
        if (boleto.getDataVencimento() == null) {
            erros.add("Data de vencimento é obrigatória");
        }
    }

    private void validarCodigoDeBarra(Boleto boleto, List<String> erros) {
        String codigo = boleto.getCodigoDeBarra().replaceAll("[^0-9]", "");
        if (codigo.length() != 44) {
            erros.add("Código de barras deve conter 44 dígitos");
        }
        if (!validarDigitoVerificador(codigo)) {
            erros.add("Código de barras inválido");
        }
    }

    private void validarLinhaDigitavel(Boleto boleto, List<String> erros) {
        String linha = boleto.getLinhaDigitavel().replaceAll("[^0-9]", "");
        if (linha.length() != 47) {
            erros.add("Linha digitável deve conter 47 dígitos");
        }
        if (!validarDigitoVerificador(linha)) {
            erros.add("Linha digitável inválida");
        }
    }

    private void validarDataVencimento(Boleto boleto, List<String> avisos) {
        LocalDate hoje = LocalDate.now();
        if (boleto.getDataVencimento() != null && boleto.getDataVencimento().isBefore(hoje)) {
            avisos.add("Boleto vencido");
        }
    }

    private void validarValor(Boleto boleto, List<String> erros) {
        if (boleto.getValor() != null && boleto.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            erros.add("Valor deve ser maior que zero");
        }
    }

    private void validarBeneficiario(Boleto boleto, List<String> erros) {
        if (boleto.getNomeBeneficiario() != null && boleto.getNomeBeneficiario().length() < 3) {
            erros.add("Nome do beneficiário deve ter pelo menos 3 caracteres");
        }
    }

    private void validarBancoEmissor(Boleto boleto, List<String> erros) {
        if (boleto.getBancoEmissor() != null && boleto.getBancoEmissor().length() < 3) {
            erros.add("Nome do banco emissor deve ter pelo menos 3 caracteres");
        }
    }

    private StatusValidacao determinarStatus(List<String> erros, List<String> avisos) {
        if (!erros.isEmpty()) {
            return StatusValidacao.FRAUDULENTO;
        }
        if (!avisos.isEmpty()) {
            return StatusValidacao.SUSPEITO;
        }
        return StatusValidacao.VALIDO;
    }

    private String gerarRecomendacao(StatusValidacao status, List<String> avisos, List<String> erros) {
        switch (status) {
            case VALIDO:
                return "Boleto válido e seguro para pagamento.";
            case SUSPEITO:
                return "Boleto válido, mas com observações: " + String.join(", ", avisos);
            case FRAUDULENTO:
                return "Boleto inválido: " + String.join(", ", erros);
            default:
                return "Status de validação desconhecido.";
        }
    }

    private boolean validarDigitoVerificador(String codigo) {
        int soma = 0;
        int peso = 2;
        for (int i = codigo.length() - 2; i >= 0; i--) {
            int digito = Character.getNumericValue(codigo.charAt(i));
            soma += digito * peso;
            peso = peso == 9 ? 2 : peso + 1;
        }
        int resto = soma % 11;
        int dv = resto < 2 ? 0 : 11 - resto;
        return dv == Character.getNumericValue(codigo.charAt(codigo.length() - 1));
    }
}
