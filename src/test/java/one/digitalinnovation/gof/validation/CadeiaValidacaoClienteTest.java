package one.digitalinnovation.gof.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import one.digitalinnovation.gof.exception.ValidacaoException;
import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.Endereco;

class CadeiaValidacaoClienteTest {

	private CadeiaValidacaoCliente cadeia;

	@BeforeEach
	void setUp() {
		cadeia = new CadeiaValidacaoCliente(new ValidadorNomeObrigatorio(), new ValidadorEmailFormato(),
				new ValidadorCepFormato());
	}

	private Cliente clienteValido() {
		return Cliente.builder()
				.nome("Rafael Alvarenga")
				.email("rafael@example.com")
				.endereco(Endereco.builder().cep("01001000").build())
				.build();
	}

	@Test
	void deveAceitarClienteValido() {
		assertDoesNotThrow(() -> cadeia.validar(clienteValido()));
	}

	@Test
	void deveRejeitarNomeEmBranco() {
		Cliente cliente = clienteValido();
		cliente.setNome("   ");
		assertThrows(ValidacaoException.class, () -> cadeia.validar(cliente));
	}

	@Test
	void deveRejeitarEmailInvalido() {
		Cliente cliente = clienteValido();
		cliente.setEmail("rafael-sem-arroba");
		assertThrows(ValidacaoException.class, () -> cadeia.validar(cliente));
	}

	@Test
	void deveRejeitarCepInvalido() {
		Cliente cliente = clienteValido();
		cliente.setEndereco(Endereco.builder().cep("123").build());
		assertThrows(ValidacaoException.class, () -> cadeia.validar(cliente));
	}

	@Test
	void deveRejeitarEnderecoAusente() {
		Cliente cliente = clienteValido();
		cliente.setEndereco(null);
		assertThrows(ValidacaoException.class, () -> cadeia.validar(cliente));
	}
}
