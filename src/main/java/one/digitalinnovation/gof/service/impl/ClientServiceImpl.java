package one.digitalinnovation.gof.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import one.digitalinnovation.gof.model.Address;
import one.digitalinnovation.gof.model.Client;
import one.digitalinnovation.gof.repository.AddressRepository;
import one.digitalinnovation.gof.repository.ClientRepository;
import one.digitalinnovation.gof.service.ClientService;
import one.digitalinnovation.gof.service.ViaCepService;
import one.digitalinnovation.gof.service.exceptions.DatabaseException;
import one.digitalinnovation.gof.service.exceptions.ResourceNotFoundException;

/**
 * Implementação da <b>Strategy</b> {@link ClientService}, a qual pode ser
 * injetada pelo Spring (via {@link Autowired}). Com isso, como essa classe é um
 * {@link Service}, ela será tratada como um <b>Singleton</b>.
 * 
 * @author falvojr
 * @author edielson-assis
 */
@Service
public class ClientServiceImpl implements ClientService {

	// Singleton: Injetar os componentes do Spring com @Autowired.

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private ViaCepService viaCepService;

	// Strategy: Implementar os métodos definidos na interface.
	// Facade: Abstrair integrações com subsistemas, provendo uma interface simples.

	/**
	 * Buscar todos os Clientes.
	 * 
	 * @return Iterable<Client>
	 */
	@Override
	public Iterable<Client> findAll() {
		return clientRepository.findAll();
	}

	/**
	 * Buscar cliente por ID.
	 * 
	 * @param id
	 * @return client
	 */
	@Override
	public Client findById(Long id) {
		Optional<Client> client = clientRepository.findById(id);
		return client.orElseThrow(() -> new ResourceNotFoundException(id));
	}

	/**
	 * Inserir um cliente.
	 * 
	 * @param client
	 */
	@Override
	public void insert(Client client) {
		saveClientWithCep(client);
	}

	/**
	 * Atualizar um cliente por ID.
	 * 
	 * @param id
	 * @param client
	 */
	@Override
	public void update(Long id, Client client) {
		Optional<Client> clientBd = clientRepository.findById(id);
		if (clientBd.isPresent()) {
			saveClientWithCep(client);
		}
	}

	/**
	 * Deletar um cliente por ID.
	 * 
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		try {
			clientRepository.deleteById(id);
		} 
		catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		} 
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage());
		}
	}

	/**
	 * Verificar se o endreço do cliente já existe (pelo CEP).
	 * 
	 * @param client
	 */
	private void saveClientWithCep(Client client) {
		String cep = client.getAddress().getCep();
		Address address = addressRepository.findById(cep).orElseGet(() -> {
			// Caso não exista, integrar com o ViaCEP e persistir o retorno.
			Address newaddress = viaCepService.searchAddressByZipCode(cep);
			addressRepository.save(newaddress);
			return newaddress;
		});
		client.setAddress(address);
		// Inserir cliente, vinculando o endereço (novo ou existente).
		clientRepository.save(client);
	}
}