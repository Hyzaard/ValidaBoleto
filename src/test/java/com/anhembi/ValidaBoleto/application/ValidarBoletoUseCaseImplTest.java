package com.anhembi.ValidaBoleto.application;

import com.anhembi.ValidaBoleto.adapters.BoletoGateway;
import com.anhembi.ValidaBoleto.core.entities.Boleto;
import com.anhembi.ValidaBoleto.core.enuns.StatusValidacao;
import com.anhembi.ValidaBoleto.infrastructure.dtos.ResultadoValidacaoDto;
import com.anhembi.ValidaBoleto.infrastructure.exception.ValidacaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidarBoletoUseCaseImplTest {

    @Mock
    private BoletoGateway boletoGateway;

    @InjectMocks
    private ValidarBoletoUseCaseImpl validarBoletoUseCase;

    private String linhaDigitavelValida;
    private Boleto boletoValido;

    @BeforeEach
    void setUp() {
        linhaDigitavelValida = "34191.12345 67890.101112 13141.516171 8 12345678901234";
        
        boletoValido = Boleto.builder()
                .linhaDigitavel(linhaDigitavelValida)
                .codigoDeBarra("341911234567890101112131415161712345678901234")
                .nomeBeneficiario("Banco Itaú S.A.")
                .bancoEmissor("341")
                .valor(new BigDecimal("123.45"))
                .dataVencimento(LocalDate.now().plusDays(10))
                .build();
    }

    @Test
    void execute_DeveValidarBoletoComSucesso() throws ValidacaoException {
        when(boletoGateway.listarTodos()).thenReturn(java.util.Collections.singletonList(boletoValido));

        ResultadoValidacaoDto resultado = validarBoletoUseCase.execute(linhaDigitavelValida);

        assertNotNull(resultado);
        assertEquals(StatusValidacao.VALIDO, resultado.getStatus());
        assertTrue(resultado.getErros().isEmpty());
        assertTrue(resultado.getAvisos().isEmpty());
    }

    @Test
    void execute_DeveRetornarErroQuandoLinhaDigitavelInvalida() {
        String linhaDigitavelInvalida = "12345";

        assertThrows(ValidacaoException.class, () -> validarBoletoUseCase.execute(linhaDigitavelInvalida));
    }

    @Test
    void execute_DeveRetornarErroQuandoBoletoVencido() throws ValidacaoException {
        Boleto boletoVencido = boletoValido.toBuilder()
                .dataVencimento(LocalDate.now().minusDays(1))
                .build();

        when(boletoGateway.listarTodos()).thenReturn(java.util.Collections.singletonList(boletoVencido));

        ResultadoValidacaoDto resultado = validarBoletoUseCase.execute(linhaDigitavelValida);

        assertNotNull(resultado);
        assertEquals(StatusValidacao.SUSPEITO, resultado.getStatus());
        assertTrue(resultado.getAvisos().contains("Boleto vencido"));
    }

    @Test
    void execute_DeveRetornarErroQuandoValorInvalido() throws ValidacaoException {
        Boleto boletoValorInvalido = boletoValido.toBuilder()
                .valor(BigDecimal.ZERO)
                .build();

        when(boletoGateway.listarTodos()).thenReturn(java.util.Collections.singletonList(boletoValorInvalido));

        ResultadoValidacaoDto resultado = validarBoletoUseCase.execute(linhaDigitavelValida);

        assertNotNull(resultado);
        assertEquals(StatusValidacao.FRAUDULENTO, resultado.getStatus());
        assertTrue(resultado.getErros().contains("Valor deve ser maior que zero"));
    }

    @Test
    void execute_DeveRetornarErroQuandoBeneficiarioInvalido() throws ValidacaoException {
        Boleto boletoBeneficiarioInvalido = boletoValido.toBuilder()
                .nomeBeneficiario("AB")
                .build();

        when(boletoGateway.listarTodos()).thenReturn(java.util.Collections.singletonList(boletoBeneficiarioInvalido));

        ResultadoValidacaoDto resultado = validarBoletoUseCase.execute(linhaDigitavelValida);

        assertNotNull(resultado);
        assertEquals(StatusValidacao.FRAUDULENTO, resultado.getStatus());
        assertTrue(resultado.getErros().contains("Nome do beneficiário deve ter pelo menos 3 caracteres"));
    }

    @Test
    void execute_DeveRetornarErroQuandoBancoEmissorInvalido() throws ValidacaoException {
        Boleto boletoBancoInvalido = boletoValido.toBuilder()
                .bancoEmissor("999")
                .build();

        when(boletoGateway.listarTodos()).thenReturn(java.util.Collections.singletonList(boletoBancoInvalido));

        ResultadoValidacaoDto resultado = validarBoletoUseCase.execute(linhaDigitavelValida);

        assertNotNull(resultado);
        assertEquals(StatusValidacao.FRAUDULENTO, resultado.getStatus());
        assertTrue(resultado.getErros().contains("Banco emissor inválido"));
    }

    @Test
    void execute_DeveRetornarErroQuandoCodigoDeBarraInvalido() throws ValidacaoException {
        Boleto boletoCodigoInvalido = boletoValido.toBuilder()
                .codigoDeBarra("123")
                .build();

        when(boletoGateway.listarTodos()).thenReturn(java.util.Collections.singletonList(boletoCodigoInvalido));

        ResultadoValidacaoDto resultado = validarBoletoUseCase.execute(linhaDigitavelValida);

        assertNotNull(resultado);
        assertEquals(StatusValidacao.FRAUDULENTO, resultado.getStatus());
        assertTrue(resultado.getErros().contains("Código de barras inválido"));
    }

    @Test
    void execute_DeveRetornarErroQuandoCpfCnpjBeneficiarioInvalido() throws ValidacaoException {
        Boleto boletoCpfInvalido = boletoValido.toBuilder()
                .cpfCnpjBeneficiario("123")
                .build();

        when(boletoGateway.listarTodos()).thenReturn(java.util.Collections.singletonList(boletoCpfInvalido));

        ResultadoValidacaoDto resultado = validarBoletoUseCase.execute(linhaDigitavelValida);

        assertNotNull(resultado);
        assertEquals(StatusValidacao.FRAUDULENTO, resultado.getStatus());
        assertTrue(resultado.getErros().contains("CPF/CNPJ do beneficiário inválido"));
    }
} 