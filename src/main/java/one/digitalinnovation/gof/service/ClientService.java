package one.digitalinnovation.gof.service;

import one.digitalinnovation.gof.model.Client;

/**
 * Interface que define o padrão <b>Strategy</b> no domínio de cliente. Com
 * isso, se necessário, podemos ter multiplas implementações dessa mesma
 * interface.
 * 
 * @author falvojr
 * @author edielson-assis
 */
public interface ClientService {

	Iterable<Client> findAll();

	Client findById(Long id);

	void insert(Client cliente);

	void update(Long id, Client cliente);

	void delete(Long id);
}