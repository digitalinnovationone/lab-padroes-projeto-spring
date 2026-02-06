package one.digitalinnovation.gof.event;

import one.digitalinnovation.gof.model.Cliente;
import org.springframework.context.ApplicationEvent;

/**
 * Evento de dominio que representa uma operacao realizada sobre um {@link Cliente}.
 * Demonstra o padrao <b>Observer</b> via Spring Events.
 */
public class ClienteEvent extends ApplicationEvent {

	public enum TipoOperacao {
		CRIADO, ATUALIZADO, DELETADO
	}

	private final Cliente cliente;
	private final TipoOperacao tipo;

	public ClienteEvent(Object source, Cliente cliente, TipoOperacao tipo) {
		super(source);
		this.cliente = cliente;
		this.tipo = tipo;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public TipoOperacao getTipo() {
		return tipo;
	}

}
