package one.digitalinnovation.gof.service.impl;

import java.util.Optional;

import one.digitalinnovation.gof.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	private PerfilRepository perfilRepository;
	@Autowired
	private ContatoRepository contatoRepository;
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
		Optional<Cliente> cliente = clienteRepository.findById(id);
		return cliente.get();
	}
	@Override
	public Iterable<Cliente> buscarPorNome(String nome) {
		// Buscar Clientes pelo nome.
		return clienteRepository.findByNomeContainingIgnoreCase(nome);
	}
	@Override
	public Iterable<Cliente> buscarPorCep(String cep) {
		return clienteRepository.findByEnderecoCep(cep);  // Certifique-se de que o repositório tem esse método
	}
	@Override
	public void inserir(Cliente cliente) {
		salvarClienteComCep(cliente);
	}

	@Override
	public void atualizar(Long id, Cliente cliente) {
		// Buscar Cliente por ID, caso exista:
		Optional<Cliente> clienteBd = clienteRepository.findById(id);
		if (clienteBd.isPresent()) {
			salvarClienteComCep(cliente);
		}
	}

	@Override
	public void deletar(Long id) {
		// Deletar Cliente por ID.
		clienteRepository.deleteById(id);
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
		// Inserir Cliente, vinculando o Endereco (novo ou existente).
		cliente.setEndereco(endereco);
		// Inserir Cliente, vinculando o Perfil.
		PerfilCliente perfil = cliente.getPerfil();
		perfilRepository.save(perfil);
		String perfilNome = cliente.getPerfil().getNome();
		Optional<PerfilCliente> perfilOptional = perfilRepository.findById(perfilNome);
		if (perfilOptional.isPresent()) {
			cliente.setPerfil(perfilOptional.get());
		} else {
			throw new RuntimeException("Perfil não encontrado: " + perfilNome);
		}
		// Inserir Cliente, vinculando aos contatos.
		Contato contato = cliente.getContato();
		contatoRepository.save(contato);
		Long contatoId = cliente.getContato().getId();
		Optional<Contato> contatoOptional = contatoRepository.findById(contatoId);
		if (contatoOptional.isPresent()) {
			cliente.setContato(contatoOptional.get());
		} else {
			throw new RuntimeException("Contatos não encontrados");
		}

		clienteRepository.save(cliente);
	}
}
