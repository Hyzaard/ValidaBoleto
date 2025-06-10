package com.anhembi.ValidaBoleto.core.usecases.boleto;

import com.anhembi.ValidaBoleto.core.entities.Boleto;
import com.anhembi.ValidaBoleto.core.gateway.BoletoGateway;

public class CriarBoletoUseCaseImpl implements CriarBoletoUseCase {

    private final BoletoGateway boletoGateway;

    public CriarBoletoUseCaseImpl(BoletoGateway boletoGateway){
        this.boletoGateway = boletoGateway;
    }

    @Override
    public Boleto execute(Boleto boleto){
        return eventoGateway.criarBoleto(boleto);
    }
}
