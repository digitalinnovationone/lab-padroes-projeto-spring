package one.digitalinnovation.gof.validation;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import one.digitalinnovation.gof.exception.ValidacaoException;
import one.digitalinnovation.gof.model.Cliente;

@Component
public class ValidadorEmailFormato extends AbstractValidadorCliente {

	private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

	@Override
	protected void aplicar(Cliente cliente) {
		String email = cliente.getEmail();
		if (email == null || !EMAIL.matcher(email).matches()) {
			throw new ValidacaoException("O e-mail informado é inválido.");
		}
	}
}
