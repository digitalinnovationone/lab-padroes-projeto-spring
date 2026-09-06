package one.digitalinnovation.gof.service.chain;

import org.springframework.stereotype.Component;

import one.digitalinnovation.gof.dto.ClienteRequestDTO;
import one.digitalinnovation.gof.exception.ValidationException;
import one.digitalinnovation.gof.model.ClienteRepository;

@Component
public class CepValidationHandler extends ClienteValidationHandler {

    @Override
    public void validate(ClienteRequestDTO request, Long idAtual, ClienteRepository repository) {
        String cep = request.getCep();
        if (cep == null || cep.isBlank()) {
            throw new ValidationException("CEP não pode ser nulo ou vazio.");
        }

        String cleanCep = cep.replaceAll("\\D", "");
        if (cleanCep.length() != 8) {
            throw new ValidationException("CEP inválido. O CEP deve possuir exatamente 8 dígitos numéricos: " + cep);
        }

        checkNext(request, idAtual, repository);
    }
}
