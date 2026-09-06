package one.digitalinnovation.gof.service.chain;

import org.springframework.stereotype.Component;

import one.digitalinnovation.gof.dto.ClienteRequestDTO;
import one.digitalinnovation.gof.exception.ValidationException;
import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;

import java.util.Optional;

@Component
public class CpfValidationHandler extends ClienteValidationHandler {

    @Override
    public void validate(ClienteRequestDTO request, Long idAtual, ClienteRepository repository) {
        String cpf = request.getCpf();
        if (cpf == null || cpf.isBlank()) {
            throw new ValidationException("CPF não pode ser nulo ou vazio.");
        }

        String cleanCpf = cpf.replaceAll("\\D", "");
        if (cleanCpf.length() != 11 || isCpfComTodosDigitosIguais(cleanCpf)) {
            throw new ValidationException("CPF com número de dígitos inválido: " + cpf);
        }

        if (!isValidCpfChecksum(cleanCpf)) {
            throw new ValidationException("Dígito verificador do CPF inválido: " + cpf);
        }

        // Validar unicidade no repositório
        Optional<Cliente> clienteExistente = repository.findByCpf(cleanCpf);
        if (clienteExistente.isPresent()) {
            if (idAtual == null || !clienteExistente.get().getId().equals(idAtual)) {
                throw new ValidationException("Já existe um cliente cadastrado com o CPF: " + cpf);
            }
        }

        checkNext(request, idAtual, repository);
    }

    private boolean isCpfComTodosDigitosIguais(String cpf) {
        return cpf.matches("^(\\d)\\1{10}$");
    }

    private boolean isValidCpfChecksum(String cpf) {
        try {
            int d1 = calcularDigito(cpf.substring(0, 9), 10);
            int d2 = calcularDigito(cpf.substring(0, 9) + d1, 11);
            return cpf.endsWith("" + d1 + d2);
        } catch (Exception e) {
            return false;
        }
    }

    private int calcularDigito(String str, int pesoInicial) {
        int soma = 0;
        int peso = pesoInicial;
        for (int i = 0; i < str.length(); i++) {
            soma += Character.getNumericValue(str.charAt(i)) * peso--;
        }
        int resto = 11 - (soma % 11);
        return (resto > 9) ? 0 : resto;
    }
}
