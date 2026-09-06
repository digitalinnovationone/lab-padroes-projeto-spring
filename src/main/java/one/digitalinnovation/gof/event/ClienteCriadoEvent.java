package one.digitalinnovation.gof.event;

import org.springframework.context.ApplicationEvent;
import one.digitalinnovation.gof.model.Cliente;

/**
 * Padrão Observer (Event-Driven): Evento disparado quando um novo cliente é registrado.
 */
public class ClienteCriadoEvent extends ApplicationEvent {

    private final Cliente cliente;

    public ClienteCriadoEvent(Object source, Cliente cliente) {
        super(source);
        this.cliente = cliente;
    }

    public Cliente getCliente() {
        return cliente;
    }
}
