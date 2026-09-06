package one.digitalinnovation.gof.service.chain;

import one.digitalinnovation.gof.dto.ClienteRequestDTO;
import one.digitalinnovation.gof.model.ClienteRepository;

/**
 * Padrão Chain of Responsibility: Classe base abstrata para encadeamento de validações de negócio.
 */
public abstract class ClienteValidationHandler {

    private ClienteValidationHandler nextHandler;

    public ClienteValidationHandler linkWith(ClienteValidationHandler next) {
        this.nextHandler = next;
        return next;
    }

    public abstract void validate(ClienteRequestDTO request, Long idAtual, ClienteRepository repository);

    protected void checkNext(ClienteRequestDTO request, Long idAtual, ClienteRepository repository) {
        if (nextHandler != null) {
            nextHandler.validate(request, idAtual, repository);
        }
    }
}
