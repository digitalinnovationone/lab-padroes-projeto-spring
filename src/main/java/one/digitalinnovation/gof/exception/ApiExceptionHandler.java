package one.digitalinnovation.gof.exception;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

	@ExceptionHandler(RecursoNaoEncontradoException.class)
	public ResponseEntity<ErroResponse> tratarNaoEncontrado(RecursoNaoEncontradoException ex,
			HttpServletRequest request) {
		return montar(HttpStatus.NOT_FOUND, ex.getMessage(), request);
	}

	@ExceptionHandler(ValidacaoException.class)
	public ResponseEntity<ErroResponse> tratarValidacao(ValidacaoException ex, HttpServletRequest request) {
		return montar(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErroResponse> tratarCamposInvalidos(MethodArgumentNotValidException ex,
			HttpServletRequest request) {
		String mensagem = ex.getBindingResult().getFieldErrors().stream()
				.map(this::descreverCampo)
				.collect(Collectors.joining("; "));
		return montar(HttpStatus.BAD_REQUEST, mensagem, request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErroResponse> tratarInesperado(Exception ex, HttpServletRequest request) {
		log.error("Erro inesperado ao processar {}", request.getRequestURI(), ex);
		return montar(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno inesperado.", request);
	}

	private String descreverCampo(FieldError erro) {
		return erro.getField() + ": " + erro.getDefaultMessage();
	}

	private ResponseEntity<ErroResponse> montar(HttpStatus status, String mensagem, HttpServletRequest request) {
		ErroResponse corpo = new ErroResponse(status.value(), status.getReasonPhrase(), mensagem,
				request.getRequestURI());
		return ResponseEntity.status(status).body(corpo);
	}
}
