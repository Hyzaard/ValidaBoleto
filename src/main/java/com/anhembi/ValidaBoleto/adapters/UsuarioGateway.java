package com.anhembi.ValidaBoleto.adapters;

import com.anhembi.ValidaBoleto.core.entities.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioGateway {
    Usuario salvar(Usuario usuario);
    Optional<Usuario> buscarPorId(Long id);
    List<Usuario> listarTodos();
    void deletar(Long id);
    boolean existePorEmail(String email);
    boolean existePorCpf(String cpf);
} 