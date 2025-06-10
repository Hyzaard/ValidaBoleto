package com.anhembi.ValidaBoleto.application;

import com.anhembi.ValidaBoleto.core.entities.Boleto;
import com.anhembi.ValidaBoleto.core.gateway.BoletoGateway;
import com.anhembi.ValidaBoleto.core.usecases.boleto.ValidarBoletoUseCase;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ValidarBoletoService implements ValidarBoletoUseCase {

    private final ValidarBoletoGateway validarBoletoGateway;

    @Autowired
    public ValidarBoletoService(ValidarBoletoGateway boletoGateway){
        this.validarBoletoGateway = boletoGateway;
    }

    public ValidarBoletoUseCaseImpl(BoletoGateway boletoGateway){
        this.boletoGateway = boletoGateway;
    }

    @Override
    public Boleto execute(Long id, String codigoDeBarra, String cpfCnpjBeneficiario,
                          String bancoEmissor, String conta,
                          String agencia, BigDecimal valor,
                          LocalDate dataVencimento, LocalDate dataEmissao){

    }
}
