package one.digitalinnovation.gof.service;

import org.springframework.stereotype.Component;

import one.digitalinnovation.gof.dto.ClienteDto;
import one.digitalinnovation.gof.dto.EnderecoDto;
import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.Endereco;

/**
 * Mapeia entre as entidades JPA (model) e os DTOs expostos na API.
 *
 * @author falvojr
 */
@Component
public class ClienteMapper {

	public ClienteDto toDto(Cliente cliente) {
		return new ClienteDto(cliente.getId(), cliente.getNome(), toDto(cliente.getEndereco()));
	}

	public EnderecoDto toDto(Endereco endereco) {
		if (endereco == null) {
			return null;
		}
		return new EnderecoDto(
				endereco.getCep(),
				endereco.getLogradouro(),
				endereco.getComplemento(),
				endereco.getBairro(),
				endereco.getLocalidade(),
				endereco.getUf(),
				endereco.getIbge(),
				endereco.getGia(),
				endereco.getDdd(),
				endereco.getSiafi());
	}

	public Cliente toEntity(ClienteDto dto) {
		Cliente cliente = new Cliente();
		cliente.setId(dto.id());
		cliente.setNome(dto.nome());
		cliente.setEndereco(toEntity(dto.endereco()));
		return cliente;
	}

	public Endereco toEntity(EnderecoDto dto) {
		if (dto == null) {
			return null;
		}
		Endereco endereco = new Endereco();
		endereco.setCep(dto.cep());
		endereco.setLogradouro(dto.logradouro());
		endereco.setComplemento(dto.complemento());
		endereco.setBairro(dto.bairro());
		endereco.setLocalidade(dto.localidade());
		endereco.setUf(dto.uf());
		endereco.setIbge(dto.ibge());
		endereco.setGia(dto.gia());
		endereco.setDdd(dto.ddd());
		endereco.setSiafi(dto.siafi());
		return endereco;
	}
}