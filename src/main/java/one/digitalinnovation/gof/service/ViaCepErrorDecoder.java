package one.digitalinnovation.gof.service;

import org.springframework.stereotype.Component;

import feign.Response;
import feign.codec.ErrorDecoder;
import one.digitalinnovation.gof.exception.NotFoundException;
import one.digitalinnovation.gof.exception.ViaCepException;

/**
 * Decodifica erros HTTP da API ViaCEP em exceções de domínio:
 * <ul>
 * <li>404 - CEP não encontrado no ViaCEP -&gt; {@link NotFoundException} (HTTP 404)</li>
 * <li>demais erros/falhas -&gt; {@link ViaCepException} (HTTP 502)</li>
 * </ul>
 *
 * @author falvojr
 */
@Component
public class ViaCepErrorDecoder implements ErrorDecoder {

	@Override
	public Exception decode(String methodKey, Response response) {
		if (response.status() == 404) {
			return new NotFoundException("CEP não encontrado na API ViaCEP.");
		}
		return new ViaCepException("Falha na integração com a API ViaCEP (HTTP " + response.status() + ").");
	}
}