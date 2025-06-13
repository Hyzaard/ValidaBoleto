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
        UsuarioEntity entity = usuarioMapper.toEntity(usuario);
        entity = jpaRepository.save(entity);
        return usuarioMapper.toDomain(entity);
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
