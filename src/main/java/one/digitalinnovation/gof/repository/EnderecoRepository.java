package one.digitalinnovation.gof.repository;

import one.digitalinnovation.gof.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, String> {

}