package one.digitalinnovation.gof.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import one.digitalinnovation.gof.dto.ClienteDto;
import one.digitalinnovation.gof.dto.EnderecoDto;
import one.digitalinnovation.gof.exception.NotFoundException;
import one.digitalinnovation.gof.service.ClienteService;

@WebMvcTest(ClienteRestController.class)
class ClienteRestControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockitoBean
	private ClienteService clienteService;

	private final EnderecoDto endereco = new EnderecoDto("01001000", "Praça da Sé", null, "Sé", "São Paulo", "SP",
			null, null, null, null);

	@Test
	void inserir_deveRetornar201ComLocation() throws Exception {
		ClienteDto criado = new ClienteDto(1L, "Fulano", endereco);
		when(clienteService.inserir(any(ClienteDto.class))).thenReturn(criado);

		mockMvc.perform(post("/clientes")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new ClienteDto(null, "Fulano", endereco))))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "http://localhost/clientes/1"))
				.andExpect(jsonPath("$.id").value(1));
	}

	@Test
	void inserir_deveRetornar400_quandoNomeVazio() throws Exception {
		mockMvc.perform(post("/clientes")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new ClienteDto(null, "", endereco))))
				.andExpect(status().isBadRequest());
	}

	@Test
	void inserir_deveRetornar400_quandoCepInvalido() throws Exception {
		mockMvc.perform(post("/clientes")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(
						new ClienteDto(null, "Fulano", new EnderecoDto("123", null, null, null, null, null, null, null,
								null, null)))))
				.andExpect(status().isBadRequest());
	}

	@Test
	void buscarPorId_deveRetornar404_quandoClienteNaoExiste() throws Exception {
		doThrow(new NotFoundException("Cliente não encontrado com id: 99"))
				.when(clienteService).buscarPorId(99L);

		mockMvc.perform(get("/clientes/99"))
				.andExpect(status().isNotFound());
	}

	@Test
	void deletar_deveRetornar204() throws Exception {
		mockMvc.perform(delete("/clientes/1"))
				.andExpect(status().isNoContent());
	}
}