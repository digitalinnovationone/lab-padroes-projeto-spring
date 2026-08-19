package one.digitalinnovation.gof.service;

import one.digitalinnovation.gof.dto.ClienteRequestDTO;
import one.digitalinnovation.gof.dto.ClienteResponseDTO;

/**
 * Interface que define o padrão <b>Strategy</b> no domínio de cliente. Com
 * isso, se necessário, podemos ter multiplas implementações dessa mesma
 * interface.
 * 
 * @author falvojr
 */
public interface ClienteService {

	Iterable<ClienteResponseDTO> buscarTodos();

	ClienteResponseDTO buscarPorId(Long id);

	ClienteResponseDTO inserir(ClienteRequestDTO cliente);

	ClienteResponseDTO atualizar(Long id, ClienteRequestDTO cliente);

	void deletar(Long id);

}
