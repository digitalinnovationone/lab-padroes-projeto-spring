package one.digitalinnovation.gof.service;

import java.util.List;

import one.digitalinnovation.gof.dto.ClienteDto;

/**
 * Interface que define o padrão <b>Strategy</b> no domínio de cliente. Com
 * isso, se necessário, podemos ter multiplas implementações dessa mesma
 * interface.
 * 
 * @author falvojr
 */
public interface ClienteService {

	List<ClienteDto> buscarTodos();

	ClienteDto buscarPorId(Long id);

	ClienteDto inserir(ClienteDto cliente);

	ClienteDto atualizar(Long id, ClienteDto cliente);

	void deletar(Long id);

}