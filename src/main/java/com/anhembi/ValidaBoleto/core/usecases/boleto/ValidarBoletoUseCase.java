package com.anhembi.ValidaBoleto.core.usecases.boleto;

import com.anhembi.ValidaBoleto.core.entities.Boleto;
import com.anhembi.ValidaBoleto.infrastructure.exception.ValidacaoException;

public interface ValidarBoletoUseCase {
    public Boleto validacao(String linhaDigitavel);
}
