package com.anhembi.ValidaBoleto.core.usecases.boleto;

import com.anhembi.ValidaBoleto.core.entities.Boleto;
import com.anhembi.ValidaBoleto.infrastructure.exception.ValidacaoException;

import java.util.List;

public interface BuscarBoletoUseCase {

    public List<Boleto> execute() throws ValidacaoException;

}
