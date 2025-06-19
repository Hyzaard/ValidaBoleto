package com.anhembi.ValidaBoleto.infrastructure.persistence.mappers;

import com.anhembi.ValidaBoleto.core.entities.Usuario;
import com.anhembi.ValidaBoleto.infrastructure.persistence.UsuarioEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import com.anhembi.ValidaBoleto.infrastructure.persistence.BoletoEntity;
import com.anhembi.ValidaBoleto.infrastructure.dtos.BoletoDto;
import com.anhembi.ValidaBoleto.infrastructure.dtos.UsuarioDto;
import com.anhembi.ValidaBoleto.infrastructure.persistence.mappers.BoletoMapper;

@Component
public class UsuarioMapper {
    
    private final BoletoMapper boletoMapper;

    public UsuarioMapper(BoletoMapper boletoMapper) {
        this.boletoMapper = boletoMapper;
    }

    public UsuarioEntity toEntity(Usuario usuario) {
        if (usuario == null) return null;
        UsuarioEntity entity = UsuarioEntity.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .resultadoUltimaValidacaoBoleto(usuario.getResultadoUltimaValidacaoBoleto())
                .build();
        // Mapear os boletos
        if (usuario.getBoletos() != null) {
            entity.setBoletos(usuario.getBoletos().stream()
                    .map(boletoMapper::toEntity)
                    .collect(Collectors.toList()));
            // Configurar o relacionamento bidirecional
            entity.getBoletos().forEach(boleto -> boleto.setUsuario(entity));
        }
        return entity;
    }

    public Usuario toDomain(UsuarioEntity entity) {
        if (entity == null) return null;
        Usuario usuario = Usuario.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .email(entity.getEmail())
                .resultadoUltimaValidacaoBoleto(entity.getResultadoUltimaValidacaoBoleto())
                .build();
        // Mapear os boletos
        if (entity.getBoletos() != null) {
            usuario.setBoletos(entity.getBoletos().stream()
                    .map(boletoMapper::toDomain)
                    .collect(Collectors.toList()));
        }
        return usuario;
    }

    public UsuarioDto toDto(UsuarioEntity entity) {
        if (entity == null) return null;
        UsuarioDto dto = UsuarioDto.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .email(entity.getEmail())
                .resultadoUltimaValidacaoBoleto(entity.getResultadoUltimaValidacaoBoleto())
                .boletos(entity.getBoletos() != null ?
                    entity.getBoletos().stream().map(boletoMapper::toDto).collect(Collectors.toList()) : null)
                .build();
        return dto;
    }

    public UsuarioDto toDto(Usuario usuario) {
        if (usuario == null) return null;
        return UsuarioDto.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .resultadoUltimaValidacaoBoleto(usuario.getResultadoUltimaValidacaoBoleto())
                .boletos(usuario.getBoletos() != null ?
                    usuario.getBoletos().stream().map(boletoMapper::toDto).collect(Collectors.toList()) : null)
                .build();
    }

    public Usuario toDomain(UsuarioDto dto) {
        if (dto == null) return null;
        
        Usuario usuario = new Usuario();
        usuario.setId(dto.getId());
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setResultadoUltimaValidacaoBoleto(dto.getResultadoUltimaValidacaoBoleto());
        
        // Mapear os boletos se existirem
        if (dto.getBoletos() != null) {
            usuario.setBoletos(dto.getBoletos().stream()
                    .map(boletoMapper::toDomain)
                    .collect(Collectors.toList()));
        }
        
        return usuario;
    }

    public static UsuarioEntity toEntity(UsuarioDto dto) {
        if (dto == null) return null;
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(dto.getId());
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setResultadoUltimaValidacaoBoleto(dto.getResultadoUltimaValidacaoBoleto());
        return entity;
    }
}