package one.digitalinnovation.gof.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class ClienteRequest {

	@NotBlank(message = "O nome é obrigatório.")
	private String nome;

	@NotBlank(message = "O e-mail é obrigatório.")
	@Email(message = "O e-mail informado é inválido.")
	private String email;

	@NotBlank(message = "O CEP é obrigatório.")
	@Pattern(regexp = "\\d{8}", message = "O CEP deve conter 8 dígitos numéricos.")
	private String cep;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}
}
