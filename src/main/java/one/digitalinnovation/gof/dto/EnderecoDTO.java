package one.digitalinnovation.gof.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO representando as informações de Endereço")
public class EnderecoDTO {

    @Schema(description = "Código de Endereçamento Postal (CEP)", example = "01001000")
    private String cep;

    @Schema(description = "Logradouro / Rua", example = "Praça da Sé")
    private String logradouro;

    @Schema(description = "Complemento", example = "lado ímpar")
    private String complemento;

    @Schema(description = "Bairro", example = "Sé")
    private String bairro;

    @Schema(description = "Localidade / Cidade", example = "São Paulo")
    private String localidade;

    @Schema(description = "Unidade Federativa (Estado)", example = "SP")
    private String uf;

    @Schema(description = "DDD da região", example = "11")
    private String ddd;

    public EnderecoDTO() {
    }

    public EnderecoDTO(String cep, String logradouro, String complemento, String bairro, String localidade, String uf, String ddd) {
        this.cep = cep;
        this.logradouro = logradouro;
        this.complemento = complemento;
        this.bairro = bairro;
        this.localidade = localidade;
        this.uf = uf;
        this.ddd = ddd;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }
}
