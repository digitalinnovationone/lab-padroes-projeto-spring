package one.digitalinnovation.gof.support;

import java.util.concurrent.ThreadLocalRandom;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.Endereco;

public final class ClienteTestFactory {

	private static final String[] NOMES = {
			"Ana", "Beatriz", "Camila", "Daniel", "Eduarda", "Felipe",
			"Gabriela", "Henrique", "Isabela", "Joao", "Larissa", "Marcos"
	};

	private static final String[] SOBRENOMES = {
			"Almeida", "Barbosa", "Costa", "Ferreira", "Lima", "Martins",
			"Oliveira", "Pereira", "Ribeiro", "Santos", "Souza", "Teixeira"
	};

	private static final String[] CEPS_VALIDOS = {
			"01001-000",
			"20040-020",
			"30130-100",
			"40020-000",
			"50030-230"
	};

	private ClienteTestFactory() {
	}

	public static Cliente criarClienteAleatorio() {
		Cliente cliente = new Cliente();
		cliente.setNome(nomeAleatorio(NOMES) + " " + nomeAleatorio(SOBRENOMES));
		cliente.setEndereco(criarEnderecoAleatorio());
		return cliente;
	}

	public static Endereco criarEnderecoAleatorio() {
		int indice = ThreadLocalRandom.current().nextInt(CEPS_VALIDOS.length);

		Endereco endereco = new Endereco();
		endereco.setCep(CEPS_VALIDOS[indice]);
		return endereco;
	}

	private static String nomeAleatorio(String[] opcoes) {
		int indice = ThreadLocalRandom.current().nextInt(opcoes.length);
		return opcoes[indice];
	}
}
