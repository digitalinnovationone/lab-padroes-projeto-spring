package one.digitalinnovation.gof.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO (record) do cliente, desacoplando a API REST das entidades JPA.
 * A validação de entrada acontece aqui, na borda da aplicação.
 *
 * @author falvojr
 */
public record ClienteDto(

		Long id,

		@NotBlank(message = "Nome é obrigatório")
		String nome,

		@NotNull(message = "Endereço é obrigatório")
		@Valid
		EnderecoDto endereco) {
}