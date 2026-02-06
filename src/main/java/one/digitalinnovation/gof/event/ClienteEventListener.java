package one.digitalinnovation.gof.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Observer/Listener que reage a eventos de dominio do {@link one.digitalinnovation.gof.model.Cliente}.
 * Demonstra o padrao <b>Observer</b> via Spring Events.
 */
@Component
public class ClienteEventListener {

	private static final Logger logger = LoggerFactory.getLogger(ClienteEventListener.class);

	@EventListener
	public void onClienteEvent(ClienteEvent event) {
		logger.info("Evento de cliente: {} - Cliente: {} (ID: {})",
				event.getTipo(),
				event.getCliente().getNome(),
				event.getCliente().getId());
	}

}
