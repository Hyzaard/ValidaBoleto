package com.anhembi.ValidaBoleto.infrastructure.util;

public final class DigitoVerificador {

    private DigitoVerificador() {
        // Construtor privado para impedir instanciação
    }

    /**
     * Calcula o módulo 10 para validação de boletos bancários
     * @param bloco String contendo os dígitos para cálculo
     * @return int dígito verificador calculado
     */
    public static int modulo10(String bloco) {
        int soma = 0;
        int peso = 2;

        // Processa da esquerda para a direita
        for (int i = 0; i < bloco.length(); i++) {
            int digito = Character.getNumericValue(bloco.charAt(i));
            int produto = digito * peso;
            
            // Se o produto for maior que 9, soma os dígitos
            if (produto > 9) {
                produto = (produto / 10) + (produto % 10);
            }
            
            soma += produto;
            peso = (peso == 2) ? 1 : 2;
        }

        int resto = soma % 10;
        return (resto == 0) ? 0 : 10 - resto;
    }

    /**
     * Calcula o módulo 11 para validação de boletos bancários
     * @param bloco String contendo os dígitos para cálculo
     * @return int dígito verificador calculado
     */
    public static int modulo11Bancario(String bloco) {
        int soma = 0;
        int peso = 2;

        for (int i = bloco.length() - 1; i >= 0; i--) {
            int digito = Character.getNumericValue(bloco.charAt(i));
            soma += digito * peso;
            peso = (peso == 9) ? 2 : peso + 1;
        }

        int resto = soma % 11;
        int dv = 11 - resto;

        // Regras específicas do módulo 11 bancário
        if (dv == 0 || dv == 10 || dv == 11) {
            return 1;
        }
        return dv;
    }

    /**
     * Calcula o módulo 11 para validação de boletos de arrecadação
     * @param bloco String contendo os dígitos para cálculo
     * @return int dígito verificador calculado
     */
    public static int modulo11Arrecadacao(String bloco) {
        int soma = 0;
        int peso = 2;

        for (int i = bloco.length() - 1; i >= 0; i--) {
            int digito = Character.getNumericValue(bloco.charAt(i));
            soma += digito * peso;
            peso = (peso == 9) ? 2 : peso + 1;
        }

        int resto = soma % 11;
        
        // Regras específicas do módulo 11 de arrecadação
        if (resto == 0 || resto == 1) {
            return 0;
        }
        if (resto == 10) {
            return 1;
        }
        return 11 - resto;
    }
}
