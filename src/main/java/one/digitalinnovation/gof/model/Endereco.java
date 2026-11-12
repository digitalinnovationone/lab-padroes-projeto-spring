package one.digitalinnovation.gof.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter
public class Endereco {

	@Id
	@Schema(description = "CEP para busca automática no ViaCEP", example = "01001000")
	private String cep;

	@Schema(description = "Logradouro preenchido automaticamente", accessMode = Schema.AccessMode.READ_ONLY)
	private String logradouro;

	@Schema(description = "Complemento preenchido automaticamente", accessMode = Schema.AccessMode.READ_ONLY)
	private String complemento;

	@Schema(description = "Bairro preenchido automaticamente", accessMode = Schema.AccessMode.READ_ONLY)
	private String bairro;

	@Schema(description = "Localidade preenchida automaticamente", accessMode = Schema.AccessMode.READ_ONLY)
	private String localidade;

	@Schema(description = "UF preenchida automaticamente", accessMode = Schema.AccessMode.READ_ONLY)
	private String uf;

	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private String ibge;

	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private String gia;

	@Schema(description = "DDD preenchido automaticamente", accessMode = Schema.AccessMode.READ_ONLY)
	private String ddd;

	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private String siafi;

}
