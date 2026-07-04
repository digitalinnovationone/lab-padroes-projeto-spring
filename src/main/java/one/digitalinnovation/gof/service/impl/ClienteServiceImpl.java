package one.digitalinnovation.gof.service.impl;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import one.digitalinnovation.gof.event.ClienteCadastradoEvent;
import one.digitalinnovation.gof.exception.RecursoNaoEncontradoException;
import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteService;
import one.digitalinnovation.gof.service.ViaCepService;
import one.digitalinnovation.gof.validation.CadeiaValidacaoCliente;

/**
 * Implementação da <b>Strategy</b> {@link ClienteService}. Como é um
 * {@link Service} do Spring, atua como <b>Singleton</b> e como <b>Facade</b> das
 * integrações (H2 e ViaCEP). Aplica a <b>Chain of Responsibility</b> de
 * validação antes de persistir e publica um evento (<b>Observer</b>) após o
 * cadastro.
 */
@Service
public class ClienteServiceImpl implements ClienteService {

	private final ClienteRepository clienteRepository;
	private final EnderecoRepository enderecoRepository;
	private final ViaCepService viaCepService;
	private final CadeiaValidacaoCliente cadeiaValidacao;
	private final ApplicationEventPublisher eventPublisher;

	public ClienteServiceImpl(ClienteRepository clienteRepository, EnderecoRepository enderecoRepository,
			ViaCepService viaCepService, CadeiaValidacaoCliente cadeiaValidacao,
			ApplicationEventPublisher eventPublisher) {
		this.clienteRepository = clienteRepository;
		this.enderecoRepository = enderecoRepository;
		this.viaCepService = viaCepService;
		this.cadeiaValidacao = cadeiaValidacao;
		this.eventPublisher = eventPublisher;
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<Cliente> buscarTodos() {
		return clienteRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente buscarPorId(Long id) {
		return clienteRepository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado para o id " + id + "."));
	}

	@Override
	@Transactional
	public void inserir(Cliente cliente) {
		cadeiaValidacao.validar(cliente);
		salvarClienteComCep(cliente);
		eventPublisher.publishEvent(new ClienteCadastradoEvent(this, cliente));
	}

	@Override
	@Transactional
	public void atualizar(Long id, Cliente cliente) {
		Cliente existente = buscarPorId(id);
		cadeiaValidacao.validar(cliente);
		cliente.setId(existente.getId());
		salvarClienteComCep(cliente);
	}

	@Override
	@Transactional
	public void deletar(Long id) {
		buscarPorId(id);
		clienteRepository.deleteById(id);
	}

	private void salvarClienteComCep(Cliente cliente) {
		String cep = cliente.getEndereco().getCep();
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
			Endereco novoEndereco = viaCepService.consultarCep(cep);
			return enderecoRepository.save(novoEndereco);
		});
		cliente.setEndereco(endereco);
		clienteRepository.save(cliente);
	}
}
