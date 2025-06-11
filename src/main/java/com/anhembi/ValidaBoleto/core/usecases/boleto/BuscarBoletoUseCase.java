package com.anhembi.ValidaBoleto.core.usecases.boleto;

import com.anhembi.ValidaBoleto.core.entities.Boleto;

import java.util.List;

public interface BuscarBoletoUseCase {

    public List<Boleto> execute();

}
