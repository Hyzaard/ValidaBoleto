package com.anhembi.ValidaBoleto.core.usecases;

import com.anhembi.ValidaBoleto.core.entities.Boleto;

public interface validarBoletoCase {

    public Boleto validarBoleto(Long id, String codigoDeBarra);

}
