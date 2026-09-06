package one.digitalinnovation.gof.service.strategy;

import one.digitalinnovation.gof.model.Endereco;

/**
 * Padrão Strategy: Interface para diferentes algoritmos/provedores de consulta de CEP.
 */
public interface CepStrategy {
    Endereco consultarCep(String cep);
    String getStrategyName();
}
