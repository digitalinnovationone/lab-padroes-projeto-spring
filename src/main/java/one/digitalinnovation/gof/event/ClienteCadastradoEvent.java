package one.digitalinnovation.gof.event;

import org.springframework.context.ApplicationEvent;

import one.digitalinnovation.gof.model.Cliente;

public class ClienteCadastradoEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	private final transient Cliente cliente;

	public ClienteCadastradoEvent(Object source, Cliente cliente) {
		super(source);
		this.cliente = cliente;
	}

	public Cliente getCliente() {
		return cliente;
	}
}
