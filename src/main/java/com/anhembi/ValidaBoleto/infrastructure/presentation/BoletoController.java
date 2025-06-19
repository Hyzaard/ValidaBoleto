package com.anhembi.ValidaBoleto.infrastructure.presentation;

import com.anhembi.ValidaBoleto.core.entities.Boleto;
import com.anhembi.ValidaBoleto.core.usecases.boleto.BoletoParserUseCase;
import com.anhembi.ValidaBoleto.core.usecases.boleto.CriarBoletoUseCase;
import com.anhembi.ValidaBoleto.core.usecases.boleto.ValidarBoletoUseCase;
import com.anhembi.ValidaBoleto.core.usecases.usuario.UsuarioUseCase;
import com.anhembi.ValidaBoleto.infrastructure.dtos.BoletoDto;
import com.anhembi.ValidaBoleto.infrastructure.dtos.LinhaDigitavelDto;
import com.anhembi.ValidaBoleto.infrastructure.exception.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boletos")
public class BoletoController {

    private final BoletoParserUseCase boletoParserUseCase;
    private final ValidarBoletoUseCase validarBoletoUseCase;
    private final CriarBoletoUseCase criarBoletoUseCase;
    private final UsuarioUseCase usuarioUseCase;

    @Autowired
    public BoletoController(BoletoParserUseCase boletoParserUseCase, 
                           ValidarBoletoUseCase validarBoletoUseCase,
                           CriarBoletoUseCase criarBoletoUseCase,
                           UsuarioUseCase usuarioUseCase) {
        this.boletoParserUseCase = boletoParserUseCase;
        this.validarBoletoUseCase = validarBoletoUseCase;
        this.criarBoletoUseCase = criarBoletoUseCase;
        this.usuarioUseCase = usuarioUseCase;
    }

    @PostMapping("/validar")
    public ResponseEntity<?> validarBoleto(@RequestBody LinhaDigitavelDto linhaDigitavelDto) {
        if (linhaDigitavelDto == null) {
            return ResponseEntity.badRequest().body("Linha digitável é obrigatória");
        }
        
        if (linhaDigitavelDto.getLinhaDigitavel() == null) {
            return ResponseEntity.badRequest().body("Linha digitável é obrigatória");
        }
        
        if (linhaDigitavelDto.getLinhaDigitavel().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Linha digitável é obrigatória");
        }

        try {
            Boleto boleto = validarBoletoUseCase.validacao(linhaDigitavelDto.getLinhaDigitavel());
            return ResponseEntity.ok(toDto(boleto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar o boleto: " + e.getMessage());
        }
    }

    // Endpoint alternativo que aceita string diretamente
    @PostMapping("/validar-string")
    public ResponseEntity<?> validarBoletoString(@RequestBody String linhaDigitavel) {
        if (linhaDigitavel == null || linhaDigitavel.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Linha digitável é obrigatória");
        }

        try {
            Boleto boleto = validarBoletoUseCase.validacao(linhaDigitavel);
            return ResponseEntity.ok(toDto(boleto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar o boleto: " + e.getMessage());
        }
    }

    // Endpoint para validar e salvar com usuário
    @PostMapping("/validar-usuario/{usuarioId}")
    public ResponseEntity<?> validarESalvarBoleto(@PathVariable Long usuarioId, @RequestBody LinhaDigitavelDto linhaDigitavelDto) {
        System.out.println("DEBUG: usuarioId=" + usuarioId);
        System.out.println("DEBUG: linhaDigitavelDto=" + linhaDigitavelDto);
        if (linhaDigitavelDto != null) {
            System.out.println("DEBUG: linhaDigitavelDto.getLinhaDigitavel()=" + linhaDigitavelDto.getLinhaDigitavel());
        } else {
            System.out.println("DEBUG: linhaDigitavelDto é null");
        }
        if (linhaDigitavelDto == null) {
            return ResponseEntity.badRequest().body("Linha digitável é obrigatória");
        }
        if (linhaDigitavelDto.getLinhaDigitavel() == null) {
            return ResponseEntity.badRequest().body("Linha digitável é obrigatória");
        }
        if (linhaDigitavelDto.getLinhaDigitavel().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Linha digitável é obrigatória");
        }
        if (usuarioId == null || usuarioId <= 0) {
            return ResponseEntity.badRequest().body("ID do usuário inválido");
        }
        try {
            Boleto boleto = validarBoletoUseCase.validacao(linhaDigitavelDto.getLinhaDigitavel());
            Boleto boletoSalvo = criarBoletoUseCase.execute(boleto, usuarioId);
            // Salvar o resultado da validação no campo do usuário
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            String resultadoJson = mapper.writeValueAsString(boleto);
            var usuarioOpt = usuarioUseCase.buscarPorId(usuarioId);
            if (usuarioOpt.isPresent()) {
                var usuario = usuarioOpt.get();
                usuario.setResultadoUltimaValidacaoBoleto(resultadoJson);
                usuarioUseCase.atualizar(usuarioId, usuario);
            }
            return ResponseEntity.ok(toDto(boletoSalvo));
        } catch (ValidacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar o boleto: " + e.getMessage());
        }
    }

    // Endpoint alternativo que aceita string diretamente
    @PostMapping("/validar-string-usuario/{usuarioId}")
    public ResponseEntity<?> validarESalvarBoletoString(@PathVariable Long usuarioId, @RequestBody String linhaDigitavel) {
        if (linhaDigitavel == null || linhaDigitavel.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Linha digitável é obrigatória");
        }
        
        if (usuarioId == null || usuarioId <= 0) {
            return ResponseEntity.badRequest().body("ID do usuário inválido");
        }

        try {
            Boleto boleto = validarBoletoUseCase.validacao(linhaDigitavel);
            
            Boleto boletoSalvo = criarBoletoUseCase.execute(boleto, usuarioId);
            
            return ResponseEntity.ok(toDto(boletoSalvo));
        } catch (ValidacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar o boleto: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("ID inválido");
        }

        try {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar o boleto: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            return ResponseEntity.ok(List.of());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar os boletos: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("ID inválido");
        }

        try {
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao deletar o boleto: " + e.getMessage());
        }
    }

    private BoletoDto toDto(Boleto boleto) {
        return BoletoDto.builder()
                .codigoDeBarra(boleto.getCodigoDeBarra())
                .nomeBeneficiario(boleto.getNomeBeneficiario())
                .bancoEmissor(boleto.getBancoEmissor())
                .valor(boleto.getValor())
                .dataVencimento(boleto.getDataVencimento())
                .status(boleto.getStatus())
                .avisos(boleto.getAvisos())
                .erros(boleto.getErros())
                .recomendacao(boleto.getRecomendacao())
                .build();
    }

    private Boleto toDomain(BoletoDto dto) {
        return Boleto.builder()
                .codigoDeBarra(dto.getCodigoDeBarra())
                .nomeBeneficiario(dto.getNomeBeneficiario())
                .bancoEmissor(dto.getBancoEmissor())
                .valor(dto.getValor())
                .dataVencimento(dto.getDataVencimento())
                .status(dto.getStatus())
                .avisos(dto.getAvisos())
                .erros(dto.getErros())
                .recomendacao(dto.getRecomendacao())
                .build();
    }
}
