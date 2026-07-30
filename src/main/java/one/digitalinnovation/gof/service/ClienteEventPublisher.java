package one.digitalinnovation.gof.service;

import one.digitalinnovation.gof.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClienteEventPublisher {

    @Autowired
    private List<ClienteObserver> observers;

    public void notificarTodos(Cliente cliente, String evento){
        observers.forEach(observer -> observer.notificar(cliente, evento));
    }
}
