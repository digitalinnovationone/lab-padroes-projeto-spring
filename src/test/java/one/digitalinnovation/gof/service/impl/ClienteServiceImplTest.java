package one.digitalinnovation.gof.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import one.digitalinnovation.gof.dto.ClienteDto;
import one.digitalinnovation.gof.dto.EnderecoDto;
import one.digitalinnovation.gof.exception.NotFoundException;
import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.repository.ClienteRepository;
import one.digitalinnovation.gof.repository.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteMapper;
import one.digitalinnovation.gof.service.ViaCepService;

@ExtendWith(MockitoExtension.class)
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
		service = new ClienteServiceImpl(clienteRepository, enderecoRepository, viaCepService, new ClienteMapper());
	}

	private EnderecoDto enderecoDto(String cep) {
		return new EnderecoDto(cep, null, null, null, null, null, null, null, null, null);
	}

	@Test
	void buscarPorId_deveRetornarDto_quandoClienteExiste() {
		Cliente cliente = new Cliente();
		cliente.setId(1L);
		cliente.setNome("Fulano");
		Endereco endereco = new Endereco();
		endereco.setCep("01001000");
		cliente.setEndereco(endereco);

		when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

		ClienteDto dto = service.buscarPorId(1L);

		assertThat(dto.id()).isEqualTo(1L);
		assertThat(dto.nome()).isEqualTo("Fulano");
		assertThat(dto.endereco().cep()).isEqualTo("01001000");
	}

	@Test
	void buscarPorId_deveLancarNotFoundException_quandoClienteNaoExiste() {
		when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.buscarPorId(99L))
				.isInstanceOf(NotFoundException.class)
				.hasMessageContaining("99");
	}

	@Test
	void inserir_deveConsultarViaCep_eSalvarCliente_quandoCepNaoExiste() {
		ClienteDto dto = new ClienteDto(null, "Fulano", enderecoDto("01001000"));

		Endereco enderecoViaCep = new Endereco();
		enderecoViaCep.setCep("01001000");
		enderecoViaCep.setLogradouro("Praça da Sé");

		when(enderecoRepository.findById("01001000")).thenReturn(Optional.empty());
		when(viaCepService.consultarCep("01001000")).thenReturn(enderecoViaCep);
		when(enderecoRepository.save(any(Endereco.class))).thenAnswer(inv -> inv.getArgument(0));
		when(clienteRepository.save(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));

		ClienteDto resultado = service.inserir(dto);

		verify(viaCepService).consultarCep("01001000");
		verify(enderecoRepository).save(any(Endereco.class));
		assertThat(resultado.endereco().cep()).isEqualTo("01001000");
	}

	@Test
	void inserir_naoDeveConsultarViaCep_quandoCepJaExiste() {
		ClienteDto dto = new ClienteDto(null, "Fulano", enderecoDto("01001000"));

		Endereco existente = new Endereco();
		existente.setCep("01001000");

		when(enderecoRepository.findById("01001000")).thenReturn(Optional.of(existente));
		when(clienteRepository.save(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));

		service.inserir(dto);

		verify(viaCepService, never()).consultarCep(anyString());
		verify(enderecoRepository, never()).save(any(Endereco.class));
	}

	@Test
	void inserir_deveLancarIllegalArgumentException_quandoEnderecoNulo() {
		ClienteDto dto = new ClienteDto(null, "Fulano", null);

		assertThatThrownBy(() -> service.inserir(dto))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void inserir_deveLancarIllegalArgumentException_quandoCepVazio() {
		ClienteDto dto = new ClienteDto(null, "Fulano", enderecoDto(""));

		assertThatThrownBy(() -> service.inserir(dto))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void atualizar_deveLancarNotFoundException_quandoClienteNaoExiste() {
		when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.atualizar(99L, new ClienteDto(null, "Fulano", enderecoDto("01001000"))))
				.isInstanceOf(NotFoundException.class);
	}

	@Test
	void deletar_deveLancarNotFoundException_quandoClienteNaoExiste() {
		when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.deletar(99L))
				.isInstanceOf(NotFoundException.class);
	}
}