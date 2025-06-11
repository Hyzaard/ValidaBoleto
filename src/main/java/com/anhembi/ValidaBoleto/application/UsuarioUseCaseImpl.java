package com.anhembi.ValidaBoleto.application;

import com.anhembi.ValidaBoleto.adapters.UsuarioGateway;
import com.anhembi.ValidaBoleto.core.entities.Usuario;
import com.anhembi.ValidaBoleto.core.usecases.usuario.UsuarioUseCase;
import com.anhembi.ValidaBoleto.infrastructure.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioUseCaseImpl implements UsuarioUseCase {

    private final UsuarioGateway usuarioGateway;

    @Autowired
    public UsuarioUseCaseImpl(UsuarioGateway usuarioGateway) {
        this.usuarioGateway = usuarioGateway;
    }

    @Override
    public Usuario criar(Usuario usuario) throws ValidacaoException {
        validarUsuario(usuario);
        return usuarioGateway.salvar(usuario);
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioGateway.buscarPorId(id);
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarioGateway.listarTodos();
    }

    @Override
    public void deletar(Long id) throws ValidacaoException {
        if (!usuarioGateway.buscarPorId(id).isPresent()) {
            throw new ValidacaoException("Usuário não encontrado");
        }
        usuarioGateway.deletar(id);
    }

    @Override
    public Usuario atualizar(Long id, Usuario usuario) throws ValidacaoException {
        if (!usuarioGateway.buscarPorId(id).isPresent()) {
            throw new ValidacaoException("Usuário não encontrado");
        }
        validarUsuario(usuario);
        usuario = usuario.toBuilder().id(id).build();
        return usuarioGateway.salvar(usuario);
    }

    private void validarUsuario(Usuario usuario) throws ValidacaoException {
        if (usuario == null) {
            throw new ValidacaoException("Usuário não pode ser nulo");
        }
        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new ValidacaoException("Nome é obrigatório");
        }
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new ValidacaoException("Email é obrigatório");
        }
        if (usuarioGateway.existePorEmail(usuario.getEmail())) {
            throw new ValidacaoException("Email já cadastrado");
        }
    }

} 