package one.digitalinnovation.gof.validation;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import one.digitalinnovation.gof.exception.ValidacaoException;
import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.Endereco;

@Component
public class ValidadorCepFormato extends AbstractValidadorCliente {

	private static final Pattern CEP = Pattern.compile("\\d{8}");

	@Override
	protected void aplicar(Cliente cliente) {
		Endereco endereco = cliente.getEndereco();
		if (endereco == null || endereco.getCep() == null || !CEP.matcher(endereco.getCep()).matches()) {
			throw new ValidacaoException("O CEP deve conter 8 dígitos numéricos.");
		}
	}
}
