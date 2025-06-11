package com.anhembi.ValidaBoleto.application;

import com.anhembi.ValidaBoleto.adapters.BoletoGateway;
import com.anhembi.ValidaBoleto.core.entities.Boleto;
import com.anhembi.ValidaBoleto.core.usecases.boleto.CriarBoletoUseCase;
import com.anhembi.ValidaBoleto.infrastructure.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CriarBoletoUseCaseImpl implements CriarBoletoUseCase {

    private final BoletoGateway boletoGateway;

    @Autowired
    public CriarBoletoUseCaseImpl(BoletoGateway boletoGateway) {
        this.boletoGateway = boletoGateway;
    }

    @Override
    public Boleto execute(Boleto boleto) throws ValidacaoException {
        try {
            validarBoleto(boleto);
            return boletoGateway.salvar(boleto);
        } catch (Exception e) {
            throw new ValidacaoException("Erro ao criar boleto: " + e.getMessage());
        }
    }

    private void validarBoleto(Boleto boleto) throws ValidacaoException {
        if (boleto == null) {
            throw new ValidacaoException("Boleto não pode ser nulo");
        }
        if (boleto.getCodigoDeBarra() == null || boleto.getCodigoDeBarra().trim().isEmpty()) {
            throw new ValidacaoException("Código de barras é obrigatório");
        }
        if (boleto.getLinhaDigitavel() == null || boleto.getLinhaDigitavel().trim().isEmpty()) {
            throw new ValidacaoException("Linha digitável é obrigatória");
        }
        if (boleto.getNomeBeneficiario() == null || boleto.getNomeBeneficiario().trim().isEmpty()) {
            throw new ValidacaoException("Nome do beneficiário é obrigatório");
        }
        if (boleto.getBancoEmissor() == null || boleto.getBancoEmissor().trim().isEmpty()) {
            throw new ValidacaoException("Banco emissor é obrigatório");
        }
        if (boleto.getValor() == null || boleto.getValor().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new ValidacaoException("Valor deve ser maior que zero");
        }
        if (boleto.getDataVencimento() == null) {
            throw new ValidacaoException("Data de vencimento é obrigatória");
        }
    }
}
