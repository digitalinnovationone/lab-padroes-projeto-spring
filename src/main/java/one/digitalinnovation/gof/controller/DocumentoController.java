package one.digitalinnovation.gof.controller;

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

import one.digitalinnovation.gof.model.Documento;
import one.digitalinnovation.gof.service.DocumentoService;

@RestController
@RequestMapping("documento")
public class DocumentoController {
	
	@Autowired
	private DocumentoService documentoService;
	
	@GetMapping
	public ResponseEntity<Iterable<Documento>> buscarTodos() {
		return ResponseEntity.ok(documentoService.buscarTodos());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Documento> buscarPorId(@PathVariable Long id) {
		return ResponseEntity.ok(documentoService.buscarPorId(id));
	}
	
	@PostMapping
	public ResponseEntity<Documento> inserir(@RequestBody Documento documento) {
		documentoService.inserir(documento);
		return ResponseEntity.ok(documento);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Documento> atualizar(@PathVariable Long id, @RequestBody Documento documento) {
		documentoService.atualizar(id, documento);
		return ResponseEntity.ok(documento);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Documento> deletar(@PathVariable Long id) {
		documentoService.deletar(id);
		return ResponseEntity.ok().build();
	}
}
