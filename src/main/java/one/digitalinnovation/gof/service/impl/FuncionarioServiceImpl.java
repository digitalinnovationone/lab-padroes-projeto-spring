package one.digitalinnovation.gof.service.impl;

import java.util.Optional;

import one.digitalinnovation.gof.model.Funcionario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import one.digitalinnovation.gof.model.FuncionarioRepository;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.EnderecoRepository;
import one.digitalinnovation.gof.service.FuncionarioService;
import one.digitalinnovation.gof.service.ViaCepService;

/**
 * Implementação da <b>Strategy</b> {@link FuncionarioService}, a qual pode ser
 * injetada pelo Spring (via {@link Autowired}). Com isso, como essa classe é um
 * {@link Service}, ela será tratada como um <b>Singleton</b>.
 * 
 * @author falvojr
 */
@Service
public class FuncionarioServiceImpl implements FuncionarioService {

	// Singleton: Injetar os componentes do Spring com @Autowired.
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private ViaCepService viaCepService;
	
	// Strategy: Implementar os métodos definidos na interface.
	// Facade: Abstrair integrações com subsistemas, provendo uma interface simples.

	@Override

	public Iterable<Funcionario> buscarTodos() {
		// Buscar todos os Clientes.
		return funcionarioRepository.findAll();
	}

	@Override
	public Funcionario buscarPorId(Long id) {
		// Buscar Funcionario por ID.
		Optional<Funcionario> funcionario = funcionarioRepository.findById(id);
		return funcionario.get();
	}

	@Override
	public Funcionario buscaPorCpf(String cpf) {
	// Buscar Funcionario por CPF.
		Optional<Funcionario> funcionario = funcionarioRepository.findByCpf(cpf);
		return funcionario.get();
	}


	@Override
	public void inserir(Funcionario funcionario) {
		salvarClienteComCep(funcionario);
	}

	@Override
	public void atualizar(Long id, Funcionario funcionario) {
		// Buscar Funcionario por ID, caso exista:
		Optional<Funcionario> funcionarioBd = funcionarioRepository.findById(id);
		if (funcionarioBd.isPresent()) {
			salvarClienteComCep(funcionario);
		}
	}

	@Override
	public void deletar(Long id) {
		// Deletar Funcionario por ID.
		funcionarioRepository.deleteById(id);
	}

	private void salvarClienteComCep(Funcionario funcionario) {
		// Verificar se o Endereco do Funcionario já existe (pelo CEP).
		String cep = funcionario.getEndereco().getCep();
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
			// Caso não exista, integrar com o ViaCEP e persistir o retorno.
			Endereco novoEndereco = viaCepService.consultarCep(cep);
			enderecoRepository.save(novoEndereco);
			return novoEndereco;
		});
		funcionario.setEndereco(endereco);
		// Inserir Funcionario, vinculando o Endereco (novo ou existente).
		funcionarioRepository.save(funcionario);
	}

}
