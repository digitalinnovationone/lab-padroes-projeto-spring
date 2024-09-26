package one.digitalinnovation.gof.service;

import one.digitalinnovation.gof.model.Documento;

public interface DocumentoService {
	
	Iterable<Documento> buscarTodos();

	Documento buscarPorId(Long id);

	void inserir(Documento documento);

	void atualizar(Long id, Documento documento);

	void deletar(Long id);
}
