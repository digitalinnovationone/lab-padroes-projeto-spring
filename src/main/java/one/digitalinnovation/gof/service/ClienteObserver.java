package one.digitalinnovation.gof.service;

import one.digitalinnovation.gof.model.Cliente;

public interface ClienteObserver {

    void notificar (Cliente cliente, String evento);

}
