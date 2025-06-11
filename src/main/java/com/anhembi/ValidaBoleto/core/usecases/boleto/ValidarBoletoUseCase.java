package com.anhembi.ValidaBoleto.core.usecases.boleto;

import com.anhembi.ValidaBoleto.infrastructure.dtos.ResultadoValidacaoDto;
import com.anhembi.ValidaBoleto.infrastructure.exception.ValidacaoException;

public interface ValidarBoletoUseCase {
    ResultadoValidacaoDto execute(String linhaDigitavel) throws ValidacaoException;
}
