package one.digitalinnovation.gof.model;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends CrudRepository<Cliente, Long> {
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
    Optional<Cliente> findByCpf(String cpf);
}