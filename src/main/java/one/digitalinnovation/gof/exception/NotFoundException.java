package one.digitalinnovation.gof.exception;

/**
 * Exceção para recursos não encontrados. Mapeada para HTTP 404 pelo
 * {@link GlobalExceptionHandler}.
 *
 * @author falvojr
 */
public class NotFoundException extends RuntimeException {

	public NotFoundException(String message) {
		super(message);
	}
}