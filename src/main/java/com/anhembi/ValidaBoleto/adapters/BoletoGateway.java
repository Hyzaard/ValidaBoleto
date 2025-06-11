package com.anhembi.ValidaBoleto.adapters;

import com.anhembi.ValidaBoleto.core.entities.Boleto;

import java.util.List;

public interface BoletoGateway {

    Boleto criarBoleto(Boleto boleto);
    List<Boleto> buscarBoletos();
}
