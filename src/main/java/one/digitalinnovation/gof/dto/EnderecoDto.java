package one.digitalinnovation.gof.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * DTO (record) do endereço, com validação de entrada via Bean Validation.
 * Records são o padrão atual do Java para transportadores imutáveis de dados.
 *
 * @author falvojr
 */
public record EnderecoDto(

		@NotBlank(message = "CEP é obrigatório")
		@Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP inválido")
		String cep,

		String logradouro,
		String complemento,
		String bairro,
		String localidade,
		String uf,
		String ibge,
		String gia,
		String ddd,
		String siafi) {
}