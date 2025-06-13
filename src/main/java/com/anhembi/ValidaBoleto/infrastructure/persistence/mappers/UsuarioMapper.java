package com.anhembi.ValidaBoleto.infrastructure.persistence.mappers;

import com.anhembi.ValidaBoleto.core.entities.Usuario;
import com.anhembi.ValidaBoleto.infrastructure.persistence.UsuarioEntity;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    
    public UsuarioEntity toEntity(Usuario usuario) {
        if (usuario == null) return null;
        
        return UsuarioEntity.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .build();
    }

    public Usuario toDomain(UsuarioEntity entity) {
        if (entity == null) return null;
        
        Usuario usuario = new Usuario();
        usuario.setId(entity.getId());
        usuario.setNome(entity.getNome());
        usuario.setEmail(entity.getEmail());
        return usuario;
    }
} 