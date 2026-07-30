package one.digitalinnovation.gof.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteEventPublisher;
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
    private ClienteEventPublisher clienteEventPublisher;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        Endereco endereco = new Endereco();
        endereco.setCep("01001-000");

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João");
        cliente.setEndereco(endereco);

        lenient().when(enderecoRepository.findById("01001-000")).thenReturn(Optional.of(endereco));
    }

    @Test
    void deveNotificarObserversAoInserirCliente() {
        clienteService.inserir(cliente);

        verify(clienteRepository, times(1)).save(cliente);
        verify(clienteEventPublisher, times(1)).notificarTodos(cliente, "CRIADO");
    }

    @Test
    void deveNotificarObserversAoAtualizarCliente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        clienteService.atualizar(1L, cliente);

        verify(clienteEventPublisher, times(1)).notificarTodos(cliente, "ATUALIZADO");
    }

    @Test
    void naoDeveNotificarAoAtualizarClienteInexistente() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        clienteService.atualizar(99L, cliente);

        verify(clienteEventPublisher, times(0)).notificarTodos(any(), any());
    }

    @Test
    void deveNotificarObserversAoDeletarCliente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        clienteService.deletar(1L);

        verify(clienteRepository, times(1)).deleteById(1L);
        verify(clienteEventPublisher, times(1)).notificarTodos(cliente, "REMOVIDO");
    }

}
