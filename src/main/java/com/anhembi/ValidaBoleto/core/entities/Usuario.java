package com.anhembi.ValidaBoleto.core.domain;

public record Usuario(Long id,
                      String name,
                      String email,
                      Long boletoId
) {}
