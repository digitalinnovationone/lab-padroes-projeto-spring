package one.digitalinnovation.gof.service.chain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import one.digitalinnovation.gof.dto.ClienteRequestDTO;
import one.digitalinnovation.gof.exception.ValidationException;
import one.digitalinnovation.gof.model.ClienteRepository;
import one.digitalinnovation.gof.model.TipoCliente;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteValidationChainTest {

    @Mock
    private ClienteRepository clienteRepository;

    private CpfValidationHandler cpfHandler;
    private EmailValidationHandler emailHandler;
    private CepValidationHandler cepHandler;

    @BeforeEach
    void setUp() {
        cpfHandler = new CpfValidationHandler();
        emailHandler = new EmailValidationHandler();
        cepHandler = new CepValidationHandler();

        cpfHandler.linkWith(emailHandler).linkWith(cepHandler);
    }

    @Test
    @DisplayName("Deve passar em todas as validações quando a requisição for válida")
    void devePassarNaValidacaoQuandoDadosValidos() {
        ClienteRequestDTO dto = new ClienteRequestDTO();
        dto.setNome("Maria Oliveira");
        // CPF válido com dígito verificador correto
        dto.setCpf("52998224725");
        dto.setEmail("maria.oliveira@email.com");
        dto.setTipoCliente(TipoCliente.PESSOA_FISICA);
        dto.setCep("01001-000");

        when(clienteRepository.findByCpf(anyString())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> cpfHandler.validate(dto, null, clienteRepository));
    }

    @Test
    @DisplayName("Deve lançar ValidationException quando o CPF tiver formato ou dígito inválido")
    void deveLancarExcecaoQuandoCpfInvalido() {
        ClienteRequestDTO dto = new ClienteRequestDTO();
        dto.setNome("Maria Oliveira");
        dto.setCpf("111.111.111-11"); // CPF inválido
        dto.setEmail("maria@email.com");
        dto.setTipoCliente(TipoCliente.PESSOA_FISICA);
        dto.setCep("01001-000");

        ValidationException ex = assertThrows(ValidationException.class,
                () -> cpfHandler.validate(dto, null, clienteRepository));

        assertTrue(ex.getMessage().contains("CPF"));
    }

    @Test
    @DisplayName("Deve lançar ValidationException quando o CEP tiver número de dígitos incorreto")
    void deveLancarExcecaoQuandoCepInvalido() {
        ClienteRequestDTO dto = new ClienteRequestDTO();
        dto.setNome("Maria Oliveira");
        dto.setCpf("52998224725");
        dto.setEmail("maria@email.com");
        dto.setTipoCliente(TipoCliente.PESSOA_FISICA);
        dto.setCep("123"); // CEP inválido (menos de 8 dígitos)

        ValidationException ex = assertThrows(ValidationException.class,
                () -> cpfHandler.validate(dto, null, clienteRepository));

        assertTrue(ex.getMessage().contains("CEP"));
    }
}
