package one.digitalinnovation.gof.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import one.digitalinnovation.gof.model.Cliente;

@Component
public class NotificacaoBoasVindasListener {

	private static final Logger log = LoggerFactory.getLogger(NotificacaoBoasVindasListener.class);

	@EventListener
	public void aoCadastrarCliente(ClienteCadastradoEvent evento) {
		Cliente cliente = evento.getCliente();
		log.info("Enviando e-mail de boas-vindas para {} <{}>.", cliente.getNome(), cliente.getEmail());
	}
}
