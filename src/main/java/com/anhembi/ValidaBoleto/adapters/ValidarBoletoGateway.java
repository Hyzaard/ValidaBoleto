package com.anhembi.ValidaBoleto.adapters;

import com.anhembi.ValidaBoleto.core.entities.Boleto;

public interface ValidarBoletoGateway {
    Boleto validar(String linhaDigitavel);
}
