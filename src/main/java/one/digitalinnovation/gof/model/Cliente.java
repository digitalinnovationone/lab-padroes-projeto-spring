package one.digitalinnovation.gof.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String nome;
	private String email;
	@ManyToOne
	private Endereco endereco;

	public Cliente() {
	}

	private Cliente(Builder builder) {
		this.id = builder.id;
		this.nome = builder.nome;
		this.email = builder.email;
		this.endereco = builder.endereco;
	}

	public static Builder builder() {
		return new Builder();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public static class Builder {

		private Long id;
		private String nome;
		private String email;
		private Endereco endereco;

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder nome(String nome) {
			this.nome = nome;
			return this;
		}

		public Builder email(String email) {
			this.email = email;
			return this;
		}

		public Builder endereco(Endereco endereco) {
			this.endereco = endereco;
			return this;
		}

		public Cliente build() {
			return new Cliente(this);
		}
	}

}
