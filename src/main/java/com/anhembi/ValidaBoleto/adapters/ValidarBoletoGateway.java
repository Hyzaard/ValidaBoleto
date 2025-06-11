package com.anhembi.ValidaBoleto.adapters;

import com.anhembi.ValidaBoleto.core.entities.Boleto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ValidarBoletoGateway {

    public Boleto execute(String linhaDigitavel, String nomeBeneficiario, String cpfCnpjBeneficiario);
}
