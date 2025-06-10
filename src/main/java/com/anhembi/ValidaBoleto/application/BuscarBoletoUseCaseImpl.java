package com.anhembi.ValidaBoleto.core.usecases.boleto;

import com.anhembi.ValidaBoleto.core.gateway.BoletoGateway;

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
