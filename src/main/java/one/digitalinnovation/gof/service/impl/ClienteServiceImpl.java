package one.digitalinnovation.gof.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import one.digitalinnovation.gof.dto.ClienteDto;
import one.digitalinnovation.gof.exception.NotFoundException;
import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.repository.ClienteRepository;
import one.digitalinnovation.gof.repository.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteMapper;
import one.digitalinnovation.gof.service.ClienteService;
import one.digitalinnovation.gof.service.ViaCepService;

/**
 * Implementação da <b>Strategy</b> {@link ClienteService}. Como essa classe é
 * um {@link Service}, o Spring a trata como um <b>Singleton</b> e injeta suas
 * dependências via construtor (padrão atual recomendado).
 * 
 * @author falvojr
 */
@Service
public class ClienteServiceImpl implements ClienteService {

	// Singleton: dependências injetadas pelo Spring via construtor.
	private final ClienteRepository clienteRepository;
	private final EnderecoRepository enderecoRepository;
	private final ViaCepService viaCepService;
	private final ClienteMapper mapper;

	public ClienteServiceImpl(ClienteRepository clienteRepository, EnderecoRepository enderecoRepository,
			ViaCepService viaCepService, ClienteMapper mapper) {
		this.clienteRepository = clienteRepository;
		this.enderecoRepository = enderecoRepository;
		this.viaCepService = viaCepService;
		this.mapper = mapper;
	}

	// Strategy: implementar os métodos definidos na interface.
	// Facade: abstrair integrações com subsistemas, provendo uma interface simples.

	@Override
	public List<ClienteDto> buscarTodos() {
		return clienteRepository.findAll().stream()
				.map(mapper::toDto)
				.toList();
	}

	@Override
	public ClienteDto buscarPorId(Long id) {
		return mapper.toDto(buscarCliente(id));
	}

	@Override
	@Transactional
	public ClienteDto inserir(ClienteDto cliente) {
		return mapper.toDto(salvarClienteComCep(mapper.toEntity(cliente)));
	}

	@Override
	@Transactional
	public ClienteDto atualizar(Long id, ClienteDto cliente) {
		// Buscar Cliente por ID, caso exista (senão, HTTP 404):
		Cliente clienteParaAtualizar = buscarCliente(id);
		// Atualizar os campos com os dados recebidos na requisição.
		clienteParaAtualizar.setNome(cliente.nome());
		clienteParaAtualizar.setEndereco(mapper.toEntity(cliente.endereco()));
		// Chamar o método que contém a lógica de endereço e salvamento.
		return mapper.toDto(salvarClienteComCep(clienteParaAtualizar));
	}

	@Override
	@Transactional
	public void deletar(Long id) {
		// Garantir HTTP 404 caso o Cliente não exista.
		buscarCliente(id);
		clienteRepository.deleteById(id);
	}

	private Cliente buscarCliente(Long id) {
		return clienteRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Cliente não encontrado com id: " + id));
	}

	private Cliente salvarClienteComCep(Cliente cliente) {
		// Validar presença do Endereco e do CEP (evita NullPointerException).
		if (cliente.getEndereco() == null || cliente.getEndereco().getCep() == null
				|| cliente.getEndereco().getCep().isBlank()) {
			throw new IllegalArgumentException("CEP é obrigatório para cadastrar o endereço.");
		}
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
		return clienteRepository.save(cliente);
	}

}