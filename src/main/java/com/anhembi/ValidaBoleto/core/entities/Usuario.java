package com.anhembi.ValidaBoleto.core.entities;

public record Usuario(Long id,
                      String name,
                      String email,
                      Long boletoId
) {}
