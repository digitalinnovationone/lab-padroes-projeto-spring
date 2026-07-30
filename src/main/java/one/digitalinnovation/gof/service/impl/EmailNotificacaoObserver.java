package one.digitalinnovation.gof.service.impl;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.service.ClienteObserver;

public class EmailNotificacaoObserver implements ClienteObserver {

    @Override
    public void notificar(Cliente cliente, String evento) {
        System.out.printf("[E-MAIL] Cliente '%s' (id=%d) -> evento: %s%n", cliente.getNome(), cliente.getId(), evento);
    }

}
