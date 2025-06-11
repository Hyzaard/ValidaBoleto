package com.anhembi.ValidaBoleto.application;

import com.anhembi.ValidaBoleto.adapters.BoletoGateway;
import com.anhembi.ValidaBoleto.core.entities.Boleto;
import com.anhembi.ValidaBoleto.core.usecases.boleto.CriarBoletoUseCase;

public class CriarBoletoUseCaseImpl implements CriarBoletoUseCase {

    private final BoletoGateway boletoGateway;

    public CriarBoletoUseCaseImpl(BoletoGateway boletoGateway){
        this.boletoGateway = boletoGateway;
    }

    @Override
    public Boleto execute(Boleto boleto){
        return boletoGateway.criarBoleto(boleto);
    }
}
