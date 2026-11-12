package one.digitalinnovation.gof.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.service.ClienteService;

/**
 * Esse {@link RestController} representa nossa <b>Facade</b>, pois abstrai toda
 * a complexidade de integrações (Banco de Dados H2 e API do ViaCEP) em uma
 * interface simples e coesa (API REST).
 * 
 * @author falvojr
 */
@RestController
@RequestMapping("clientes")
@Tag(name = "Clientes", description = "Endpoints para gerenciamento de endereço clientes")
public class ClienteRestController {

	private final ClienteService clienteService;

	@Autowired
	public ClienteRestController(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	@GetMapping
	@Operation(summary = "Lista todos os clientes", description = "Retorna uma lista completa contendo todos os clientes cadastrados e seus respectivos endereços.")
	public ResponseEntity<Iterable<Cliente>> buscarTodos() {
		return ResponseEntity.ok(clienteService.buscarTodos());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Busca cliente por ID", description = "Retorna os detalhes de um cliente específico baseado no ID fornecido.")
	public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
		return ResponseEntity.ok(clienteService.buscarPorId(id));
	}

	@PostMapping
	@Operation(summary = "Insere um novo cliente", description = "Cria um novo cliente com os detalhes fornecidos.")
	public ResponseEntity<Cliente> inserir(@RequestBody Cliente cliente) {
		clienteService.inserir(cliente);
		return ResponseEntity.ok(cliente);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualiza um cliente existente", description = "Atualiza os detalhes de um cliente existente com base no ID fornecido.")
	public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
		clienteService.atualizar(id, cliente);
		return ResponseEntity.ok(cliente);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Deleta um cliente existente", description = "Remove um cliente existente com base no ID fornecido.")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		clienteService.deletar(id);
		return ResponseEntity.ok().build();
	}
}
