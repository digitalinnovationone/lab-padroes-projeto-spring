package one.digitalinnovation.gof.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.service.ClienteService;
import tools.jackson.databind.json.JsonMapper;

@SpringBootTest
@AutoConfigureMockMvc
class ClienteRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    private JsonMapper jsonMapper;
    private Cliente cliente;
    private final Long CLIENTE_ID = 1L;

    @BeforeEach
    void setUp() {
        jsonMapper = new JsonMapper();

        cliente = new Cliente();
        cliente.setId(CLIENTE_ID);
        cliente.setNome("Maria Oliveira");
    }

    @Test
    @DisplayName("Deve retornar 200 OK e a lista de clientes ao buscar todos")
    void buscarTodos_DeveRetornarStatus200_EListaDeClientes() throws Exception {
        when(clienteService.buscarTodos()).thenReturn(List.of(cliente));

        mockMvc.perform(get("/clientes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(CLIENTE_ID))
                .andExpect(jsonPath("$[0].nome").value("Maria Oliveira"));
    }

    @Test
    @DisplayName("Deve retornar 200 OK e o cliente correspondente ao ID informado")
    void buscarPorId_DeveRetornarStatus200_EClienteInformado() throws Exception {
        when(clienteService.buscarPorId(CLIENTE_ID)).thenReturn(cliente);

        mockMvc.perform(get("/clientes/{id}", CLIENTE_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CLIENTE_ID))
                .andExpect(jsonPath("$.nome").value("Maria Oliveira"));
    }

    @Test
    @DisplayName("Deve retornar 200 OK e o cliente inserido no método POST")
    void inserir_DeveRetornarStatus200_EClienteInserido() throws Exception {
        doNothing().when(clienteService).inserir(any(Cliente.class));

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(cliente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CLIENTE_ID))
                .andExpect(jsonPath("$.nome").value("Maria Oliveira"));
    }

    @Test
    @DisplayName("Deve retornar 200 OK e o cliente atualizado no método PUT")
    void atualizar_DeveRetornarStatus200_EClienteAtualizado() throws Exception {
        doNothing().when(clienteService).atualizar(eq(CLIENTE_ID), any(Cliente.class));

        mockMvc.perform(put("/clientes/{id}", CLIENTE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(cliente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CLIENTE_ID))
                .andExpect(jsonPath("$.nome").value("Maria Oliveira"));
    }

    @Test
    @DisplayName("Deve retornar 200 OK sem corpo de resposta no método DELETE")
    void deletar_DeveRetornarStatus200() throws Exception {
        doNothing().when(clienteService).deletar(CLIENTE_ID);

        mockMvc.perform(delete("/clientes/{id}", CLIENTE_ID))
                .andExpect(status().isOk());
    }
}