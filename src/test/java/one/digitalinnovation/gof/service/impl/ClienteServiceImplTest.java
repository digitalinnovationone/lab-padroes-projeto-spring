package one.digitalinnovation.gof.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import one.digitalinnovation.gof.event.ClienteCadastradoEvent;
import one.digitalinnovation.gof.exception.RecursoNaoEncontradoException;
import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.EnderecoRepository;
import one.digitalinnovation.gof.service.ViaCepService;
import one.digitalinnovation.gof.validation.CadeiaValidacaoCliente;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

	@Mock
	private ClienteRepository clienteRepository;
	@Mock
	private EnderecoRepository enderecoRepository;
	@Mock
	private ViaCepService viaCepService;
	@Mock
	private CadeiaValidacaoCliente cadeiaValidacao;
	@Mock
	private ApplicationEventPublisher eventPublisher;
	@InjectMocks
	private ClienteServiceImpl service;

	private Cliente clienteComCep(String cep) {
		return Cliente.builder()
				.nome("Rafael Alvarenga")
				.email("rafael@example.com")
				.endereco(Endereco.builder().cep(cep).build())
				.build();
	}

	@Test
	void inserirDeveValidarConsultarCepSalvarEPublicarEvento() {
		Cliente cliente = clienteComCep("01001000");
		Endereco resolvido = Endereco.builder().cep("01001000").localidade("São Paulo").build();
		when(enderecoRepository.findById("01001000")).thenReturn(Optional.empty());
		when(viaCepService.consultarCep("01001000")).thenReturn(resolvido);

		service.inserir(cliente);

		verify(cadeiaValidacao).validar(cliente);
		verify(viaCepService).consultarCep("01001000");
		verify(enderecoRepository).save(resolvido);
		verify(clienteRepository).save(cliente);
		verify(eventPublisher).publishEvent(any(ClienteCadastradoEvent.class));
	}

	@Test
	void inserirDeveReusarEnderecoExistenteSemConsultarViaCep() {
		Cliente cliente = clienteComCep("01001000");
		Endereco existente = Endereco.builder().cep("01001000").build();
		when(enderecoRepository.findById("01001000")).thenReturn(Optional.of(existente));

		service.inserir(cliente);

		verify(viaCepService, never()).consultarCep(anyString());
		verify(enderecoRepository, never()).save(any(Endereco.class));
		verify(clienteRepository).save(cliente);
	}

	@Test
	void buscarPorIdInexistenteDeveLancarExcecao() {
		when(clienteRepository.findById(99L)).thenReturn(Optional.empty());
		assertThrows(RecursoNaoEncontradoException.class, () -> service.buscarPorId(99L));
	}

	@Test
	void buscarPorIdExistenteDeveRetornarCliente() {
		Cliente cliente = Cliente.builder().id(1L).nome("Rafael Alvarenga").build();
		when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
		assertSame(cliente, service.buscarPorId(1L));
	}

	@Test
	void atualizarDeveValidarPreservarIdESalvar() {
		Cliente existente = Cliente.builder().id(1L).nome("Antigo").build();
		Cliente novo = clienteComCep("01001000");
		Endereco existenteEndereco = Endereco.builder().cep("01001000").build();
		when(clienteRepository.findById(1L)).thenReturn(Optional.of(existente));
		when(enderecoRepository.findById("01001000")).thenReturn(Optional.of(existenteEndereco));

		service.atualizar(1L, novo);

		verify(cadeiaValidacao).validar(novo);
		assertEquals(1L, novo.getId());
		verify(clienteRepository).save(novo);
	}

	@Test
	void atualizarClienteInexistenteDeveLancarExcecao() {
		Cliente novo = clienteComCep("01001000");
		when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

		assertThrows(RecursoNaoEncontradoException.class, () -> service.atualizar(99L, novo));
		verify(cadeiaValidacao, never()).validar(any(Cliente.class));
		verify(clienteRepository, never()).save(any(Cliente.class));
	}

	@Test
	void deletarClienteExistenteDeveRemover() {
		Cliente existente = Cliente.builder().id(1L).nome("Rafael Alvarenga").build();
		when(clienteRepository.findById(1L)).thenReturn(Optional.of(existente));

		service.deletar(1L);

		verify(clienteRepository).deleteById(1L);
	}

	@Test
	void deletarClienteInexistenteDeveLancarExcecao() {
		when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

		assertThrows(RecursoNaoEncontradoException.class, () -> service.deletar(99L));
		verify(clienteRepository, never()).deleteById(any());
	}
}
