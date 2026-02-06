package one.digitalinnovation.gof.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Tratamento global de excecoes da API.
 * Centraliza as respostas de erro em um unico ponto.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ClienteNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleClienteNotFound(ClienteNotFoundException ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now().toString());
		body.put("status", 404);
		body.put("error", "Not Found");
		body.put("message", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
	}

	@ExceptionHandler(CepInvalidoException.class)
	public ResponseEntity<Map<String, Object>> handleCepInvalido(CepInvalidoException ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now().toString());
		body.put("status", 422);
		body.put("error", "Unprocessable Entity");
		body.put("message", ex.getMessage());
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now().toString());
		body.put("status", 400);
		body.put("error", "Bad Request");
		Map<String, String> fieldErrors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(
				error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));
		body.put("errors", fieldErrors);
		return ResponseEntity.badRequest().body(body);
	}

}
