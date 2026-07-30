package one.digitalinnovation.gof.service.impl;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.service.ClienteObserver;

import java.time.LocalDateTime;

public class AuditoriaObserver  implements ClienteObserver {

    @Override
    public void notificar(Cliente cliente, String evento) {
        System.out.printf("[AUDITORIA] %s - Cliente '%s' (id=%d) foi %s.%n", LocalDateTime.now(), cliente.getNome(), cliente.getId(), evento);
    }

}
