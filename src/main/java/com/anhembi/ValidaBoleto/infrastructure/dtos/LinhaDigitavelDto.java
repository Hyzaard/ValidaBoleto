package com.anhembi.ValidaBoleto.infrastructure.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinhaDigitavelDto {
    private String linhaDigitavel;
    
    @Override
    public String toString() {
        return "LinhaDigitavelDto{" +
                "linhaDigitavel='" + linhaDigitavel + '\'' +
                '}';
    }
    
    // Método para validar se o DTO é válido
    public boolean isValid() {
        return linhaDigitavel != null && !linhaDigitavel.trim().isEmpty();
    }
} 