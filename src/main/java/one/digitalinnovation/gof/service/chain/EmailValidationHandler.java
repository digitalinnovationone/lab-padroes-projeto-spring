package one.digitalinnovation.gof.service.chain;

import org.springframework.stereotype.Component;

import one.digitalinnovation.gof.dto.ClienteRequestDTO;
import one.digitalinnovation.gof.exception.ValidationException;
import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;

@Component
public class EmailValidationHandler extends ClienteValidationHandler {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    @Override
    public void validate(ClienteRequestDTO request, Long idAtual, ClienteRepository repository) {
        String email = request.getEmail();
        if (email == null || email.isBlank()) {
            throw new ValidationException("E-mail não pode ser nulo ou vazio.");
        }

        if (!email.matches(EMAIL_REGEX)) {
            throw new ValidationException("Formato de e-mail inválido: " + email);
        }

        // Validação de unicidade no banco de dados
        Iterable<Cliente> clientes = repository.findAll();
        for (Cliente cliente : clientes) {
            if (email.equalsIgnoreCase(cliente.getEmail())) {
                if (idAtual == null || !cliente.getId().equals(idAtual)) {
                    throw new ValidationException("Já existe um cliente cadastrado com o e-mail: " + email);
                }
            }
        }

        checkNext(request, idAtual, repository);
    }
}
