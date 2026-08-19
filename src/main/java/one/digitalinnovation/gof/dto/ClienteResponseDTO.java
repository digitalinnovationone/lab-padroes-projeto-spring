package one.digitalinnovation.gof.dto;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.Endereco;

public class ClienteResponseDTO {

    private Long id;
    private String nome;
    private Endereco endereco;

    public ClienteResponseDTO() {
    }

    public ClienteResponseDTO(Long id, String nome, Endereco endereco) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
    }

    public ClienteResponseDTO(Cliente cliente) {
        if (cliente != null) {
            this.id = cliente.getId();
            this.nome = cliente.getNome();
            this.endereco = cliente.getEndereco();
        }
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

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
}
