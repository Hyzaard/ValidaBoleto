package com.anhembi.ValidaBoleto.application;

import com.anhembi.ValidaBoleto.adapters.UsuarioGateway;
import com.anhembi.ValidaBoleto.core.entities.Usuario;
import com.anhembi.ValidaBoleto.infrastructure.exception.ValidacaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioUseCaseImplTest {

    @Mock
    private UsuarioGateway usuarioGateway;

    @InjectMocks
    private UsuarioUseCaseImpl usuarioUseCase;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder()
                .nome("Teste Usuario")
                .email("teste@email.com")
                .cpf("12345678900")
                .build();
    }

    @Test
    void criar_DeveCriarUsuarioComSucesso() throws ValidacaoException {
        when(usuarioGateway.existePorEmail(any())).thenReturn(false);
        when(usuarioGateway.salvar(any())).thenReturn(usuario);

        Usuario resultado = usuarioUseCase.criar(usuario);

        assertNotNull(resultado);
        assertEquals(usuario.getNome(), resultado.getNome());
        assertEquals(usuario.getEmail(), resultado.getEmail());
        verify(usuarioGateway).salvar(any());
    }

    @Test
    void criar_DeveLancarExcecaoQuandoEmailJaExiste() {
        when(usuarioGateway.existePorEmail(any())).thenReturn(true);

        assertThrows(ValidacaoException.class, () -> usuarioUseCase.criar(usuario));
        verify(usuarioGateway, never()).salvar(any());
    }

    @Test
    void buscarPorId_DeveRetornarUsuarioQuandoEncontrado() {
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioUseCase.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(usuario.getNome(), resultado.get().getNome());
    }

    @Test
    void listarTodos_DeveRetornarListaDeUsuarios() {
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioGateway.listarTodos()).thenReturn(usuarios);

        List<Usuario> resultado = usuarioUseCase.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(usuario.getNome(), resultado.get(0).getNome());
    }

    @Test
    void deletar_DeveDeletarUsuarioComSucesso() throws ValidacaoException {
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioGateway).deletar(1L);

        assertDoesNotThrow(() -> usuarioUseCase.deletar(1L));
        verify(usuarioGateway).deletar(1L);
    }

    @Test
    void deletar_DeveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThrows(ValidacaoException.class, () -> usuarioUseCase.deletar(1L));
        verify(usuarioGateway, never()).deletar(any());
    }

    @Test
    void atualizar_DeveAtualizarUsuarioComSucesso() throws ValidacaoException {
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.of(usuario));
        when(usuarioGateway.existePorEmail(any())).thenReturn(false);
        when(usuarioGateway.salvar(any())).thenReturn(usuario);

        Usuario resultado = usuarioUseCase.atualizar(1L, usuario);

        assertNotNull(resultado);
        assertEquals(usuario.getNome(), resultado.getNome());
        verify(usuarioGateway).salvar(any());
    }

    @Test
    void atualizar_DeveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        when(usuarioGateway.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThrows(ValidacaoException.class, () -> usuarioUseCase.atualizar(1L, usuario));
        verify(usuarioGateway, never()).salvar(any());
    }
} 