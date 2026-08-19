package one.digitalinnovation.gof.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API REST - Padrões de Projeto com Spring Boot")
                        .description("API REST desenvolvida para o desafio de Design Patterns da DIO. "
                                + "Demonstração prática dos padrões Singleton, Strategy, Facade, Repository e DTO com integração ViaCEP.")
                        .version("1.0.0"));
    }
}
