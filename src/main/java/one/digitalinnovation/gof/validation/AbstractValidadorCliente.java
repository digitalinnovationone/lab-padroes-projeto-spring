package one.digitalinnovation.gof.validation;

import one.digitalinnovation.gof.model.Cliente;

public abstract class AbstractValidadorCliente implements ValidadorCliente {

	private ValidadorCliente proximo;

	@Override
	public void setProximo(ValidadorCliente proximo) {
		this.proximo = proximo;
	}

	@Override
	public void validar(Cliente cliente) {
		aplicar(cliente);
		if (proximo != null) {
			proximo.validar(cliente);
		}
	}

	protected abstract void aplicar(Cliente cliente);
}
