package one.digitalinnovation.gof.service;

import one.digitalinnovation.gof.dto.ClienteRequestDTO;
import one.digitalinnovation.gof.dto.ClienteResponseDTO;

/**
 * Padrão Facade: Interface unificada de alto nível que simplifica e orquestra o subsistema de Clientes.
 */
public interface ClienteFacade {

    Iterable<ClienteResponseDTO> buscarTodos();

    ClienteResponseDTO buscarPorId(Long id);

    ClienteResponseDTO inserir(ClienteRequestDTO requestDTO);

    ClienteResponseDTO atualizar(Long id, ClienteRequestDTO requestDTO);

    void deletar(Long id);
}
