package one.digitalinnovation.gof.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Schema(description = "Entidade que representa um Cliente no sistema")
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Schema(description = "Identificador único do cliente", example = "1" , accessMode = Schema.AccessMode.READ_ONLY)
	private Long id;

	@Schema(description = "Nome completo do cliente", example = "Tony Stark")
	private String nome;

	@ManyToOne
	@Schema(description = "Endereço do cliente")
	private Endereco endereco;
}
