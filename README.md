# Explorando Padrões de Projeto na Prática com Java (Spring)

API REST de **Cliente** que demonstra padrões de projeto do GoF combinados com as abstrações do Spring Framework. O projeto evoluiu a implementação de referência da DIO, acrescentando novos padrões e engenharia de qualidade em volta (validação, tratamento de erros, DTOs e testes).

## Padrões de projeto aplicados

| Padrão | Onde | Como |
| --- | --- | --- |
| **Singleton** | `@Service`, `@Component`, `@Repository` | Beans gerenciados pelo Spring são singletons por padrão. |
| **Strategy** | `service/ClienteService` + `service/impl/ClienteServiceImpl` | A interface define o contrato; a implementação é injetada, permitindo trocar a estratégia. |
| **Repository** | `model/ClienteRepository`, `model/EnderecoRepository` | Abstração de acesso a dados via Spring Data JPA. |
| **Facade** | `controller/ClienteRestController` e `ClienteServiceImpl` | Escondem a complexidade das integrações (H2 e ViaCEP) atrás de uma interface simples. |
| **Chain of Responsibility** | pacote `validation/` | Cada validação do cliente é um elo (`ValidadorNomeObrigatorio`, `ValidadorEmailFormato`, `ValidadorCepFormato`) encadeado em `CadeiaValidacaoCliente`. |
| **Observer** | pacote `event/` | Após o cadastro, um `ClienteCadastradoEvent` é publicado e consumido por listeners desacoplados (boas-vindas e auditoria). |
| **Builder** | `model/Cliente`, `model/Endereco` | Construção fluente das entidades, usada no mapper e nos testes. |

## Engenharia de qualidade

- **Bean Validation** (`@Valid` + DTOs) valida a requisição na fronteira da API.
- **Cadeia de validação de domínio** garante as regras no serviço, independente do transporte.
- **Tratamento de erros global** (`@RestControllerAdvice`) devolve respostas padronizadas com os status HTTP corretos (400 para dados inválidos, 404 para recurso inexistente).
- **DTOs** desacoplam o contrato da API das entidades JPA.
- **Testes** cobrem a cadeia de validação (unitário), o serviço (Mockito) e o controller (`@WebMvcTest`).

## Stack

- Java 11 (compatível com Java 21) · Spring Boot 2.5.4
- Spring Web · Spring Data JPA · H2 · OpenFeign (ViaCEP) · Bean Validation
- springdoc-openapi (Swagger UI) · JUnit 5 · Mockito

## Como executar

```bash
./mvnw spring-boot:run
```

- API: `http://localhost:8080/clientes`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- Console H2: `http://localhost:8080/h2-console`

## Endpoints

| Método | Rota | Descrição |
| --- | --- | --- |
| `GET` | `/clientes` | Lista todos os clientes. |
| `GET` | `/clientes/{id}` | Busca um cliente por id (404 se não existir). |
| `POST` | `/clientes` | Cadastra um cliente (valida os dados e resolve o endereço pelo CEP). |
| `PUT` | `/clientes/{id}` | Atualiza um cliente existente. |
| `DELETE` | `/clientes/{id}` | Remove um cliente. |

### Exemplo de cadastro

```bash
curl -X POST http://localhost:8080/clientes \
  -H "Content-Type: application/json" \
  -d '{"nome":"Rafael Alvarenga","email":"rafael@example.com","cep":"01001000"}'
```

## Testes

```bash
./mvnw test
```
