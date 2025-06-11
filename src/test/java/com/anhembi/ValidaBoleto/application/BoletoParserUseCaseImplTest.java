package com.anhembi.ValidaBoleto.application;

import com.anhembi.ValidaBoleto.adapters.BoletoParserGateway;
import com.anhembi.ValidaBoleto.core.entities.Boleto;
import com.anhembi.ValidaBoleto.infrastructure.exception.ValidacaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoletoParserUseCaseImplTest {

    @Mock
    private BoletoParserGateway boletoParserGateway;

    @InjectMocks
    private BoletoParserUseCaseImpl boletoParserUseCase;

    private String linhaDigitavelValida;

    @BeforeEach
    void setUp() {
        // Linha digitável válida de exemplo (47 dígitos)
        linhaDigitavelValida = "34191.12345 67890.101112 13141.516171 8 12345678901234";
    }

    @Test
    void execute_DeveProcessarLinhaDigitavelComSucesso() throws ValidacaoException {
        Boleto boletoEsperado = Boleto.builder()
                .linhaDigitavel(linhaDigitavelValida)
                .codigoDeBarra("341911234567890101112131415161712345678901234")
                .bancoEmissor("341")
                .valor(new BigDecimal("123.45"))
                .dataVencimento(LocalDate.now().plusDays(10))
                .build();

        when(boletoParserGateway.execute(any())).thenReturn(boletoEsperado);

        Boleto resultado = boletoParserUseCase.execute(linhaDigitavelValida);

        assertNotNull(resultado);
        assertEquals(boletoEsperado.getLinhaDigitavel(), resultado.getLinhaDigitavel());
        assertEquals(boletoEsperado.getCodigoDeBarra(), resultado.getCodigoDeBarra());
        assertEquals(boletoEsperado.getBancoEmissor(), resultado.getBancoEmissor());
        assertEquals(boletoEsperado.getValor(), resultado.getValor());
        assertEquals(boletoEsperado.getDataVencimento(), resultado.getDataVencimento());
    }

    @Test
    void execute_DeveLancarExcecaoQuandoLinhaDigitavelInvalida() {
        String linhaDigitavelInvalida = "12345"; // Linha digitável muito curta

        assertThrows(ValidacaoException.class, () -> boletoParserUseCase.execute(linhaDigitavelInvalida));
        verify(boletoParserGateway, never()).execute(any());
    }

    @Test
    void execute_DeveLancarExcecaoQuandoDigitoVerificadorInvalido() {
        // Linha digitável com dígito verificador inválido
        String linhaDigitavelDigitoInvalido = "34191.12345 67890.101112 13141.516171 9 12345678901234";

        assertThrows(ValidacaoException.class, () -> boletoParserUseCase.execute(linhaDigitavelDigitoInvalido));
        verify(boletoParserGateway, never()).execute(any());
    }

    @Test
    void execute_DeveLancarExcecaoQuandoCodigoDeBarrasInvalido() {
        // Linha digitável que gera código de barras inválido
        String linhaDigitavelCodigoInvalido = "34191.12345 67890.101112 13141.516171 8 12345678901235";

        assertThrows(ValidacaoException.class, () -> boletoParserUseCase.execute(linhaDigitavelCodigoInvalido));
        verify(boletoParserGateway, never()).execute(any());
    }
} 