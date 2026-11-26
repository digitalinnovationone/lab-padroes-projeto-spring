package one.digitalinnovation.gof.controller;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ApiExceptionHandler {

	// Converte erros de Bean Validation em uma resposta JSON clara para o consumidor da API.
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> tratarValidacao(MethodArgumentNotValidException exception) {
		Map<String, String> erros = exception.getBindingResult().getFieldErrors().stream()
				.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage,
						(first, second) -> first, LinkedHashMap::new));
		Map<String, Object> resposta = new LinkedHashMap<>();
		resposta.put("mensagem", "Dados inválidos");
		resposta.put("erros", erros);
		return ResponseEntity.badRequest().body(resposta);
	}

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
