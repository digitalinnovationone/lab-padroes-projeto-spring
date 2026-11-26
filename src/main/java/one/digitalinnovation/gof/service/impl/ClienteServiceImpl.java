package one.digitalinnovation.gof.service.impl;

import feign.FeignException;
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

	// Singleton gerenciado pelo Spring; constructor injection torna as dependências explícitas e facilita testes.
	private final ClienteRepository clienteRepository;
	private final EnderecoRepository enderecoRepository;
	private final ViaCepService viaCepService;

	public ClienteServiceImpl(ClienteRepository clienteRepository,
			EnderecoRepository enderecoRepository, ViaCepService viaCepService) {
		this.clienteRepository = clienteRepository;
		this.enderecoRepository = enderecoRepository;
		this.viaCepService = viaCepService;
	}
	
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
	public Cliente inserir(Cliente cliente) {
		if (cliente.getId() != null && clienteRepository.existsById(cliente.getId())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"Cliente já existe, verifique e tente novamente");
		}
		// POST cria um novo recurso: o ID deve ser gerado pela estratégia JPA.
		cliente.setId(null);
		return salvarClienteComCep(cliente);
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

	private Cliente salvarClienteComCep(Cliente cliente) {
		// Verificar se o Endereco do Cliente já existe (pelo CEP).
		// Normaliza CEP com ou sem hífen para manter uma única chave no banco.
		String cepInformado = cliente.getEndereco().getCep();
		String cep = cepInformado.replace("-", "");
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
			// Caso não exista, integrar com o ViaCEP e persistir o retorno.
			Endereco novoEndereco;
			try {
				novoEndereco = viaCepService.consultarCep(cep);
			} catch (FeignException exception) {
				throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
						"Não foi possível consultar o CEP no ViaCEP");
			}
			if (novoEndereco == null || novoEndereco.isErro()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CEP não encontrado");
			}
			novoEndereco.setCep(cep);
			return enderecoRepository.save(novoEndereco);
		});
		cliente.setEndereco(endereco);
		// Inserir Cliente, vinculando o Endereco (novo ou existente).
		return clienteRepository.save(cliente);
	}

}
