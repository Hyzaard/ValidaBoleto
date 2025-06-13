package com.anhembi.ValidaBoleto.infrastructure.persistence;

import com.anhembi.ValidaBoleto.adapters.ValidarBoletoGateway;
import com.anhembi.ValidaBoleto.core.entities.Boleto;
import com.anhembi.ValidaBoleto.core.enuns.StatusValidacao;
import org.springframework.stereotype.Repository;

@Repository
public class ValidarBoletoRepository implements ValidarBoletoGateway {
    @Override
    public Boleto validar(String linhaDigitavel) {
        // TODO: Implementar a lógica de validação do boleto
        Boleto boleto = new Boleto();
        boleto.setLinhaDigitavel(linhaDigitavel);
        boleto.setStatus(StatusValidacao.VALIDO);
        return boleto;
    }
} 