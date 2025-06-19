package com.anhembi.ValidaBoleto.infrastructure.persistence;

import com.anhembi.ValidaBoleto.adapters.BoletoGateway;
import com.anhembi.ValidaBoleto.core.entities.Boleto;
import com.anhembi.ValidaBoleto.infrastructure.persistence.mappers.BoletoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoletoRepository implements BoletoGateway {
    
    private final JpaBoletoRepository jpaRepository;
    private final JpaUsuarioRepository jpaUsuarioRepository;
    private final BoletoMapper boletoMapper;

    @Override
    public Boleto salvar(Boleto boleto) {
        BoletoEntity entity = boletoMapper.toEntity(boleto);
        entity = jpaRepository.save(entity);
        return boletoMapper.toDomain(entity);
    }

    public Boleto salvar(Boleto boleto, Long usuarioId) {
        // Buscar a entidade do usuário
        var usuarioEntity = jpaUsuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        // Criar a entidade do boleto com o usuário
        BoletoEntity entity = boletoMapper.toEntity(boleto, usuarioEntity);
        entity = jpaRepository.save(entity);
        return boletoMapper.toDomain(entity);
    }

    @Override
    public Optional<Boleto> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(boletoMapper::toDomain);
    }

    @Override
    public List<Boleto> listarTodos() {
        return jpaRepository.findAll().stream()
                .map(boletoMapper::toDomain)
                .toList();
    }

    @Override
    public void deletar(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Boleto atualizar(Long id, Boleto boleto) {
        if (jpaRepository.existsById(id)) {
            BoletoEntity entity = boletoMapper.toEntity(boleto);
            entity.setId(id);
            entity = jpaRepository.save(entity);
            return boletoMapper.toDomain(entity);
        }
        throw new RuntimeException("Boleto não encontrado com o ID: " + id);
    }

    public Optional<BoletoEntity> findByCodigoDeBarra(String codigoDeBarra) {
        return jpaRepository.findByCodigoDeBarra(codigoDeBarra);
    }

    public Optional<BoletoEntity> findByLinhaDigitavel(String linhaDigitavel) {
        return jpaRepository.findByLinhaDigitavel(linhaDigitavel);
    }
}
