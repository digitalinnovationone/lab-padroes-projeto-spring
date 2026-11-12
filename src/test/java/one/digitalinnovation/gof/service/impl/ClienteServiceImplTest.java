package one.digitalinnovation.gof.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente cliente;
    private Endereco endereco;
    private final Long CLIENTE_ID = 1L;
    private final String CEP = "12345678";

    @BeforeEach
    void setUp() {
        endereco = new Endereco();
        endereco.setCep(CEP);
        endereco.setLogradouro("Rua Teste");

        cliente = new Cliente();
        cliente.setId(CLIENTE_ID);
        cliente.setNome("João Silva");
        cliente.setEndereco(endereco);
    }

    @Test
    @DisplayName("Deve retornar todos os clientes")
    void buscarTodos_DeveRetornarListaDeClientes() {
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));

        Iterable<Cliente> clientes = clienteService.buscarTodos();

        assertNotNull(clientes);
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar um cliente quando o ID existir")
    void buscarPorId_DeveRetornarCliente_QuandoIdExistir() {
        when(clienteRepository.findById(CLIENTE_ID)).thenReturn(Optional.of(cliente));

        Cliente clienteEncontrado = clienteService.buscarPorId(CLIENTE_ID);

        assertNotNull(clienteEncontrado);
        assertEquals(CLIENTE_ID, clienteEncontrado.getId());
        verify(clienteRepository, times(1)).findById(CLIENTE_ID);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar por ID inexistente")
    void buscarPorId_DeveLancarExcecao_QuandoIdNaoExistir() {
        when(clienteRepository.findById(CLIENTE_ID)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> clienteService.buscarPorId(CLIENTE_ID));

        assertEquals("Cliente não encontrado para o ID informado.", exception.getMessage());
        verify(clienteRepository, times(1)).findById(CLIENTE_ID);
    }

    @Test
    @DisplayName("Deve inserir cliente com endereço já existente no banco")
    void inserir_DeveSalvarCliente_QuandoEnderecoJaExistir() {
        when(enderecoRepository.findById(CEP)).thenReturn(Optional.of(endereco));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        clienteService.inserir(cliente);

        verify(enderecoRepository, times(1)).findById(CEP);
        verify(viaCepService, never()).consultarCep(anyString());
        verify(enderecoRepository, never()).save(any(Endereco.class));
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    @DisplayName("Deve inserir cliente com novo endereço buscando via CEP")
    void inserir_DeveSalvarClienteEEndereco_QuandoEnderecoNaoExistir() {
        when(enderecoRepository.findById(CEP)).thenReturn(Optional.empty());
        when(viaCepService.consultarCep(CEP)).thenReturn(endereco);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        clienteService.inserir(cliente);

        verify(enderecoRepository, times(1)).findById(CEP);
        verify(viaCepService, times(1)).consultarCep(CEP);
        verify(enderecoRepository, times(1)).save(endereco);
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    @DisplayName("Deve atualizar cliente quando o ID existir")
    void atualizar_DeveSalvarCliente_QuandoIdExistir() {
        when(clienteRepository.findById(CLIENTE_ID)).thenReturn(Optional.of(cliente));
        when(enderecoRepository.findById(CEP)).thenReturn(Optional.of(endereco));

        clienteService.atualizar(CLIENTE_ID, cliente);

        verify(clienteRepository, times(1)).findById(CLIENTE_ID);
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    @DisplayName("Não deve atualizar cliente quando o ID não existir")
    void atualizar_NaoDeveSalvarCliente_QuandoIdNaoExistir() {
        when(clienteRepository.findById(CLIENTE_ID)).thenReturn(Optional.empty());

        clienteService.atualizar(CLIENTE_ID, cliente);

        verify(clienteRepository, times(1)).findById(CLIENTE_ID);
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve deletar cliente pelo ID")
    void deletar_DeveChamarDeleteDoRepository() {
        doNothing().when(clienteRepository).deleteById(CLIENTE_ID);

        clienteService.deletar(CLIENTE_ID);

        verify(clienteRepository, times(1)).deleteById(CLIENTE_ID);
    }
}