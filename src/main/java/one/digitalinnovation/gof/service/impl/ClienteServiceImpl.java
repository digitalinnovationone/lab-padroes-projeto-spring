package one.digitalinnovation.gof.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteService;
import one.digitalinnovation.gof.service.ViaCepService;

/**
 * Implementação da <b>Strategy</b> {@link ClienteService}, a qual pode ser
 * injetada pelo Spring (via {@link Autowired}). Com isso, como essa classe é um
 * {@link Service}, ela será tratada como um <b>Singleton</b>.
 * 
 * @author falvojr
 */
@Service
public class ClienteServiceImpl implements ClienteService {

	// Singleton: Injetar os componentes do Spring com @Autowired.
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private ViaCepService viaCepService;
	
	// Strategy: Implementar os métodos definidos na interface.
	// Facade: Abstrair integrações com subsistemas, provendo uma interface simples.

	@Override
	public Iterable<Cliente> buscarTodos() {
		// Buscar todos os Clientes.
		return clienteRepository.findAll();
	}

	@Override
	public Cliente buscarPorId(Long id) {
		// Buscar Cliente por ID.
		return clienteRepository.findById(id)
				.orElseThrow(() -> erroNaoEncontrado("Nao foi possivel localizar id, verifique e tente novamente"));
	}

	@Override
	public void inserir(Cliente cliente) {
		// Verificar se Cliente já existe.
		clienteRepository.findById(cliente.getId())
				.ifPresent(c -> {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
							"Cliente já existe, verifique e tente novamente");
		});
		salvarClienteComCep(cliente);
	}

	@Override
	public void atualizar(Long id, Cliente cliente) {
		// Buscar Cliente por ID, caso exista:
		clienteRepository.findById(id)
				.orElseThrow(() -> erroNaoEncontrado("Não foi possivel atualizar, verifique e tente novamente"));
		cliente.setId(id);
		salvarClienteComCep(cliente);
	}

	@Override
	public void deletar(Long id) {
		// Deletar Cliente por ID.
		if (!clienteRepository.existsById(id)) {
			throw erroNaoEncontrado("Nao foi possivel deletar, verifique e tente novamente");
		}
		clienteRepository.deleteById(id);
	}

	private ResponseStatusException erroNaoEncontrado(String mensagem) {
		return new ResponseStatusException(HttpStatus.NOT_FOUND, mensagem);
	}

	private void salvarClienteComCep(Cliente cliente) {
		// Verificar se o Endereco do Cliente já existe (pelo CEP).
		String cep = cliente.getEndereco().getCep();
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
			// Caso não exista, integrar com o ViaCEP e persistir o retorno.
			Endereco novoEndereco = viaCepService.consultarCep(cep);
			enderecoRepository.save(novoEndereco);
			return novoEndereco;
		});
		cliente.setEndereco(endereco);
		// Inserir Cliente, vinculando o Endereco (novo ou existente).
		clienteRepository.save(cliente);
	}

}
