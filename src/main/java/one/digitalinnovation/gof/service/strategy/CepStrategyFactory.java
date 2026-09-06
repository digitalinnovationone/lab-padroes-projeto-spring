package one.digitalinnovation.gof.service.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Padrão Strategy + Factory: Gerencia e fornece dynamicamente a estratégia de CEP solicitada.
 */
@Component
public class CepStrategyFactory {

    private static final Logger log = LoggerFactory.getLogger(CepStrategyFactory.class);

    private final Map<String, CepStrategy> strategies;

    @Autowired
    public CepStrategyFactory(Map<String, CepStrategy> strategies) {
        this.strategies = strategies;
    }

    public CepStrategy getStrategy(String strategyName) {
        if (strategyName == null || strategyName.isBlank()) {
            return strategies.getOrDefault("viaCep", strategies.get("fallbackCep"));
        }

        CepStrategy strategy = strategies.get(strategyName);
        if (strategy == null) {
            log.warn("Estratégia de CEP '{}' não encontrada. Utilizando 'viaCep' padrão.", strategyName);
            return strategies.getOrDefault("viaCep", strategies.get("fallbackCep"));
        }

        return strategy;
    }
}
