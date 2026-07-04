package one.digitalinnovation.gof.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

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

import one.digitalinnovation.gof.dto.ClienteRequest;
import one.digitalinnovation.gof.dto.ClienteResponse;
import one.digitalinnovation.gof.mapper.ClienteMapper;
import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.service.ClienteService;

/**
 * <b>Facade</b> REST do domínio de cliente: abstrai as integrações (H2 e ViaCEP)
 * em uma interface simples e coesa, trabalhando com DTOs validados.
 */
@RestController
@RequestMapping("clientes")
public class ClienteRestController {

	private final ClienteService clienteService;
	private final ClienteMapper clienteMapper;

	public ClienteRestController(ClienteService clienteService, ClienteMapper clienteMapper) {
		this.clienteService = clienteService;
		this.clienteMapper = clienteMapper;
	}

	@GetMapping
	public ResponseEntity<List<ClienteResponse>> buscarTodos() {
		List<ClienteResponse> clientes = new ArrayList<>();
		clienteService.buscarTodos().forEach(cliente -> clientes.add(clienteMapper.toResponse(cliente)));
		return ResponseEntity.ok(clientes);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long id) {
		Cliente cliente = clienteService.buscarPorId(id);
		return ResponseEntity.ok(clienteMapper.toResponse(cliente));
	}

	@PostMapping
	public ResponseEntity<ClienteResponse> inserir(@RequestBody @Valid ClienteRequest request) {
		Cliente cliente = clienteMapper.toEntity(request);
		clienteService.inserir(cliente);
		return ResponseEntity.status(HttpStatus.CREATED).body(clienteMapper.toResponse(cliente));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ClienteResponse> atualizar(@PathVariable Long id, @RequestBody @Valid ClienteRequest request) {
		Cliente cliente = clienteMapper.toEntity(request);
		clienteService.atualizar(id, cliente);
		return ResponseEntity.ok(clienteMapper.toResponse(cliente));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Long id) {
		clienteService.deletar(id);
		return ResponseEntity.noContent().build();
	}
}
