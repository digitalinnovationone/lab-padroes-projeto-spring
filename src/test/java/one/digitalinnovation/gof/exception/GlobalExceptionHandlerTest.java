package one.digitalinnovation.gof.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Deve retornar ProblemDetail com status 404 para ClienteNaoEncontradoException")
    void handleClienteNotFound_DeveRetornarStatus404() {
        String mensagemErro = "Cliente com ID 1 não encontrado.";
        ClienteNaoEncontradoException exception = new ClienteNaoEncontradoException(mensagemErro);

        ProblemDetail response = exceptionHandler.handleClienteNotFound(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertEquals("Cliente não encontrado", response.getTitle());
        assertEquals(mensagemErro, response.getDetail());
    }

    @Test
    @DisplayName("Deve retornar ProblemDetail com status 400 para NullPointerException")
    void handleNullPointerException_DeveRetornarStatus400() {
        String mensagemErro = "Objeto cliente não pode ser nulo";
        NullPointerException exception = new NullPointerException(mensagemErro);

        ProblemDetail response = exceptionHandler.handleNullPointerException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("Requisição inválida", response.getTitle());
        assertEquals(mensagemErro, response.getDetail());
    }

    @Test
    @DisplayName("Deve retornar ProblemDetail com status 400 para IllegalArgumentException")
    void handleIllegalArgumentException_DeveRetornarStatus400() {
        String mensagemErro = "O ID informado não pode ser negativo";
        IllegalArgumentException exception = new IllegalArgumentException(mensagemErro);

        ProblemDetail response = exceptionHandler.handleIllegalArgumentException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("Parâmetro incorreto", response.getTitle());
        assertEquals(mensagemErro, response.getDetail());
    }

    @Test
    @DisplayName("Deve retornar ProblemDetail com status 500 para Exception genérica")
    void handleGenericException_DeveRetornarStatus500() {
        Exception exception = new RuntimeException("Falha de conexão com o banco de dados");

        ProblemDetail response = exceptionHandler.handleGenericException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
        assertEquals("Internal Server Error", response.getTitle());
        // Garante que a mensagem original (que pode expor dados sensíveis) não vaza para o cliente
        assertEquals("Ocorreu um erro interno no servidor.", response.getDetail());
    }
}