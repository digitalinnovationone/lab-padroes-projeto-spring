package one.digitalinnovation.gof.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.EnderecoRepository;
import one.digitalinnovation.gof.service.ViaCepService;
import one.digitalinnovation.gof.support.ClienteTestFactory;

class ClienteServiceImplTest {

	@Mock
	private ClienteRepository clienteRepository;

	@Mock
	private EnderecoRepository enderecoRepository;

	@Mock
	private ViaCepService viaCepService;

	private ClienteServiceImpl service;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		service = new ClienteServiceImpl(clienteRepository, enderecoRepository, viaCepService);
	}

	@Test
	void buscarPorIdDeveLancarNotFoundQuandoClienteNaoExiste() {
		when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

		ResponseStatusException exception = assertThrows(ResponseStatusException.class,
				() -> service.buscarPorId(99L));

		assertEquals(404, exception.getStatus().value());
	}

	@Test
	void inserirDeveConsultarNormalizarEPersistirCep() {
		// Gera dados diversos a cada execução, mas usa CEPs conhecidos e válidos.
		Cliente cliente = ClienteTestFactory.criarClienteAleatorio();
		String cepComHifen = cliente.getEndereco().getCep();
		String cepNormalizado = cepComHifen.replace("-", "");

		Endereco enderecoConsultado = new Endereco();
		enderecoConsultado.setLogradouro("Praça da Sé");

		when(enderecoRepository.findById(cepNormalizado)).thenReturn(Optional.empty());
		when(viaCepService.consultarCep(cepNormalizado)).thenReturn(enderecoConsultado);
		when(enderecoRepository.save(any(Endereco.class))).thenAnswer(invocation -> invocation.getArgument(0));
		when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> {
			Cliente salvo = invocation.getArgument(0);
			salvo.setId(1L);
			return salvo;
		});

		Cliente criado = service.inserir(cliente);

		assertEquals(1L, criado.getId());
		assertEquals(cepNormalizado, criado.getEndereco().getCep());
		verify(viaCepService).consultarCep(cepNormalizado);
		verify(clienteRepository).save(criado);
	}
}
