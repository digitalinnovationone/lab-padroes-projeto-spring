package one.digitalinnovation.gof.model;

import javax.persistence.*;

@Entity
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String nome;
	@ManyToOne
	private Endereco endereco;
	@OneToOne
	private PerfilCliente perfil;
	@ManyToOne
	private Contato contato;

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

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public PerfilCliente getPerfil() {
		return perfil;
	}
	public void setPerfil(PerfilCliente perfil) {
		this.perfil = perfil;
	}
	public Contato getContato() {
		return contato;
	}
	public void setContato(Contato contato){
		this.contato = contato;
	}

}
