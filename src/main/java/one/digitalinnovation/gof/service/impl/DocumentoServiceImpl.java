package one.digitalinnovation.gof.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import one.digitalinnovation.gof.model.Documento;
import one.digitalinnovation.gof.repository.DocumentoRepository;
import one.digitalinnovation.gof.service.DocumentoService;

@Service
public class DocumentoServiceImpl implements DocumentoService {
	
	@Autowired
	private DocumentoRepository documentoRepository;
	
	@Override
	public Iterable<Documento> buscarTodos() {
		return documentoRepository.findAll();
	}

	@Override
	public Documento buscarPorId(Long id) {
		Optional<Documento> documento = documentoRepository.findById(id);
		return documento.get();
	}

	@Override
	public void inserir(Documento documento) {
		documentoRepository.save(documento);
	}

	@Override
	public void atualizar(Long id, Documento documento) {
		Optional<Documento> documentoExiste = documentoRepository.findById(id);
		if (documentoExiste.isPresent()) {
			documentoRepository.save(documento);
		}
	}

	@Override
	public void deletar(Long id) {
		documentoRepository.deleteById(id);
	}

}
