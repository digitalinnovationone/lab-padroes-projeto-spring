package one.digitalinnovation.gof.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import one.digitalinnovation.gof.model.Produto;

@Repository
public interface ProdutoRepository extends CrudRepository<Produto, Long>{
	
}