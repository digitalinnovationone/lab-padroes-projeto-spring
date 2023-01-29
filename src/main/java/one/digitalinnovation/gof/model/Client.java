package one.digitalinnovation.gof.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

/**
 * Classe {@link Client} representa um cliente no banco de dados.
 * 
 * @author falvojr
 * @author edielson-assis
 */
@Data
@Entity
public class Client {

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
	private Long id;
	private String name;
	
	@ManyToOne
	private Address address;
}