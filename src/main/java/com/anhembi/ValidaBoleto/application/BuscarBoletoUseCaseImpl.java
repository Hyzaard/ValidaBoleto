package com.anhembi.ValidaBoleto.application;

import com.anhembi.ValidaBoleto.adapters.BoletoGateway;
import com.anhembi.ValidaBoleto.core.entities.Boleto;
import com.anhembi.ValidaBoleto.core.usecases.boleto.BuscarBoletoUseCase;

import java.util.List;

public class BuscarBoletoUseCaseImpl implements BuscarBoletoUseCase {

    private final BoletoGateway boletoGateway;

    public BuscarBoletoUseCaseImpl(BoletoGateway boletoGateway){
        this.boletoGateway = boletoGateway;
    }

    @Override
    public List<Boleto> execute(){
        return boletoGateway.buscarBoletos();
    }
}
