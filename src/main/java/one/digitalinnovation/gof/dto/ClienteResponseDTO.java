package one.digitalinnovation.gof.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import one.digitalinnovation.gof.model.TipoCliente;

import java.time.LocalDateTime;

@Schema(description = "DTO de resposta contendo os dados consolidados do Cliente")
public class ClienteResponseDTO {

    @Schema(description = "ID único do cliente no sistema", example = "1")
    private Long id;

    @Schema(description = "Nome completo do cliente", example = "João da Silva")
    private String nome;

    @Schema(description = "CPF do cliente", example = "123.456.789-00")
    private String cpf;

    @Schema(description = "E-mail de contato", example = "joao.silva@email.com")
    private String email;

    @Schema(description = "Tipo do cliente", example = "PESSOA_FISICA")
    private TipoCliente tipoCliente;

    @Schema(description = "Percentual de desconto padrão associado ao perfil", example = "0.05")
    private double descontoPadrao;

    @Schema(description = "Endereço completo resolvido")
    private EnderecoDTO endereco;

    @Schema(description = "Estratégia de CEP utilizada na resolução", example = "ViaCepStrategy")
    private String cepStrategyUtilizada;

    @Schema(description = "Data e hora do cadastro", example = "2026-09-06T14:30:00")
    private LocalDateTime dataCadastro;

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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public double getDescontoPadrao() {
        return descontoPadrao;
    }

    public void setDescontoPadrao(double descontoPadrao) {
        this.descontoPadrao = descontoPadrao;
    }

    public EnderecoDTO getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoDTO endereco) {
        this.endereco = endereco;
    }

    public String getCepStrategyUtilizada() {
        return cepStrategyUtilizada;
    }

    public void setCepStrategyUtilizada(String cepStrategyUtilizada) {
        this.cepStrategyUtilizada = cepStrategyUtilizada;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
}
