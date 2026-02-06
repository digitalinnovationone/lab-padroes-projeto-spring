package one.digitalinnovation.gof.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import one.digitalinnovation.gof.event.ClienteEvent;
import one.digitalinnovation.gof.event.ClienteEvent.TipoOperacao;
import one.digitalinnovation.gof.exception.CepInvalidoException;
import one.digitalinnovation.gof.exception.ClienteNotFoundException;
import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteService;
import one.digitalinnovation.gof.service.ViaCepService;

/**
 * Implementacao da <b>Strategy</b> {@link ClienteService}, a qual pode ser
 * injetada pelo Spring (via {@link Autowired}). Com isso, como essa classe e um
 * {@link Service}, ela sera tratada como um <b>Singleton</b>.
 *
 * Tambem utiliza o padrao <b>Observer</b> via Spring Events para notificar
 * listeners sobre operacoes realizadas em clientes.
 *
 * @author falvojr
 */
@Service
public class ClienteServiceImpl implements ClienteService {

	private static final Logger logger = LoggerFactory.getLogger(ClienteServiceImpl.class);

	// Singleton: Injetar os componentes do Spring com @Autowired.
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private ViaCepService viaCepService;
	@Autowired
	private ApplicationEventPublisher eventPublisher;

	// Strategy: Implementar os metodos definidos na interface.
	// Facade: Abstrair integracoes com subsistemas, provendo uma interface simples.

	@Override
	public Iterable<Cliente> buscarTodos() {
		logger.debug("Buscando todos os clientes");
		return clienteRepository.findAll();
	}

	@Override
	public Cliente buscarPorId(Long id) {
		logger.debug("Buscando cliente por ID: {}", id);
		return clienteRepository.findById(id)
				.orElseThrow(() -> new ClienteNotFoundException(id));
	}

	@Override
	public void inserir(Cliente cliente) {
		logger.info("Inserindo novo cliente: {}", cliente.getNome());
		salvarClienteComCep(cliente);
		// Observer: Publicar evento de cliente criado.
		eventPublisher.publishEvent(new ClienteEvent(this, cliente, TipoOperacao.CRIADO));
	}

	@Override
	public void atualizar(Long id, Cliente cliente) {
		logger.info("Atualizando cliente com ID: {}", id);
		// Verificar se o cliente existe, caso contrario lancar excecao.
		clienteRepository.findById(id)
				.orElseThrow(() -> new ClienteNotFoundException(id));
		cliente.setId(id);
		salvarClienteComCep(cliente);
		// Observer: Publicar evento de cliente atualizado.
		eventPublisher.publishEvent(new ClienteEvent(this, cliente, TipoOperacao.ATUALIZADO));
	}

	@Override
	public void deletar(Long id) {
		logger.info("Deletando cliente com ID: {}", id);
		Cliente cliente = clienteRepository.findById(id)
				.orElseThrow(() -> new ClienteNotFoundException(id));
		clienteRepository.deleteById(id);
		// Observer: Publicar evento de cliente deletado.
		eventPublisher.publishEvent(new ClienteEvent(this, cliente, TipoOperacao.DELETADO));
	}

	private void salvarClienteComCep(Cliente cliente) {
		// Verificar se o Endereco do Cliente ja existe (pelo CEP).
		String cep = cliente.getEndereco().getCep();
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
			// Caso nao exista, integrar com o ViaCEP e persistir o retorno.
			Endereco novoEndereco = viaCepService.consultarCep(cep);
			if (novoEndereco == null || novoEndereco.getCep() == null) {
				throw new CepInvalidoException(cep);
			}
			enderecoRepository.save(novoEndereco);
			return novoEndereco;
		});
		cliente.setEndereco(endereco);
		// Inserir Cliente, vinculando o Endereco (novo ou existente).
		clienteRepository.save(cliente);
	}

}
