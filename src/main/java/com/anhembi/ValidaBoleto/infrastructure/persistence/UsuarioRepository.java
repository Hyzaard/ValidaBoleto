package com.anhembi.ValidaBoleto.infrastructure.persistence;

<<<<<<< HEAD
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
} 
=======
public class UsuarioRepository {
}
>>>>>>> 69de5dba08673a97a8de5b414ec7e2097d96eba3
