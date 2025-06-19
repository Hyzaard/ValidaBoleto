package com.anhembi.ValidaBoleto.adapters;

import com.anhembi.ValidaBoleto.core.entities.Boleto;

import java.util.List;
import java.util.Optional;

public interface BoletoGateway {
    Boleto salvar(Boleto boleto);
    Boleto salvar(Boleto boleto, Long usuarioId);
    Optional<Boleto> buscarPorId(Long id);
    List<Boleto> listarTodos();
    void deletar(Long id);
    Boleto atualizar(Long id, Boleto boleto);
}
