package one.digitalinnovation.gof.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<Map<String, String>> tratarResponseStatusException(ResponseStatusException exception) {
		String mensagem = exception.getReason();
		if (mensagem == null || mensagem.isBlank()) {
			mensagem = exception.getStatus().getReasonPhrase();
		}
		return ResponseEntity.status(exception.getStatus())
				.body(Collections.singletonMap("mensagem", mensagem));
	}
}
