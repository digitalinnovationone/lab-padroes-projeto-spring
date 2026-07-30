package one.digitalinnovation.gof;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import one.digitalinnovation.gof.service.ClienteEventPublisher;
import one.digitalinnovation.gof.service.ClienteObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import one.digitalinnovation.gof.model.Cliente;

@ExtendWith(MockitoExtension.class)
class ClienteEventPublisherTest {

    @Mock
    private ClienteObserver emailObserver;

    @Mock
    private ClienteObserver auditoriaObserver;

    private ClienteEventPublisher clienteEventPublisher;

    @BeforeEach
    void setUp() {
        clienteEventPublisher = new ClienteEventPublisher();
        ReflectionTestUtils.setField(clienteEventPublisher, "observers",
                Arrays.asList(emailObserver, auditoriaObserver));
    }

    @Test
    void deveNotificarTodosOsObserversRegistrados() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Maria");

        clienteEventPublisher.notificarTodos(cliente, "CRIADO");

        verify(emailObserver, times(1)).notificar(cliente, "CRIADO");
        verify(auditoriaObserver, times(1)).notificar(cliente, "CRIADO");
    }

}

