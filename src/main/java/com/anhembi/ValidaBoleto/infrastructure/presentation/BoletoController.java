package com.anhembi.ValidaBoleto.infrastructure.presentation;

import com.anhembi.ValidaBoleto.core.entities.Boleto;
import com.anhembi.ValidaBoleto.core.usecases.boleto.BoletoParserUseCase;
import com.anhembi.ValidaBoleto.core.usecases.boleto.ValidarBoletoUseCase;
import com.anhembi.ValidaBoleto.infrastructure.dtos.BoletoDto;
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

    @Autowired
    public BoletoController(BoletoParserUseCase boletoParserUseCase, ValidarBoletoUseCase validarBoletoUseCase) {
        this.boletoParserUseCase = boletoParserUseCase;
        this.validarBoletoUseCase = validarBoletoUseCase;
    }

    @PostMapping("/validar")
    public ResponseEntity<?> validarBoleto(@RequestBody String linhaDigitavel) {
        if (linhaDigitavel == null || linhaDigitavel.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("A linha digitável não pode estar vazia");
        }

        try {
            Boleto boleto = boletoParserUseCase.execute(linhaDigitavel);
            return ResponseEntity.ok(toDto(boleto));
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
                .id(boleto.getId())
                .codigoDeBarra(boleto.getCodigoDeBarra())
                .linhaDigitavel(boleto.getLinhaDigitavel())
                .nomeBeneficiario(boleto.getNomeBeneficiario())
                .cpfCnpjBeneficiario(boleto.getCpfCnpjBeneficiario())
                .bancoEmissor(boleto.getBancoEmissor())
                .codigoDoBanco(boleto.getCodigoDoBanco())
                .valor(boleto.getValor())
                .dataVencimento(boleto.getDataVencimento())
                .status(boleto.getStatus())
                .build();
    }

    private Boleto toDomain(BoletoDto dto) {
        return Boleto.builder()
                .id(dto.getId())
                .codigoDeBarra(dto.getCodigoDeBarra())
                .linhaDigitavel(dto.getLinhaDigitavel())
                .nomeBeneficiario(dto.getNomeBeneficiario())
                .cpfCnpjBeneficiario(dto.getCpfCnpjBeneficiario())
                .bancoEmissor(dto.getBancoEmissor())
                .codigoDoBanco(dto.getCodigoDoBanco())
                .valor(dto.getValor())
                .dataVencimento(dto.getDataVencimento())
                .status(dto.getStatus())
                .build();
    }
}
