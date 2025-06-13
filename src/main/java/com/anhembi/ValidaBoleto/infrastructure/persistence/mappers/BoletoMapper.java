package com.anhembi.ValidaBoleto.infrastructure.persistence.mappers;

import com.anhembi.ValidaBoleto.core.entities.Boleto;
import com.anhembi.ValidaBoleto.infrastructure.persistence.BoletoEntity;
import org.springframework.stereotype.Component;

@Component
public class BoletoMapper {
    
    public BoletoEntity toEntity(Boleto boleto) {
        if (boleto == null) return null;
        
        return BoletoEntity.builder()
                .id(boleto.getId())
                .codigoDeBarra(boleto.getCodigoDeBarra())
                .linhaDigitavel(boleto.getLinhaDigitavel())
                .nomeBeneficiario(boleto.getNomeBeneficiario())
                .cpfCnpjBeneficiario(boleto.getCpfCnpjBeneficiario())
                .bancoEmissor(boleto.getBancoEmissor())
                .codigoDoBanco(boleto.getCodigoDoBanco())
                .valor(boleto.getValor())
                .dataVencimento(boleto.getDataVencimento())
                .status(boleto.getStatus())
                .build();
    }

    public Boleto toDomain(BoletoEntity entity) {
        if (entity == null) return null;
        
        Boleto boleto = new Boleto();
        boleto.setId(entity.getId());
        boleto.setCodigoDeBarra(entity.getCodigoDeBarra());
        boleto.setLinhaDigitavel(entity.getLinhaDigitavel());
        boleto.setNomeBeneficiario(entity.getNomeBeneficiario());
        boleto.setCpfCnpjBeneficiario(entity.getCpfCnpjBeneficiario());
        boleto.setBancoEmissor(entity.getBancoEmissor());
        boleto.setCodigoDoBanco(entity.getCodigoDoBanco());
        boleto.setValor(entity.getValor());
        boleto.setDataVencimento(entity.getDataVencimento());
        boleto.setStatus(entity.getStatus());
        return boleto;
    }
} 