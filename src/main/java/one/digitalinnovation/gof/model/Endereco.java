package one.digitalinnovation.gof.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Os atributos desse modelo foram gerados automaticamente pelo site
 * jsonschema2pojo.org. Para isso, usamos o JSON de retorno da API do ViaCEP.
 * 
 * @see <a href="https://www.jsonschema2pojo.org">jsonschema2pojo.org</a>
 * @see <a href="https://viacep.com.br">ViaCEP</a>
 * 
 * @author falvojr
 */
@Entity
public class Endereco {

	@Id
	private String cep;
	private String logradouro;
	private String complemento;
	private String bairro;
	private String localidade;
	private String uf;
	private String ibge;
	private String gia;
	private String ddd;
	private String siafi;

	public Endereco() {
	}

	private Endereco(Builder builder) {
		this.cep = builder.cep;
		this.logradouro = builder.logradouro;
		this.complemento = builder.complemento;
		this.bairro = builder.bairro;
		this.localidade = builder.localidade;
		this.uf = builder.uf;
		this.ibge = builder.ibge;
		this.gia = builder.gia;
		this.ddd = builder.ddd;
		this.siafi = builder.siafi;
	}

	public static Builder builder() {
		return new Builder();
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

	public String getIbge() {
		return ibge;
	}

	public void setIbge(String ibge) {
		this.ibge = ibge;
	}

	public String getGia() {
		return gia;
	}

	public void setGia(String gia) {
		this.gia = gia;
	}

	public String getDdd() {
		return ddd;
	}

	public void setDdd(String ddd) {
		this.ddd = ddd;
	}

	public String getSiafi() {
		return siafi;
	}

	public void setSiafi(String siafi) {
		this.siafi = siafi;
	}

	public static class Builder {

		private String cep;
		private String logradouro;
		private String complemento;
		private String bairro;
		private String localidade;
		private String uf;
		private String ibge;
		private String gia;
		private String ddd;
		private String siafi;

		public Builder cep(String cep) {
			this.cep = cep;
			return this;
		}

		public Builder logradouro(String logradouro) {
			this.logradouro = logradouro;
			return this;
		}

		public Builder complemento(String complemento) {
			this.complemento = complemento;
			return this;
		}

		public Builder bairro(String bairro) {
			this.bairro = bairro;
			return this;
		}

		public Builder localidade(String localidade) {
			this.localidade = localidade;
			return this;
		}

		public Builder uf(String uf) {
			this.uf = uf;
			return this;
		}

		public Builder ibge(String ibge) {
			this.ibge = ibge;
			return this;
		}

		public Builder gia(String gia) {
			this.gia = gia;
			return this;
		}

		public Builder ddd(String ddd) {
			this.ddd = ddd;
			return this;
		}

		public Builder siafi(String siafi) {
			this.siafi = siafi;
			return this;
		}

		public Endereco build() {
			return new Endereco(this);
		}
	}

}
