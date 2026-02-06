package one.digitalinnovation.gof.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import one.digitalinnovation.gof.event.ClienteEvent;
import one.digitalinnovation.gof.event.ClienteEvent.TipoOperacao;
import one.digitalinnovation.gof.exception.ClienteNotFoundException;
import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.EnderecoRepository;
import one.digitalinnovation.gof.service.ViaCepService;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

	@Mock
	private ClienteRepository clienteRepository;
	@Mock
	private EnderecoRepository enderecoRepository;
	@Mock
	private ViaCepService viaCepService;
	@Mock
	private ApplicationEventPublisher eventPublisher;

	@InjectMocks
	private ClienteServiceImpl clienteService;

	private Cliente cliente;
	private Endereco endereco;

	@BeforeEach
	void setUp() {
		endereco = new Endereco();
		endereco.setCep("01001000");
		endereco.setLogradouro("Praca da Se");
		endereco.setLocalidade("Sao Paulo");
		endereco.setUf("SP");

		cliente = new Cliente();
		cliente.setId(1L);
		cliente.setNome("Joao Silva");
		cliente.setEndereco(endereco);
	}

	@Test
	void deveBuscarTodosOsClientes() {
		when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente));

		Iterable<Cliente> resultado = clienteService.buscarTodos();

		assertNotNull(resultado);
		verify(clienteRepository).findAll();
	}

	@Test
	void deveBuscarClientePorIdQuandoExistir() {
		when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

		Cliente resultado = clienteService.buscarPorId(1L);

		assertNotNull(resultado);
		assertEquals("Joao Silva", resultado.getNome());
		verify(clienteRepository).findById(1L);
	}

	@Test
	void deveLancarExcecaoQuandoClienteNaoEncontrado() {
		when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

		assertThrows(ClienteNotFoundException.class,
				() -> clienteService.buscarPorId(99L));
	}

	@Test
	void deveInserirClienteComConsultaCep() {
		when(enderecoRepository.findById("01001000")).thenReturn(Optional.empty());
		when(viaCepService.consultarCep("01001000")).thenReturn(endereco);

		clienteService.inserir(cliente);

		verify(viaCepService).consultarCep("01001000");
		verify(enderecoRepository).save(endereco);
		verify(clienteRepository).save(cliente);
		verify(eventPublisher).publishEvent(any(ClienteEvent.class));
	}

	@Test
	void deveUsarEnderecoExistenteQuandoCepJaCadastrado() {
		when(enderecoRepository.findById("01001000")).thenReturn(Optional.of(endereco));

		clienteService.inserir(cliente);

		verify(viaCepService, never()).consultarCep(anyString());
		verify(clienteRepository).save(cliente);
	}

	@Test
	void deveLancarExcecaoAoAtualizarClienteInexistente() {
		when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

		assertThrows(ClienteNotFoundException.class,
				() -> clienteService.atualizar(99L, cliente));
	}

	@Test
	void deveDeletarClientePorId() {
		when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

		clienteService.deletar(1L);

		verify(clienteRepository).deleteById(1L);
		verify(eventPublisher).publishEvent(any(ClienteEvent.class));
	}

	@Test
	void devePublicarEventoCriadoAoInserirCliente() {
		when(enderecoRepository.findById("01001000")).thenReturn(Optional.of(endereco));

		clienteService.inserir(cliente);

		ArgumentCaptor<ClienteEvent> captor = ArgumentCaptor.forClass(ClienteEvent.class);
		verify(eventPublisher).publishEvent(captor.capture());
		assertEquals(TipoOperacao.CRIADO, captor.getValue().getTipo());
		assertEquals("Joao Silva", captor.getValue().getCliente().getNome());
	}

}
