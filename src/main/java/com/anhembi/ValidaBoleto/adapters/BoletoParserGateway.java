package com.anhembi.ValidaBoleto.adapters;

import com.anhembi.ValidaBoleto.core.entities.Boleto;

public interface BoletoParserGateway {
    public Boleto execute(String linhaDigitavel);
}
