package one.digitalinnovation.gof;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;
import one.digitalinnovation.gof.model.EnderecoRepository;
import one.digitalinnovation.gof.service.impl.ClienteServiceImpl;

public class ClienteServiceTest {
	
	@Mock
	private ClienteRepository clienteRepository;
	
	@Mock
	private EnderecoRepository enderecoRepository; 
	
	@Mock
	private ClienteServiceImpl clienteService;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	@Test
	void deveBuscarClientePorId() {
		Cliente cliente = new Cliente();
		cliente.setId(1L);
		cliente.setNome("João");
		
		when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
		Cliente resultado = clienteService.buscarPorId(1L);
		
		assertNotNull(resultado);
        assertEquals("João", resultado.getNome());
        verify(clienteRepository, times(1)).findById(1L);
		
	}

}
