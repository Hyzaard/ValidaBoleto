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
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioUseCase usuarioUseCase;

    @Autowired
    public UsuarioController(UsuarioUseCase usuarioUseCase) {
        this.usuarioUseCase = usuarioUseCase;
    }

    @PostMapping
    public ResponseEntity<UsuarioDto> criar(@RequestBody UsuarioDto usuarioDto) {
        try {
            var usuario = usuarioUseCase.criar(toDomain(usuarioDto));
            return ResponseEntity.status(HttpStatus.CREATED).body(toDto(usuario));
        } catch (ValidacaoException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> buscarPorId(@PathVariable Long id) {
        return usuarioUseCase.buscarPorId(id)
                .map(usuario -> ResponseEntity.ok(toDto(usuario)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDto>> listarTodos() {
        var usuarios = usuarioUseCase.listarTodos().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuarios);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            usuarioUseCase.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (ValidacaoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto> atualizar(@PathVariable Long id, @RequestBody UsuarioDto usuarioDto) {
        try {
            var usuario = usuarioUseCase.atualizar(id, toDomain(usuarioDto));
            return ResponseEntity.ok(toDto(usuario));
        } catch (ValidacaoException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private UsuarioDto toDto(Usuario usuario) {
        return UsuarioDto.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .cpf(usuario.getCpf())
                .build();
    }

    private Usuario toDomain(UsuarioDto dto) {
        return Usuario.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .email(dto.getEmail())
                .cpf(dto.getCpf())
                .build();
    }
} 