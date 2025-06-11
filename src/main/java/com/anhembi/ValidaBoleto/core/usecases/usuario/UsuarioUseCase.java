package com.anhembi.ValidaBoleto.core.usecases.usuario;

import com.anhembi.ValidaBoleto.core.entities.Usuario;
import com.anhembi.ValidaBoleto.infrastructure.exception.ValidacaoException;

import java.util.List;
import java.util.Optional;

public interface UsuarioUseCase {
    Usuario criar(Usuario usuario) throws ValidacaoException;
    Optional<Usuario> buscarPorId(Long id);
    List<Usuario> listarTodos();
    void deletar(Long id) throws ValidacaoException;
    Usuario atualizar(Long id, Usuario usuario) throws ValidacaoException;
} 