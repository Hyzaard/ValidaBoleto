package com.anhembi.ValidaBoleto.infrastructure.persistence;

import com.anhembi.ValidaBoleto.adapters.BoletoParserGateway;
import com.anhembi.ValidaBoleto.core.entities.Boleto;
import com.anhembi.ValidaBoleto.core.enuns.StatusValidacao;
import org.springframework.stereotype.Repository;

@Repository
public class BoletoParserRepository implements BoletoParserGateway {
    @Override
    public Boleto parse(String linhaDigitavel) {
        // TODO: Implementar a lógica de parsing da linha digitável
        Boleto boleto = new Boleto();
        boleto.setLinhaDigitavel(linhaDigitavel);
        boleto.setStatus(StatusValidacao.VALIDO);
        return boleto;
    }
} 