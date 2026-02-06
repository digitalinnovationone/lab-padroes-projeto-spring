package one.digitalinnovation.gof.exception;

public class ClienteNotFoundException extends RuntimeException {

	public ClienteNotFoundException(Long id) {
		super("Cliente nao encontrado com ID: " + id);
	}

}
