package one.digitalinnovation.gof;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.service.ViaCepService;

@SpringBootTest
@AutoConfigureMockMvc
class ClienteIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ViaCepService viaCepService;

	@Test
	void deveCadastrarEListarClientePassandoPelaCadeiaEViaCep() throws Exception {
		when(viaCepService.consultarCep("01001000")).thenReturn(Endereco.builder()
				.cep("01001000")
				.logradouro("Praça da Sé")
				.bairro("Sé")
				.localidade("São Paulo")
				.uf("SP")
				.build());

		mockMvc.perform(post("/clientes")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"nome\":\"Rafael Alvarenga\",\"email\":\"rafael@example.com\",\"cep\":\"01001000\"}"))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.email").value("rafael@example.com"))
				.andExpect(jsonPath("$.endereco.localidade").value("São Paulo"));

		mockMvc.perform(get("/clientes"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].nome").value("Rafael Alvarenga"))
				.andExpect(jsonPath("$[0].endereco.uf").value("SP"));
	}

	@Test
	void deveRejeitarCadastroComCepInvalidoSemChamarViaCep() throws Exception {
		mockMvc.perform(post("/clientes")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"nome\":\"Rafael Alvarenga\",\"email\":\"rafael@example.com\",\"cep\":\"123\"}"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400));
	}

	@Test
	void deveRetornar404ParaClienteInexistente() throws Exception {
		mockMvc.perform(get("/clientes/9999"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.path").value("/clientes/9999"));
	}
}
