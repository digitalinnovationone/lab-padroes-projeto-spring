package one.digitalinnovation.gof.validation;

import org.springframework.stereotype.Component;

import one.digitalinnovation.gof.model.Cliente;

@Component
public class CadeiaValidacaoCliente {

	private final ValidadorCliente primeiro;

	public CadeiaValidacaoCliente(ValidadorNomeObrigatorio nome, ValidadorEmailFormato email,
			ValidadorCepFormato cep) {
		nome.setProximo(email);
		email.setProximo(cep);
		this.primeiro = nome;
	}

	public void validar(Cliente cliente) {
		primeiro.validar(cliente);
	}
}
