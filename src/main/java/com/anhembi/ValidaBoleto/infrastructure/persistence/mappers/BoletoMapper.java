package com.anhembi.ValidaBoleto.infrastructure.persistence.mappers;

import com.anhembi.ValidaBoleto.core.entities.Boleto;
import com.anhembi.ValidaBoleto.infrastructure.persistence.BoletoEntity;
import com.anhembi.ValidaBoleto.infrastructure.dtos.BoletoDto;
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
                .observacao(boleto.getRecomendacao())
                .build();
    }

    public BoletoEntity toEntity(Boleto boleto, com.anhembi.ValidaBoleto.infrastructure.persistence.UsuarioEntity usuario) {
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
                .observacao(boleto.getRecomendacao())
                .usuario(usuario)
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

    public BoletoDto toDto(BoletoEntity entity) {
        if (entity == null) return null;
        return BoletoDto.builder()
                .codigoDeBarra(entity.getCodigoDeBarra())
                .nomeBeneficiario(entity.getNomeBeneficiario())
                .bancoEmissor(entity.getBancoEmissor())
                .valor(entity.getValor())
                .dataVencimento(entity.getDataVencimento())
                .status(entity.getStatus())
                .build();
    }

    public BoletoDto toDto(Boleto boleto) {
        if (boleto == null) return null;
        return BoletoDto.builder()
                .codigoDeBarra(boleto.getCodigoDeBarra())
                .nomeBeneficiario(boleto.getNomeBeneficiario())
                .bancoEmissor(boleto.getBancoEmissor())
                .valor(boleto.getValor())
                .dataVencimento(boleto.getDataVencimento())
                .status(boleto.getStatus())
                .build();
    }

    public Boleto toDomain(BoletoDto dto) {
        if (dto == null) return null;
        
        Boleto boleto = new Boleto();
        boleto.setCodigoDeBarra(dto.getCodigoDeBarra());
        boleto.setNomeBeneficiario(dto.getNomeBeneficiario());
        boleto.setBancoEmissor(dto.getBancoEmissor());
        boleto.setValor(dto.getValor());
        boleto.setDataVencimento(dto.getDataVencimento());
        boleto.setStatus(dto.getStatus());
        return boleto;
    }
}