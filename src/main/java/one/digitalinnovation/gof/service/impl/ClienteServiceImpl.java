package one.digitalinnovation.gof.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteService;
import one.digitalinnovation.gof.service.strategy.CepStrategyFactory;

/**
 * Implementação legada mantida para retrocompatibilidade com a interface {@link ClienteService}.
 */
@Service
public class ClienteServiceImpl implements ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private CepStrategyFactory cepStrategyFactory;

	@Override
	public Iterable<Cliente> buscarTodos() {
		return clienteRepository.findAll();
	}

	@Override
	public Cliente buscarPorId(Long id) {
		Optional<Cliente> cliente = clienteRepository.findById(id);
		return cliente.orElse(null);
	}

	@Override
	public void inserir(Cliente cliente) {
		salvarClienteComCep(cliente);
	}

	@Override
	public void atualizar(Long id, Cliente cliente) {
		Optional<Cliente> clienteBd = clienteRepository.findById(id);
		if (clienteBd.isPresent()) {
			cliente.setId(id);
			salvarClienteComCep(cliente);
		}
	}

	@Override
	public void deletar(Long id) {
		clienteRepository.deleteById(id);
	}

	private void salvarClienteComCep(Cliente cliente) {
		if (cliente.getEndereco() != null && cliente.getEndereco().getCep() != null) {
			String cep = cliente.getEndereco().getCep().replaceAll("\\D", "");
			Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
				Endereco novoEndereco = cepStrategyFactory.getStrategy(cliente.getCepStrategyUtilizada()).consultarCep(cep);
				if (novoEndereco != null) {
					enderecoRepository.save(novoEndereco);
				}
				return novoEndereco;
			});
			cliente.setEndereco(endereco);
		}
		clienteRepository.save(cliente);
	}
}
