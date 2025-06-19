package com.anhembi.ValidaBoleto.infrastructure.presentation;

import com.anhembi.ValidaBoleto.core.entities.Usuario;
import com.anhembi.ValidaBoleto.core.entities.Boleto;
import com.anhembi.ValidaBoleto.core.usecases.usuario.UsuarioUseCase;
import com.anhembi.ValidaBoleto.infrastructure.dtos.UsuarioDto;
import com.anhembi.ValidaBoleto.infrastructure.exception.ValidacaoException;
import com.anhembi.ValidaBoleto.infrastructure.persistence.mappers.UsuarioMapper;
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
    private final UsuarioMapper usuarioMapper;
    private final com.anhembi.ValidaBoleto.core.usecases.boleto.ValidarBoletoUseCase validarBoletoUseCase;

    @Autowired
    public UsuarioController(UsuarioUseCase usuarioUseCase, UsuarioMapper usuarioMapper, com.anhembi.ValidaBoleto.core.usecases.boleto.ValidarBoletoUseCase validarBoletoUseCase) {
        this.usuarioUseCase = usuarioUseCase;
        this.usuarioMapper = usuarioMapper;
        this.validarBoletoUseCase = validarBoletoUseCase;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody UsuarioDto usuarioDto) {
        try {
            // Validação básica
            if (usuarioDto == null) {
                return ResponseEntity.badRequest().body("Dados do usuário são obrigatórios");
            }
            
            if (usuarioDto.getNome() == null || usuarioDto.getNome().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Nome é obrigatório");
            }
            
            if (usuarioDto.getEmail() == null || usuarioDto.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email é obrigatório");
            }
            
            // Validação de formato de email
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
            if (!usuarioDto.getEmail().matches(emailRegex)) {
                return ResponseEntity.badRequest().body("Formato de email inválido");
            }
            
            // Criar usuário sem ID
            Usuario usuario = Usuario.builder()
                    .nome(usuarioDto.getNome().trim())
                    .email(usuarioDto.getEmail().trim().toLowerCase())
                    .resultadoUltimaValidacaoBoleto(usuarioDto.getResultadoUltimaValidacaoBoleto())
                    .build();
            
            // Salvar usuário
            Usuario usuarioSalvo = usuarioUseCase.criar(usuario);
            
            // Retornar resposta
            UsuarioDto resposta = UsuarioDto.builder()
                    .id(usuarioSalvo.getId())
                    .nome(usuarioSalvo.getNome())
                    .email(usuarioSalvo.getEmail())
                    .resultadoUltimaValidacaoBoleto(usuarioSalvo.getResultadoUltimaValidacaoBoleto())
                    .boletos(null)
                    .build();
            
            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
            
        } catch (ValidacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor: " + e.getMessage());
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

    @PostMapping("/{usuarioId}/validar-boleto")
    public ResponseEntity<?> validarBoletoParaUsuario(
            @PathVariable Long usuarioId,
            @RequestBody com.anhembi.ValidaBoleto.infrastructure.dtos.LinhaDigitavelDto linhaDigitavelDto) {
        if (linhaDigitavelDto == null || linhaDigitavelDto.getLinhaDigitavel() == null || linhaDigitavelDto.getLinhaDigitavel().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Linha digitável é obrigatória");
        }
        try {
            // Valida o boleto
            Boleto boleto = validarBoletoUseCase.validacao(linhaDigitavelDto.getLinhaDigitavel());

            // Serializa o resultado da validação
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
            mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            String resultadoJson = mapper.writeValueAsString(boleto);

            // Busca e atualiza o usuário
            var usuarioOpt = usuarioUseCase.buscarPorId(usuarioId);
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Usuário não encontrado");
            }
            var usuario = usuarioOpt.get();
            usuario.setResultadoUltimaValidacaoBoleto(resultadoJson);
            usuarioUseCase.atualizar(usuarioId, usuario);

            // Retorna o usuário atualizado (DTO)
            UsuarioDto usuarioDto = usuarioMapper.toDto(usuario);
            return ResponseEntity.ok(usuarioDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao validar e salvar resultado do boleto: " + e.getMessage());
        }
    }

    private UsuarioDto toDto(Usuario usuario) {
        return usuarioMapper.toDto(usuario);
    }

    private Usuario toDomain(UsuarioDto dto) {
        return usuarioMapper.toDomain(dto);
    }
} 