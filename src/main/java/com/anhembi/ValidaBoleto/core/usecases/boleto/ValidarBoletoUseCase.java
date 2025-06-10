package com.anhembi.ValidaBoleto.core.usecases.boleto;

import com.anhembi.ValidaBoleto.core.entities.Boleto;

public interface ValidarBoletoCase {

    public Boleto execute(Long id, String codigoDeBarra);

}
