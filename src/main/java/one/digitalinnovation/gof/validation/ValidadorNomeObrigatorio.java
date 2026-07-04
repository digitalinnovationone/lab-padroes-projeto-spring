package one.digitalinnovation.gof.validation;

import org.springframework.stereotype.Component;

import one.digitalinnovation.gof.exception.ValidacaoException;
import one.digitalinnovation.gof.model.Cliente;

@Component
public class ValidadorNomeObrigatorio extends AbstractValidadorCliente {

	@Override
	protected void aplicar(Cliente cliente) {
		if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
			throw new ValidacaoException("O nome do cliente é obrigatório.");
		}
	}
}
