# Explorando Padroes de Projetos na Pratica com Java

Repositorio com as implementacoes dos padroes de projeto explorados no Lab "Explorando Padroes de Projetos na Pratica com Java". Este projeto explora padroes de projeto usando o **Spring Framework**.

## Design Patterns Implementados

| Padrao | Onde | Descricao |
|--------|------|-----------|
| **Singleton** | `ClienteServiceImpl` (`@Service`) | Spring gerencia uma unica instancia do servico |
| **Strategy** | `ClienteService` (interface) + `ClienteServiceImpl` | Interface define contrato, permitindo multiplas implementacoes |
| **Facade** | `ClienteRestController` | Abstrai a complexidade de integracoes (H2 + ViaCEP) em uma API REST simples |
| **Observer** | `ClienteEvent` + `ClienteEventListener` | Spring Events notifica listeners sobre operacoes em clientes (CRIADO, ATUALIZADO, DELETADO) |

## Melhorias Implementadas

- **Tratamento de Erros Global**: `GlobalExceptionHandler` com `@RestControllerAdvice` para respostas de erro padronizadas (404, 400, 422)
- **Bean Validation**: Validacao de campos do `Cliente` com `@NotBlank`, `@Size` e `@NotNull`
- **Documentacao OpenAPI/Swagger**: Anotacoes `@Operation` e `@ApiResponse` em todos os endpoints
- **Codigos HTTP Corretos**: POST retorna 201 (Created), DELETE retorna 204 (No Content)
- **Observer Pattern**: Eventos de dominio via Spring Events com logging automatico
- **Logging com SLF4J**: Registro de operacoes no servico e nos eventos
- **Testes Unitarios**: 8 testes com Mockito cobrindo cenarios de sucesso e erro
- **Configuracao**: `application.properties` com H2 console, datasource e logging

## Como Executar

```bash
./mvnw spring-boot:run
```

## Endpoints Disponiveis

Apos iniciar a aplicacao:

- **Swagger UI**: http://localhost:8081/swagger-ui.html
- **H2 Console**: http://localhost:8081/h2-console (JDBC URL: `jdbc:h2:mem:testdb`, usuario: `sa`, senha vazia)

## API REST

| Metodo | Endpoint | Descricao | Status |
|--------|----------|-----------|--------|
| GET | `/clientes` | Listar todos os clientes | 200 |
| GET | `/clientes/{id}` | Buscar cliente por ID | 200 / 404 |
| POST | `/clientes` | Criar novo cliente | 201 / 400 |
| PUT | `/clientes/{id}` | Atualizar cliente | 200 / 404 |
| DELETE | `/clientes/{id}` | Deletar cliente | 204 / 404 |

### Exemplo de Request (POST /clientes)

```json
{
  "nome": "Joao Silva",
  "endereco": {
    "cep": "01001000"
  }
}
```

## Rodar Testes

```bash
./mvnw clean test
```
