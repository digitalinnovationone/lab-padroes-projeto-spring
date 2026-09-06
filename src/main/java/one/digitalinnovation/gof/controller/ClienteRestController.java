package one.digitalinnovation.gof.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import one.digitalinnovation.gof.dto.ClienteRequestDTO;
import one.digitalinnovation.gof.dto.ClienteResponseDTO;
import one.digitalinnovation.gof.service.ClienteFacade;

import javax.validation.Valid;

/**
 * REST Controller representando a <b>Facade</b> externa da aplicação.
 * Abstrai integrações complexas (Banco H2, APIs externas, Chain de Validação) em um contrato REST padronizado.
 */
@RestController
@RequestMapping("/api/v1/clientes")
@Tag(name = "Clientes", description = "Endpoints para gerenciamento completo de clientes e resolução dinâmica de CEPs")
public class ClienteRestController {

	@Autowired
	private ClienteFacade clienteFacade;

	@Operation(summary = "Listar todos os clientes", description = "Retorna uma lista com todos os clientes cadastrados e seus respectivos endereços.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso")
	})
	@GetMapping
	public ResponseEntity<Iterable<ClienteResponseDTO>> buscarTodos() {
		return ResponseEntity.ok(clienteFacade.buscarTodos());
	}

	@Operation(summary = "Buscar cliente por ID", description = "Recupera os detalhes de um cliente específico através do seu ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Cliente encontrado"),
			@ApiResponse(responseCode = "404", description = "Cliente não encontrado")
	})
	@GetMapping("/{id}")
	public ResponseEntity<ClienteResponseDTO> buscarPorId(
			@Parameter(description = "ID do cliente", example = "1") @PathVariable Long id) {
		return ResponseEntity.ok(clienteFacade.buscarPorId(id));
	}

	@Operation(summary = "Cadastrar novo cliente", description = "Valida os dados via Chain of Responsibility, resolve o CEP usando a Strategy informada (ou ViaCEP por padrão) e salva o cliente.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Erro de validação de dados ou CEP inválido"),
			@ApiResponse(responseCode = "409", description = "Conflito: CPF ou E-mail já cadastrado")
	})
	@PostMapping
	public ResponseEntity<ClienteResponseDTO> inserir(@Valid @RequestBody ClienteRequestDTO dto) {
		ClienteResponseDTO novoCliente = clienteFacade.inserir(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
	}

	@Operation(summary = "Atualizar cliente existente", description = "Atualiza os dados de um cliente existente pelo ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados de requisição inválidos"),
			@ApiResponse(responseCode = "404", description = "Cliente não encontrado")
	})
	@PutMapping("/{id}")
	public ResponseEntity<ClienteResponseDTO> atualizar(
			@Parameter(description = "ID do cliente", example = "1") @PathVariable Long id,
			@Valid @RequestBody ClienteRequestDTO dto) {
		ClienteResponseDTO clienteAtualizado = clienteFacade.atualizar(id, dto);
		return ResponseEntity.ok(clienteAtualizado);
	}

	@Operation(summary = "Deletar cliente por ID", description = "Remove o cadastro de um cliente do sistema.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Cliente removido com sucesso"),
			@ApiResponse(responseCode = "404", description = "Cliente não encontrado")
	})
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(
			@Parameter(description = "ID do cliente", example = "1") @PathVariable Long id) {
		clienteFacade.deletar(id);
		return ResponseEntity.noContent().build();
	}
}
