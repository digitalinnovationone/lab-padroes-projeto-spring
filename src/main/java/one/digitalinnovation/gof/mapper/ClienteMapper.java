package one.digitalinnovation.gof.mapper;

import org.springframework.stereotype.Component;

import one.digitalinnovation.gof.dto.ClienteRequest;
import one.digitalinnovation.gof.dto.ClienteResponse;
import one.digitalinnovation.gof.dto.EnderecoResponse;
import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.Endereco;

@Component
public class ClienteMapper {

	public Cliente toEntity(ClienteRequest request) {
		Endereco endereco = Endereco.builder()
				.cep(request.getCep())
				.build();
		return Cliente.builder()
				.nome(request.getNome())
				.email(request.getEmail())
				.endereco(endereco)
				.build();
	}

	public ClienteResponse toResponse(Cliente cliente) {
		ClienteResponse response = new ClienteResponse();
		response.setId(cliente.getId());
		response.setNome(cliente.getNome());
		response.setEmail(cliente.getEmail());
		response.setEndereco(toEnderecoResponse(cliente.getEndereco()));
		return response;
	}

	private EnderecoResponse toEnderecoResponse(Endereco endereco) {
		if (endereco == null) {
			return null;
		}
		EnderecoResponse response = new EnderecoResponse();
		response.setCep(endereco.getCep());
		response.setLogradouro(endereco.getLogradouro());
		response.setBairro(endereco.getBairro());
		response.setLocalidade(endereco.getLocalidade());
		response.setUf(endereco.getUf());
		return response;
	}
}
