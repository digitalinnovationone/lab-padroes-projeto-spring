# API REST de Clientes com Padrões de Projeto

Projeto de demonstração em Java e Spring Boot que combina padrões de projeto com uma API REST para cadastro de clientes e consulta de endereços pelo ViaCEP.

## Padrões aplicados

- **Singleton:** gerenciamento de componentes pelo contêiner Spring.
- **Strategy:** `ClienteService` define o contrato e `ClienteServiceImpl` fornece a implementação.
- **Facade/Repository:** o controller expõe uma interface simples enquanto o service integra JPA e ViaCEP.
- **Injeção de dependências:** desacoplamento por interfaces e construtores.

## Tecnologias

- Java 11
- Spring Boot 2.5
- Spring Web e Validation
- Spring Data JPA
- OpenFeign
- Banco H2
- springdoc OpenAPI/Swagger
- JUnit 5 e Mockito

## Como executar

```bash
./mvnw spring-boot:run
```

No Windows, use:

```powershell
.\mvnw.cmd spring-boot:run
```

Interfaces disponíveis após iniciar:

- API: `http://localhost:8080/clientes`
- Swagger: `http://localhost:8080/swagger-ui.html`
- H2 Console: `http://localhost:8080/h2-console`

Credenciais H2: usuário `sa`, senha vazia e JDBC URL `jdbc:h2:mem:clientes`.

## Endpoints

| Método | Caminho | Descrição |
|---|---|---|
| `GET` | `/clientes` | Lista todos os clientes |
| `GET` | `/clientes/{id}` | Busca um cliente |
| `POST` | `/clientes` | Cria um cliente |
| `PUT` | `/clientes/{id}` | Atualiza um cliente |
| `DELETE` | `/clientes/{id}` | Remove um cliente |

Exemplo de criação:

```json
{
  "nome": "Ana Silva",
  "endereco": {
    "cep": "01001-000"
  }
}
```

A API valida campos obrigatórios e o formato do CEP. Respostas de erro incluem uma mensagem clara e, quando aplicável, os campos inválidos.

## Testes

```bash
./mvnw test
```

## Próximas evoluções

- Testes de integração dos endpoints
- Autenticação e autorização com Spring Security
- Paginação e filtros
- Migrações de banco com Flyway
- Dockerização da aplicação
