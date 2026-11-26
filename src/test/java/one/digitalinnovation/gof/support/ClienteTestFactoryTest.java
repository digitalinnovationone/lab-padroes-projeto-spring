package one.digitalinnovation.gof.support;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import one.digitalinnovation.gof.model.Cliente;

class ClienteTestFactoryTest {

	@Test
	void deveGerarClientesComNomesLegiveisECepsValidos() {
		Cliente primeiro = ClienteTestFactory.criarClienteAleatorio();
		Cliente segundo = ClienteTestFactory.criarClienteAleatorio();

		System.out.println("Cliente gerado: " + primeiro.getNome()
				+ " | CEP: " + primeiro.getEndereco().getCep());
		System.out.println("Cliente gerado: " + segundo.getNome()
				+ " | CEP: " + segundo.getEndereco().getCep());

		assertTrue(primeiro.getNome().matches("\\p{Lu}[\\p{L}]+ \\p{Lu}[\\p{L}]+"));
		assertTrue(segundo.getNome().matches("\\p{Lu}[\\p{L}]+ \\p{Lu}[\\p{L}]+"));
		assertNotNull(primeiro.getEndereco());
		assertTrue(primeiro.getEndereco().getCep().matches("\\d{5}-\\d{3}"));
		assertTrue(Set.of("01001-000", "20040-020", "30130-100", "40020-000", "50030-230")
				.contains(primeiro.getEndereco().getCep()));
	}
}
