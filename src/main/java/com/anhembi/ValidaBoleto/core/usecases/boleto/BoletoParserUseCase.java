package com.anhembi.ValidaBoleto.core.usecases.boleto;

import com.anhembi.ValidaBoleto.core.entities.Boleto;
import com.anhembi.ValidaBoleto.infrastructure.exception.ValidacaoException;

public interface BoletoParserUseCase {
    public Boleto execute(String linhaDigitavel) throws ValidacaoException;
}
