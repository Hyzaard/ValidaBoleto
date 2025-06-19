package com.anhembi.ValidaBoleto.infrastructure.persistence;

import com.anhembi.ValidaBoleto.adapters.UsuarioGateway;
import com.anhembi.ValidaBoleto.core.entities.Usuario;
import com.anhembi.ValidaBoleto.infrastructure.persistence.mappers.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UsuarioRepository implements UsuarioGateway {
    
    private final JpaUsuarioRepository jpaRepository;
    private final UsuarioMapper usuarioMapper;

    @Override
    public Usuario salvar(Usuario usuario) {
        try {
            // Converter para entidade
            UsuarioEntity entity = usuarioMapper.toEntity(usuario);
            
            // Salvar no banco (JPA decide se é insert ou update baseado no ID)
            UsuarioEntity entitySalva = jpaRepository.save(entity);
            
            // Converter de volta para domínio
            return usuarioMapper.toDomain(entitySalva);
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar usuário: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(usuarioMapper::toDomain);
    }

    @Override
    public List<Usuario> listarTodos() {
        return jpaRepository.findAll().stream()
                .map(usuarioMapper::toDomain)
                .toList();
    }

    @Override
    public void deletar(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existePorEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }
}
