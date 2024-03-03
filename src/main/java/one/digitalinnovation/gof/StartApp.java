package one.digitalinnovation.gof;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

/**
 * Insert a cliente data in the App initialization
 * @author siqueira76
 */
@Component
public class StartApp implements CommandLineRunner {

    @Autowired
    private ClienteService clienteService;

    @Override
    public void run(String... args) throws Exception {

        Cliente cliente = new Cliente();
        cliente.setEndereco(new Endereco());

        cliente.setNome("Lionel Messi");
        cliente.getEndereco().setCep("22021001");

        clienteService.inserir(cliente);
    }
}
