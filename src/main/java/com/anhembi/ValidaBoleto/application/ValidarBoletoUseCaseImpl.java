package com.anhembi.ValidaBoleto.application;

import com.anhembi.ValidaBoleto.adapters.BoletoGateway;
import com.anhembi.ValidaBoleto.core.entities.Boleto;
import com.anhembi.ValidaBoleto.core.enuns.StatusValidacao;
import com.anhembi.ValidaBoleto.core.usecases.boleto.BoletoParserUseCase;
import com.anhembi.ValidaBoleto.core.usecases.boleto.ValidarBoletoUseCase;
import com.anhembi.ValidaBoleto.infrastructure.dtos.ResultadoValidacaoDto;
import com.anhembi.ValidaBoleto.infrastructure.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ValidarBoletoUseCaseImpl implements ValidarBoletoUseCase {    private final BoletoParserUseCase boletoParserUseCase;

    public ValidarBoletoUseCaseImpl(BoletoParserUseCase boletoParserUseCase) {
        this.boletoParserUseCase = boletoParserUseCase;
    }

    @Override
    public Boleto validacao(String linhaDigitavel) {
        List<String> avisos = new ArrayList<>();
        List<String> erros = new ArrayList<>();

        try {
            // Remove caracteres não numéricos da linha digitável
            String linhaDigitavelNumerica = linhaDigitavel.replaceAll("[^0-9]", "");

            if (linhaDigitavelNumerica.length() != 47) {
                throw new ValidacaoException("A linha digitável deve conter 47 dígitos numéricos.");
            }

            // Usa o BoletoParserUseCase para validar e extrair informações do boleto
            Boleto boleto = boletoParserUseCase.execute(linhaDigitavelNumerica);

            // Validações adicionais
            validarDVs(linhaDigitavelNumerica, boleto, erros);
            validarCodigoDeBarras(boleto, erros);
            validarBancoEmissorOficial(boleto, erros);
            validarDataVencimento(boleto, erros);
            validarValor(boleto, erros, avisos);
            validarBancoEmissor(boleto, erros);

            // Determina status e nível de risco
            StatusValidacao status = determinarStatus(erros, avisos);

            // Gera recomendação
            String recomendacao = gerarRecomendacao(status, avisos, erros);

            return Boleto.builder()
                    .codigoDeBarra(boleto.getCodigoDeBarra())
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
            return Boleto.builder()
                    .status(StatusValidacao.FRAUDULENTO)
                    .avisos(avisos)
                    .erros(erros)
                    .recomendacao("Boleto inválido: " + e.getMessage())
                    .build();
        } catch (Exception e) {
            erros.add("Ocorreu um erro inesperado durante a validação.");
            return Boleto.builder()
                    .status(StatusValidacao.FRAUDULENTO)
                    .avisos(avisos)
                    .erros(erros)
                    .recomendacao("Erro inesperado durante a validação")
                    .build();
        }
    }

    private void validarValor(Boleto boleto, List<String> erros, List<String> avisos) {
        if (boleto.getValor() != null && boleto.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            erros.add("Valor deve ser maior que zero");
        }
        if (boleto.getValor() != null && boleto.getValor().compareTo(new BigDecimal("10000")) > 0) {
            avisos.add("Valor do boleto acima do normal");
        }
    }

    private void validarBancoEmissor(Boleto boleto, List<String> erros) {
        if (boleto.getBancoEmissor() != null && boleto.getBancoEmissor().length() < 3) {
            erros.add("Código do banco emissor deve ter pelo menos 3 caracteres");
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
                return "Boleto válido para pagamento.";
            case SUSPEITO:
                return "Boleto válido, mas com observações: " +
                       "Conferir os dados do beneficiário do boleto, " +
                       "verifique se a impressão do boleto foi feita pelas vias oficiais, " +
                       "não negocie com estranhos e se estiver com dúvidas entre em contato com a empresa que emitiu o boleto. " +
                       String.join(", ", avisos);
            case FRAUDULENTO:
                return "Boleto inválido: " + String.join(", ", erros);
            default:
                return "Status de validação desconhecido.";
        }
    }

    // Validação dos dígitos verificadores (DV) da linha digitável e do código de barras
    private void validarDVs(String linhaDigitavel, Boleto boleto, List<String> erros) {
        // Exemplo simplificado: cheque se o DV geral do código de barras bate
        // (Implementação real depende do padrão Febraban, aqui é um placeholder)
        if (boleto.getCodigoDeBarra() != null && boleto.getCodigoDeBarra().length() == 44) {
            char dvCalculado = calcularDVBarra(boleto.getCodigoDeBarra());
            char dvInformado = boleto.getCodigoDeBarra().charAt(4);
            if (dvCalculado != dvInformado) {
                erros.add("Dígito verificador do código de barras inválido");
            }
        }
        // (Opcional) Validar DVs dos campos da linha digitável
    }

    // Placeholder para cálculo do DV do código de barras (módulo 11 simplificado)
    private char calcularDVBarra(String codigoDeBarra) {
        int soma = 0;
        int peso = 2;
        for (int i = 43; i >= 0; i--) {
            if (i == 4) continue; // Pula o DV
            soma += Character.getNumericValue(codigoDeBarra.charAt(i)) * peso;
            peso = (peso == 9) ? 2 : peso + 1;
        }
        int resto = soma % 11;
        int dv = 11 - resto;
        if (dv == 0 || dv == 10 || dv == 11) dv = 1;
        return Character.forDigit(dv, 10);
    }

    // Validação do código de barras (estrutura)
    private void validarCodigoDeBarras(Boleto boleto, List<String> erros) {
        String barra = boleto.getCodigoDeBarra();
        if (barra == null || barra.length() != 44) {
            erros.add("Código de barras inválido ou com tamanho incorreto");
        }
        // (Opcional) Validar prefixos e campos fixos
    }

    // Validação do banco emissor na lista oficial (simplificado)
    private void validarBancoEmissorOficial(Boleto boleto, List<String> erros) {
        String banco = boleto.getBancoEmissor();
        if (banco == null || banco.length() != 3) {
            erros.add("Código do banco emissor inválido");
            return;
        }
        // Lista simplificada dos principais bancos
        String[] bancosOficiais = {"001", "033", "104", "237", "341", "356", "399", "422", "745"};
        boolean encontrado = false;
        for (String b : bancosOficiais) {
            if (banco.equals(b)) {
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            erros.add("Banco emissor não consta na lista oficial Febraban");
        }
    }

    // Validação da data de vencimento (antiga ou muito futura)
    private void validarDataVencimento(Boleto boleto, List<String> erros) {
        if (boleto.getDataVencimento() != null) {
            LocalDate hoje = LocalDate.now();
            if (boleto.getDataVencimento().isBefore(hoje.minusYears(1))) {
                erros.add("Data de vencimento muito antiga");
            }
            if (boleto.getDataVencimento().isAfter(hoje.plusYears(2))) {
                erros.add("Data de vencimento muito distante no futuro");
            }
        }
    }
}
