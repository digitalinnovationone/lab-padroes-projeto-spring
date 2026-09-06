package one.digitalinnovation.gof.service.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.service.ViaCepService;

@Component("viaCep")
public class ViaCepStrategy implements CepStrategy {

    private static final Logger log = LoggerFactory.getLogger(ViaCepStrategy.class);

    @Autowired
    private ViaCepService viaCepService;

    @Override
    public Endereco consultarCep(String cep) {
        String cleanCep = cep.replaceAll("\\D", "");
        log.info("[ViaCepStrategy] Consultando CEP: {}", cleanCep);
        Endereco endereco = viaCepService.consultarCep(cleanCep);
        if (endereco != null && endereco.getCep() == null) {
            // Se o ViaCEP retornar JSON em branco/nulo
            endereco.setCep(cleanCep);
        }
        return endereco;
    }

    @Override
    public String getStrategyName() {
        return "ViaCepStrategy";
    }
}
