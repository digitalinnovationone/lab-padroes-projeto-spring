package one.digitalinnovation.gof.exception;

/**
 * Exceção para falhas na integração com a API externa ViaCEP.
 * Mapeada para HTTP 502 (Bad Gateway) pelo {@link GlobalExceptionHandler}.
 *
 * @author falvojr
 */
public class ViaCepException extends RuntimeException {

	public ViaCepException(String message) {
		super(message);
	}
}