package com.anhembi.ValidaBoleto.infrastructure.presentation;

import com.anhembi.ValidaBoleto.core.entities.Usuario;
import com.anhembi.ValidaBoleto.core.usecases.usuario.UsuarioUseCase;
import com.anhembi.ValidaBoleto.infrastructure.dtos.UsuarioDto;
import com.anhembi.ValidaBoleto.infrastructure.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioUseCase usuarioUseCase;

    @Autowired
    public UsuarioController(UsuarioUseCase usuarioUseCase) {
        this.usuarioUseCase = usuarioUseCase;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody UsuarioDto usuarioDto) {
        if (usuarioDto == null) {
            return ResponseEntity.badRequest().body("Dados do usuário não podem ser nulos");
        }

        try {
            var usuario = usuarioUseCase.criar(toDomain(usuarioDto));
            return ResponseEntity.status(HttpStatus.CREATED).body(toDto(usuario));
        } catch (ValidacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar usuário: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("ID inválido");
        }

        try {
            return usuarioUseCase.buscarPorId(id)
                    .map(usuario -> ResponseEntity.ok(toDto(usuario)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar usuário: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            var usuarios = usuarioUseCase.listarTodos().stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar usuários: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("ID inválido");
        }

        try {
            usuarioUseCase.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (ValidacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao deletar usuário: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody UsuarioDto usuarioDto) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("ID inválido");
        }
        if (usuarioDto == null) {
            return ResponseEntity.badRequest().body("Dados do usuário não podem ser nulos");
        }

        try {
            var usuario = usuarioUseCase.atualizar(id, toDomain(usuarioDto));
            return ResponseEntity.ok(toDto(usuario));
        } catch (ValidacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    private UsuarioDto toDto(Usuario usuario) {
        return UsuarioDto.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .build();
    }

    private Usuario toDomain(UsuarioDto dto) {
        return Usuario.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .email(dto.getEmail())
                .build();
    }
} 