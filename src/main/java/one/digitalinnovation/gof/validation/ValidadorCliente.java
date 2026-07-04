package one.digitalinnovation.gof.validation;

import one.digitalinnovation.gof.model.Cliente;

public interface ValidadorCliente {

	void setProximo(ValidadorCliente proximo);

	void validar(Cliente cliente);
}
