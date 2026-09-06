package one.digitalinnovation.gof.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import one.digitalinnovation.gof.dto.ClienteRequestDTO;
import one.digitalinnovation.gof.model.TipoCliente;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ClienteRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/v1/clientes - Deve retornar HTTP 200 OK")
    void deveListarClientesComSucesso() throws Exception {
        mockMvc.perform(get("/api/v1/clientes"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/v1/clientes - Deve criar cliente com sucesso usando FallbackCepStrategy")
    void deveCriarClienteComSucesso() throws Exception {
        ClienteRequestDTO dto = new ClienteRequestDTO();
        dto.setNome("Carlos Eduardo");
        dto.setCpf("52998224725");
        dto.setEmail("carlos.eduardo@empresa.com");
        dto.setTipoCliente(TipoCliente.PESSOA_FISICA);
        dto.setCep("01001-000");
        dto.setCepStrategy("fallbackCep");

        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Carlos Eduardo"))
                .andExpect(jsonPath("$.cepStrategyUtilizada").value("FallbackCepStrategy"));
    }

    @Test
    @DisplayName("POST /api/v1/clientes - Deve retornar 400 Bad Request ao passar dados inválidos")
    void deveRetornar400AoPassarDadosInvalidos() throws Exception {
        ClienteRequestDTO dto = new ClienteRequestDTO();
        dto.setNome(""); // Nome em branco
        dto.setCpf("123"); // CPF inválido
        dto.setEmail("email-invalido"); // E-mail inválido

        mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
