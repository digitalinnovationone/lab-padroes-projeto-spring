package one.digitalinnovation.gof.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import one.digitalinnovation.gof.dto.ClienteRequestDTO;
import one.digitalinnovation.gof.dto.ClienteResponseDTO;
import one.digitalinnovation.gof.service.ClienteService;

/**
 * Esse {@link RestController} representa nossa <b>Facade</b>, pois abstrai toda
 * a complexidade de integrações (Banco de Dados H2 e API do ViaCEP) em uma
 * interface simples e coesa (API REST).
 * 
 * @author falvojr
 */
@Tag(name = "Clientes", description = "Operações para gerenciamento de clientes e consulta de endereços via CEP")
@RestController
@RequestMapping("clientes")
public class ClienteRestController {

	private final ClienteService clienteService;

	public ClienteRestController(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	@Operation(summary = "Listar todos os clientes", description = "Retorna uma lista com todos os clientes cadastrados e seus respectivos endereços.")
	@ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso")
	@GetMapping
	public ResponseEntity<Iterable<ClienteResponseDTO>> buscarTodos() {
		return ResponseEntity.ok(clienteService.buscarTodos());
	}

	@Operation(summary = "Buscar cliente por ID", description = "Retorna os dados detalhados de um cliente a partir do seu identificador único.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso"),
		@ApiResponse(responseCode = "404", description = "Cliente não encontrado para o ID informado")
	})
	@GetMapping("/{id}")
	public ResponseEntity<ClienteResponseDTO> buscarPorId(
			@Parameter(description = "ID do cliente", example = "1", required = true) @PathVariable Long id) {
		return ResponseEntity.ok(clienteService.buscarPorId(id));
	}

	@Operation(summary = "Cadastrar novo cliente", description = "Cadastra um novo cliente e preenche os dados de endereço automaticamente através do CEP informado.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Cliente cadastrado com sucesso"),
		@ApiResponse(responseCode = "400", description = "Requisição inválida")
	})
	@PostMapping
	public ResponseEntity<ClienteResponseDTO> inserir(@RequestBody ClienteRequestDTO cliente) {
		ClienteResponseDTO novoCliente = clienteService.inserir(cliente);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(novoCliente.getId())
				.toUri();
		return ResponseEntity.created(uri).body(novoCliente);
	}

	@Operation(summary = "Atualizar cliente existente", description = "Atualiza os dados de um cliente existente. Caso um novo CEP seja informado, o endereço é reprocessado.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
		@ApiResponse(responseCode = "404", description = "Cliente não encontrado para o ID informado")
	})
	@PutMapping("/{id}")
	public ResponseEntity<ClienteResponseDTO> atualizar(
			@Parameter(description = "ID do cliente a ser atualizado", example = "1", required = true) @PathVariable Long id, 
			@RequestBody ClienteRequestDTO cliente) {
		return ResponseEntity.ok(clienteService.atualizar(id, cliente));
	}

	@Operation(summary = "Excluir cliente", description = "Remove o registro de um cliente da base de dados.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "Cliente excluído com sucesso"),
		@ApiResponse(responseCode = "404", description = "Cliente não encontrado para o ID informado")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(
			@Parameter(description = "ID do cliente a ser excluído", example = "1", required = true) @PathVariable Long id) {
		clienteService.deletar(id);
		return ResponseEntity.noContent().build();
	}
}
