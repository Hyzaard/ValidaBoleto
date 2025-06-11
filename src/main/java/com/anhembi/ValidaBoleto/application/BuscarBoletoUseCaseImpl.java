package com.anhembi.ValidaBoleto.application;

import com.anhembi.ValidaBoleto.adapters.BoletoGateway;
import com.anhembi.ValidaBoleto.core.entities.Boleto;
import com.anhembi.ValidaBoleto.core.usecases.boleto.BuscarBoletoUseCase;
import com.anhembi.ValidaBoleto.infrastructure.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuscarBoletoUseCaseImpl implements BuscarBoletoUseCase {

    private final BoletoGateway boletoGateway;

    @Autowired
    public BuscarBoletoUseCaseImpl(BoletoGateway boletoGateway) {
        this.boletoGateway = boletoGateway;
    }

    @Override
    public List<Boleto> execute() throws ValidacaoException {
        try {
            return boletoGateway.listarTodos();
        } catch (Exception e) {
            throw new ValidacaoException("Erro ao buscar boletos: " + e.getMessage());
        }
    }
}
