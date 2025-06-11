package com.anhembi.ValidaBoleto.infrastructure.util;

public final class DigitoVerificador {

    private DigitoVerificador() {
        // Construtor privado para impedir instanciação
    }

    public static int calcularMod10(String dados) {
        int soma = 0;
        int multiplicador = 2;

        for (int i = dados.length() - 1; i >= 0; i--) {
            int produto = Character.getNumericValue(dados.charAt(i)) * multiplicador;
            soma += (produto > 9) ? (produto / 10) + (produto % 10) : produto;
            multiplicador = (multiplicador == 2) ? 1 : 2;
        }

        int resto = soma % 10;
        int dv = 10 - resto;

        return (dv == 10) ? 0 : dv;
    }

    public static int calcularMod11(String dados) {
        int soma = 0;
        int multiplicador = 2;

        for (int i = dados.length() - 1; i >= 0; i--) {
            soma += Character.getNumericValue(dados.charAt(i)) * multiplicador;
            multiplicador = (multiplicador == 9) ? 2 : multiplicador + 1;
        }

        int resto = soma % 11;
        int dv = 11 - resto;

        if (dv == 0 || dv == 10 || dv == 11) {
            return 1;
        }

        return dv;
    }
}
