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
        try {
            // Validações básicas
            if (usuario == null) {
                throw new ValidacaoException("Usuário não pode ser nulo");
            }
            
            if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
                throw new ValidacaoException("Nome é obrigatório");
            }
            
            if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
                throw new ValidacaoException("Email é obrigatório");
            }
            
            // Validação de formato de email
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
            if (!usuario.getEmail().matches(emailRegex)) {
                throw new ValidacaoException("Formato de email inválido");
            }
            
            // Verificar se email já existe
            if (usuarioGateway.existePorEmail(usuario.getEmail())) {
                throw new ValidacaoException("Email já está cadastrado");
            }
            
            // Garantir que ID está nulo para criação
            usuario = usuario.toBuilder().id(null).build();
            
            // Salvar usuário
            return usuarioGateway.salvar(usuario);
            
        } catch (ValidacaoException e) {
            throw e;
        } catch (Exception e) {
            throw new ValidacaoException("Erro ao criar usuário: " + e.getMessage());
        }
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
        // Validação de formato de e-mail
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!usuario.getEmail().matches(emailRegex)) {
            throw new ValidacaoException("Email inválido");
        }
        // Permitir atualizar o próprio usuário sem erro de e-mail já cadastrado
        Optional<Usuario> usuarioExistente = usuarioGateway.buscarPorId(usuario.getId());
        if (usuarioGateway.existePorEmail(usuario.getEmail()) &&
            (!usuarioExistente.isPresent() || !usuarioExistente.get().getEmail().equals(usuario.getEmail()))) {
            throw new ValidacaoException("Email já cadastrado");
        }
    }

} 