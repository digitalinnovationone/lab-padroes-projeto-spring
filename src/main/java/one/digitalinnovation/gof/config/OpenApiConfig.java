package one.digitalinnovation.gof.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Clientes - Design Patterns")
                        .description("API RESTful desenvolvida para o laboratório de padrões de projeto do Spring Boot.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Carlos Faria")
                                .email("carlos.faria@estudante.firjan.senai.br"))  );
    }
}