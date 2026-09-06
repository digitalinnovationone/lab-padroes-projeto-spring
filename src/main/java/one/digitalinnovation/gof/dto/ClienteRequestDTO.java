package one.digitalinnovation.gof.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import one.digitalinnovation.gof.model.TipoCliente;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Schema(description = "DTO de requisição para criação/atualização de Cliente")
public class ClienteRequestDTO {

    @Schema(description = "Nome completo do cliente", example = "João da Silva", required = true)
    @NotBlank(message = "O nome do cliente é obrigatório")
    private String nome;

    @Schema(description = "CPF do cliente (somente números ou formatado)", example = "123.456.789-00", required = true)
    @NotBlank(message = "O CPF é obrigatório")
    private String cpf;

    @Schema(description = "E-mail de contato do cliente", example = "joao.silva@email.com", required = true)
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    private String email;

    @Schema(description = "Tipo do cliente (PESSOA_FISICA ou PESSOA_JURIDICA)", example = "PESSOA_FISICA", required = true)
    @NotNull(message = "O tipo do cliente é obrigatório")
    private TipoCliente tipoCliente;

    @Schema(description = "CEP de residência/sede do cliente", example = "01001-000", required = true)
    @NotBlank(message = "O CEP é obrigatório")
    @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "Formato de CEP inválido. Use XXXXX-XXX ou XXXXXXXX")
    private String cep;

    @Schema(description = "Nome da estratégia de busca de CEP a utilizar (Opcional: viaCep, fallbackCep)", example = "viaCep")
    private String cepStrategy;

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

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCepStrategy() {
        return cepStrategy;
    }

    public void setCepStrategy(String cepStrategy) {
        this.cepStrategy = cepStrategy;
    }
}
