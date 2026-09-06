package one.digitalinnovation.gof.service.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import one.digitalinnovation.gof.model.Endereco;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CepStrategyTest {

    @Mock
    private ViaCepStrategy viaCepStrategy;

    private FallbackCepStrategy fallbackCepStrategy;
    private CepStrategyFactory factory;

    @BeforeEach
    void setUp() {
        fallbackCepStrategy = new FallbackCepStrategy();
        Map<String, CepStrategy> strategies = new HashMap<>();
        strategies.put("viaCep", viaCepStrategy);
        strategies.put("fallbackCep", fallbackCepStrategy);

        factory = new CepStrategyFactory(strategies);
    }

    @Test
    @DisplayName("Deve retornar FallbackCepStrategy quando solicitada explicitamente")
    void deveRetornarFallbackCepStrategy() {
        CepStrategy strategy = factory.getStrategy("fallbackCep");
        assertNotNull(strategy);
        assertEquals("FallbackCepStrategy", strategy.getStrategyName());
    }

    @Test
    @DisplayName("Deve retornar dados válidos da FallbackCepStrategy")
    void deveRetornarEnderecoValidoNoFallback() {
        Endereco endereco = fallbackCepStrategy.consultarCep("01001-000");
        assertNotNull(endereco);
        assertEquals("01001000", endereco.getCep());
        assertEquals("SP", endereco.getUf());
    }
}
