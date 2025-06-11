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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/boletos")
public class BoletoController {

    private final BoletoParserUseCase boletoParserUseCase;
    private final ValidarBoletoUseCase validarBoletoUseCase;

    @Autowired
    public BoletoController(BoletoParserUseCase boletoParserUseCase, ValidarBoletoUseCase validarBoletoUseCase) {
        this.boletoParserUseCase = boletoParserUseCase;
        this.validarBoletoUseCase = validarBoletoUseCase;
    }

    @PostMapping("/validar")
    public ResponseEntity<BoletoDto> validarBoleto(@RequestBody String linhaDigitavel) {
        try {
            Boleto boleto = boletoParserUseCase.execute(linhaDigitavel);
            return ResponseEntity.ok(toDto(boleto));
        } catch (ValidacaoException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoletoDto> buscarPorId(@PathVariable Long id) {
        // TODO: Implementar busca por ID quando o use case estiver disponível
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<BoletoDto>> listarTodos() {
        // TODO: Implementar listagem quando o use case estiver disponível
        return ResponseEntity.ok(List.of());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        // TODO: Implementar deleção quando o use case estiver disponível
        return ResponseEntity.noContent().build();
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
