package one.digitalinnovation.gof.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import one.digitalinnovation.gof.dto.ClienteResponse;
import one.digitalinnovation.gof.exception.RecursoNaoEncontradoException;
import one.digitalinnovation.gof.mapper.ClienteMapper;
import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.service.ClienteService;

@WebMvcTest(ClienteRestController.class)
class ClienteRestControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ClienteService clienteService;
	@MockBean
	private ClienteMapper clienteMapper;

	@Test
	void postComDadosValidosDeveRetornar201() throws Exception {
		ClienteResponse response = new ClienteResponse();
		response.setId(1L);
		response.setNome("Rafael Alvarenga");
		when(clienteMapper.toEntity(any())).thenReturn(new Cliente());
		when(clienteMapper.toResponse(any())).thenReturn(response);

		mockMvc.perform(post("/clientes")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"nome\":\"Rafael Alvarenga\",\"email\":\"rafael@example.com\",\"cep\":\"01001000\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.nome").value("Rafael Alvarenga"));
	}

	@Test
	void postComDadosInvalidosDeveRetornar400() throws Exception {
		mockMvc.perform(post("/clientes")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"nome\":\"\",\"email\":\"invalido\",\"cep\":\"123\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400));
	}

	@Test
	void getPorIdInexistenteDeveRetornar404() throws Exception {
		when(clienteService.buscarPorId(eq(99L)))
				.thenThrow(new RecursoNaoEncontradoException("Cliente não encontrado para o id 99."));

		mockMvc.perform(get("/clientes/99"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404));
	}

	@Test
	void putComDadosValidosDeveRetornar200() throws Exception {
		ClienteResponse response = new ClienteResponse();
		response.setId(1L);
		response.setNome("Rafael Atualizado");
		when(clienteMapper.toEntity(any())).thenReturn(new Cliente());
		when(clienteMapper.toResponse(any())).thenReturn(response);

		mockMvc.perform(put("/clientes/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"nome\":\"Rafael Atualizado\",\"email\":\"rafael@example.com\",\"cep\":\"01001000\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nome").value("Rafael Atualizado"));
	}

	@Test
	void deleteDeveRetornar204() throws Exception {
		mockMvc.perform(delete("/clientes/1"))
				.andExpect(status().isNoContent());
	}
}
