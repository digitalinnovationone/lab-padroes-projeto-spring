package one.digitalinnovation.gof.event.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import one.digitalinnovation.gof.event.ClienteCriadoEvent;

@Component
public class NotificationObserver {

    private static final Logger log = LoggerFactory.getLogger(NotificationObserver.class);

    @EventListener
    public void onClienteCriadoSendWelcomeEmail(ClienteCriadoEvent event) {
        log.info("[Observer: Notificação] NOTIFICAÇÃO -> Enviando e-mail de boas-vindas para: {} ({}) - Tipo: {}",
                event.getCliente().getEmail(),
                event.getCliente().getNome(),
                event.getCliente().getTipoCliente());
    }
}
