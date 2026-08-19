package one.digitalinnovation.gof.service.impl;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import one.digitalinnovation.gof.dto.ClienteRequestDTO;
import one.digitalinnovation.gof.dto.ClienteResponseDTO;
import one.digitalinnovation.gof.exception.RecursoNaoEncontradoException;
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

	// Singleton: Componentes injetados via construtor (prática recomendada do Spring).
	private final ClienteRepository clienteRepository;
	private final EnderecoRepository enderecoRepository;
	private final ViaCepService viaCepService;

	public ClienteServiceImpl(ClienteRepository clienteRepository,
	                          EnderecoRepository enderecoRepository,
	                          ViaCepService viaCepService) {
		this.clienteRepository = clienteRepository;
		this.enderecoRepository = enderecoRepository;
		this.viaCepService = viaCepService;
	}
	
	// Strategy: Implementar os métodos definidos na interface.
	// Facade: Abstrair integrações com subsistemas, provendo uma interface simples.

	@Override
	public Iterable<ClienteResponseDTO> buscarTodos() {
		// Buscar todos os Clientes convertendo para DTO.
		return StreamSupport.stream(clienteRepository.findAll().spliterator(), false)
				.map(ClienteResponseDTO::new)
				.collect(Collectors.toList());
	}

	@Override
	public ClienteResponseDTO buscarPorId(Long id) {
		// Buscar Cliente por ID e converter para DTO.
		Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> 
			new RecursoNaoEncontradoException("Cliente não encontrado para o ID: " + id));
		return new ClienteResponseDTO(cliente);
	}

	@Override
	public ClienteResponseDTO inserir(ClienteRequestDTO clienteDTO) {
		Cliente cliente = salvarClienteComCep(new Cliente(), clienteDTO);
		return new ClienteResponseDTO(cliente);
	}

	@Override
	public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO clienteDTO) {
		// Buscar Cliente por ID, caso exista:
		Cliente clienteBd = clienteRepository.findById(id).orElseThrow(() -> 
			new RecursoNaoEncontradoException("Cliente não encontrado para o ID: " + id));
		Cliente clienteAtualizado = salvarClienteComCep(clienteBd, clienteDTO);
		return new ClienteResponseDTO(clienteAtualizado);
	}

	@Override
	public void deletar(Long id) {
		// Deletar Cliente por ID se existir.
		if (!clienteRepository.existsById(id)) {
			throw new RecursoNaoEncontradoException("Cliente não encontrado para o ID: " + id);
		}
		clienteRepository.deleteById(id);
	}

	private Cliente salvarClienteComCep(Cliente cliente, ClienteRequestDTO clienteDTO) {
		if (clienteDTO != null) {
			cliente.setNome(clienteDTO.getNome());
			// Verificar se o Endereco do Cliente já existe (pelo CEP).
			String cep = clienteDTO.getCep();
			if (cep != null && !cep.trim().isEmpty()) {
				Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
					// Caso não exista, integrar com o ViaCEP e persistir o retorno.
					Endereco novoEndereco = viaCepService.consultarCep(cep);
					if (novoEndereco != null && novoEndereco.getCep() != null) {
						enderecoRepository.save(novoEndereco);
					}
					return novoEndereco;
				});
				cliente.setEndereco(endereco);
			}
		}
		// Inserir/Atualizar Cliente, vinculando o Endereco (novo ou existente).
		return clienteRepository.save(cliente);
	}

}
