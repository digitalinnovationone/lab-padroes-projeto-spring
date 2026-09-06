package one.digitalinnovation.gof.service.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import one.digitalinnovation.gof.model.Endereco;

@Component("fallbackCep")
public class FallbackCepStrategy implements CepStrategy {

    private static final Logger log = LoggerFactory.getLogger(FallbackCepStrategy.class);

    @Override
    public Endereco consultarCep(String cep) {
        String cleanCep = cep.replaceAll("\\D", "");
        log.info("[FallbackCepStrategy] Gerando endereço mock/fallback para o CEP: {}", cleanCep);

        Endereco endereco = new Endereco();
        endereco.setCep(cleanCep);
        endereco.setLogradouro("Avenida Paulista (Fallback / Mock)");
        endereco.setComplemento("Edifício Central");
        endereco.setBairro("Bela Vista");
        endereco.setLocalidade("São Paulo");
        endereco.setUf("SP");
        endereco.setDdd("11");

        return endereco;
    }

    @Override
    public String getStrategyName() {
        return "FallbackCepStrategy";
    }
}
