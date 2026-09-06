package one.digitalinnovation.gof.factory;

import org.springframework.stereotype.Component;

import one.digitalinnovation.gof.dto.ClienteRequestDTO;
import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.TipoCliente;

import java.time.LocalDateTime;

/**
 * Padrão Factory Method: Encapsula a criação complexa de instâncias da entidade Cliente.
 */
@Component
public class ClienteFactory {

    public Cliente criarCliente(ClienteRequestDTO dto, Endereco endereco, String strategyUtilizada) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome().trim());
        cliente.setCpf(dto.getCpf().replaceAll("\\D", ""));
        cliente.setEmail(dto.getEmail().toLowerCase().trim());
        cliente.setTipoCliente(dto.getTipoCliente() != null ? dto.getTipoCliente() : TipoCliente.PESSOA_FISICA);
        cliente.setEndereco(endereco);
        cliente.setCepStrategyUtilizada(strategyUtilizada);
        cliente.setDataCadastro(LocalDateTime.now());

        return cliente;
    }
}
