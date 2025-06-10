package com.anhembi.ValidaBoleto.core.usecases.boleto;

import com.anhembi.ValidaBoleto.core.entities.Boleto;

import java.util.List;

public interface BuscarBoletoCase {

    public List<Boleto> execute();

}
