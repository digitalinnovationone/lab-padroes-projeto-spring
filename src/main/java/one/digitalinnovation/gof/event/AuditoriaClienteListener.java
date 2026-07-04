package one.digitalinnovation.gof.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import one.digitalinnovation.gof.model.Cliente;

@Component
public class AuditoriaClienteListener {

	private static final Logger log = LoggerFactory.getLogger(AuditoriaClienteListener.class);

	@EventListener
	public void aoCadastrarCliente(ClienteCadastradoEvent evento) {
		Cliente cliente = evento.getCliente();
		log.info("[AUDITORIA] Cliente cadastrado: id={}, nome={}.", cliente.getId(), cliente.getNome());
	}
}
