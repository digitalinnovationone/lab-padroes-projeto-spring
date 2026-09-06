package one.digitalinnovation.gof.event.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import one.digitalinnovation.gof.event.ClienteCriadoEvent;

@Component
public class AuditLogObserver {

    private static final Logger log = LoggerFactory.getLogger(AuditLogObserver.class);

    @EventListener
    public void onClienteCriado(ClienteCriadoEvent event) {
        log.info("[Observer: Auditoria] AUDIT LOG -> Novo cliente cadastrado com sucesso: ID={}, Nome='{}', CPF='{}', E-mail='{}', Data={}",
                event.getCliente().getId(),
                event.getCliente().getNome(),
                event.getCliente().getCpf(),
                event.getCliente().getEmail(),
                event.getCliente().getDataCadastro());
    }
}
