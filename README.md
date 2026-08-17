# Explorando Padrões de Projetos na Prática com Java

Projeto desenvolvido no Lab **"Explorando Padrões de Projetos na Prática com Java"** da [Digital Innovation One (DIO)](https://www.dio.me/), que explora a aplicação prática de padrões de projeto GoF usando o ecossistema Spring.

O projeto consiste em uma **API REST de gerenciamento de clientes** que consulta a API externa [ViaCEP](https://viacep.com.br/) para completar automaticamente os dados de endereço a partir do CEP informado, salvando tudo em um banco de dados em memória (H2).

## Padrões de Projeto Aplicados

Este laboratório explora três padrões de projeto clássicos do GoF:

| Padrão | Onde está aplicado | Descrição |
| --- | --- | --- |
| **Singleton** | `ClienteServiceImpl` (`@Service`) e todos os beans do Spring | O container Spring garante uma única instância dos beans (padrão Singleton gerenciado pelo framework, sem implementação manual) |
| **Strategy** | `ClienteService` (interface) → `ClienteServiceImpl` | A interface `ClienteService` define o contrato e a implementação `ClienteServiceImpl` encapsula o algoritmo de persistência com integração ViaCEP; o Spring injeta a implementação via construtor, permitindo trocar a estratégia sem alterar o chamador |
| **Facade** | `ClienteRestController` | O `@RestController` abstrai toda a complexidade das integrações (banco H2 + API ViaCEP + validação) em uma interface simples e coesa: a API REST `/clientes` |

## Key Features

- CRUD completo de clientes via API REST (`/clientes`)
- Integração transparente com a API ViaCEP para preenchimento automático do endereço (via **OpenFeign**)
- Reuso de endereços: se o CEP já existe no banco, ele é reaproveitado (evita consultas desnecessárias)
- Validação de entrada com **Bean Validation** (Jakarta Validation)
- Tratamento centralizado de erros com `ProblemDetail` (RFC 9457)
- Documentação interativa da API com **Swagger UI** (springdoc-openapi)
- Banco de dados **H2 em memória** com console web
- Monitoramento e health check com **Spring Boot Actuator**
- Testes automatizados (unitários e de integração)

## Tech Stack

- **Linguagem**: Java 25
- **Framework**: Spring Boot 3.5.16
- **Web**: Spring Web (Spring MVC)
- **Persistência**: Spring Data JPA + H2 Database (em memória)
- **Integração HTTP**: Spring Cloud OpenFeign (2025.0.3)
- **Validação**: Bean Validation (Jakarta Validation)
- **Documentação**: springdoc-openapi 2.8.17 (Swagger UI)
- **Observabilidade**: Spring Boot Actuator
- **Build**: Maven (com Maven Wrapper)

## Pré-requisitos

- **JDK 25** ou superior ([Adoptium](https://adoptium.net/) ou [Oracle](https://www.oracle.com/java/technologies/downloads/))
- **Maven** 3.9+ (opcional — o projeto inclui o Maven Wrapper `mvnw`/`mvnw.cmd`)
- Acesso à internet para consumir a API ViaCEP (`https://viacep.com.br`)
- Git (para clonar o repositório)

## Como Executar o Projeto

### 1. Clone o repositório

```bash
git clone https://github.com/digitalinnovationone/lab-padroes-projeto-spring-gof.git
cd lab-padroes-projeto-spring-gof
```

### 2. Execute a aplicação

Utilize o Maven Wrapper para iniciar a aplicação Spring Boot:

**Windows:**

```bash
.\mvnw.cmd spring-boot:run
```

**Linux/macOS:**

```bash
./mvnw spring-boot:run
```

Alternativamente, com Maven instalado globalmente:

```bash
mvn spring-boot:run
```

### 3. Acesse os recursos

| Recurso | URL |
| --- | --- |
| API REST | `http://localhost:8080` |
| Swagger UI | `http://localhost:8080/swagger-ui/index.html` |
| Console H2 | `http://localhost:8080/h2-console` |
| Health Check (Actuator) | `http://localhost:8080/actuator/health` |

> **Console H2**: use `jdbc:h2:mem:testdb` como JDBC URL, usuário `sa` e senha vazia.

### Executando os testes

```bash
.\mvnw.cmd test
```

## Arquitetura

### Estrutura de Diretórios

```
├── src/
│   ├── main/
│   │   ├── java/one/digitalinnovation/gof/
│   │   │   ├── Application.java              # Classe principal (Spring Boot + OpenFeign)
│   │   │   ├── controller/
│   │   │   │   └── ClienteRestController.java    # Facade: API REST /clientes
│   │   │   ├── service/
│   │   │   │   ├── ClienteService.java           # Strategy: contrato
│   │   │   │   ├── ClienteMapper.java            # Mapper DTO ↔ Entidade
│   │   │   │   ├── ViaCepService.java            # Cliente HTTP OpenFeign (ViaCEP)
│   │   │   │   ├── ViaCepErrorDecoder.java       # Decoder de erros da API ViaCEP
│   │   │   │   └── impl/
│   │   │   │       └── ClienteServiceImpl.java   # Strategy: implementação (Singleton)
│   │   │   ├── model/
│   │   │   │   ├── Cliente.java                  # Entidade JPA
│   │   │   │   └── Endereco.java                 # Entidade JPA (chave primária = CEP)
│   │   │   ├── repository/
│   │   │   │   ├── ClienteRepository.java
│   │   │   │   └── EnderecoRepository.java
│   │   │   ├── dto/
│   │   │   │   ├── ClienteDto.java               # Record com validação
│   │   │   │   └── EnderecoDto.java              # Record com validação
│   │   │   └── exception/
│   │   │       ├── GlobalExceptionHandler.java   # Tratamento central (ProblemDetail)
│   │   │       ├── NotFoundException.java
│   │   │       └── ViaCepException.java
│   │   └── resources/
│   │       ├── application.properties            # Configurações da aplicação
│   │       └── static/                           # Página HTML estática
│   └── test/java/one/digitalinnovation/gof/      # Testes unitários e de integração
├── pom.xml                                       # Build Maven
└── mvnw / mvnw.cmd                               # Maven Wrapper
```

### Fluxo de uma Requisição (Fluxo de Dados)

```
Cliente (POST /clientes)
        │
        ▼
ClienteRestController (Facade)  ─── validação Bean Validation (DTO)
        │
        ▼
ClienteService (Strategy)
        │
        ├── Endereco já existe no H2? ── sim ──► reutiliza Endereco existente
        │        │
        │        não
        │        ▼
        │   ViaCepService (OpenFeign) ──► GET https://viacep.com.br/ws/{cep}/json/
        │        │
        │        ▼
        │   Endereco salvo no H2
        │
        ▼
Cliente salvo no H2 ──► Resposta 201 Created (com Location header)
```

### Modelo de Dados

```
ENDEREÇO
├── cep (VARCHAR(9), PK)
├── logradouro, complemento, bairro, localidade, uf
├── ibge, gia, ddd, siafi

CLIENTE
├── id (BIGINT, PK, auto-incremento)
├── nome (VARCHAR, NOT NULL)
└── endereco_id (FK → ENDEREÇO.cep, N:1)
```

Um `Cliente` possui um `Endereco` (relação `@ManyToOne`). O CEP é a chave primária do endereço — por isso, endereços são únicos por CEP e reutilizados entre clientes.

## API Endpoints

Base URL: `http://localhost:8080`

### `GET /clientes`

Retorna uma lista com todos os clientes cadastrados.

**Resposta `200 OK`:**

```json
[
    {
        "id": 1,
        "nome": "Fulano de Tal",
        "endereco": {
            "cep": "01001-000",
            "logradouro": "Praça da Sé",
            "complemento": "lado ímpar",
            "bairro": "Sé",
            "localidade": "São Paulo",
            "uf": "SP",
            "ibge": "3550308",
            "gia": "1004",
            "ddd": "11",
            "siafi": "7107"
        }
    }
]
```

### `GET /clientes/{id}`

Retorna os detalhes de um cliente específico com base no seu `id`.

**Resposta `200 OK`:** mesmo formato do exemplo acima.

**Erro `404 Not Found`:**

```json
{
    "type": "about:blank",
    "title": "Recurso não encontrado",
    "status": 404,
    "detail": "Cliente não encontrado com id: 999"
}
```

### `POST /clientes`

Cria um novo cliente. O corpo da requisição deve conter os dados do cliente, incluindo o endereço com o CEP.

**Corpo de requisição:**

```json
{
    "nome": "Fulano de Tal",
    "endereco": {
        "cep": "01001-000"
    }
}
```

**Resposta `201 Created`** — com header `Location: /clientes/{id}` e o cliente completo no corpo (dados do endereço preenchidos pelo ViaCEP).

**Erro `400 Bad Request`** (validação):

```json
{
    "type": "about:blank",
    "title": "Requisição inválida",
    "status": 400,
    "detail": "nome: Nome é obrigatório; endereco.cep: CEP é obrigatório"
}
```

**Erro `502 Bad Gateway`** (falha na integração ViaCEP):

```json
{
    "type": "about:blank",
    "title": "Falha na integração externa",
    "status": 502,
    "detail": "Falha ao consultar o CEP na API ViaCEP"
}
```

**Comportamento da Integração (Facade):**

1. O sistema valida o CEP (formato `\d{5}-?\d{3}`) e a presença do nome.
2. Verifica se o `Endereco` para o CEP fornecido já existe no banco de dados local.
3. Se o `Endereco` não existir, consulta a API do ViaCEP para obter os dados completos do endereço.
4. O `Endereco` retornado pelo ViaCEP é salvo no banco de dados local.
5. O novo `Cliente` é salvo com a referência ao `Endereco` (seja o que já existia ou o que foi recém-criado).

### `PUT /clientes/{id}`

Atualiza os dados de um cliente existente. O comportamento da integração com o ViaCEP é o mesmo do endpoint `POST`.

**Corpo de requisição:** mesmo formato do `POST`.

**Resposta `200 OK`:** cliente atualizado (formato completo).

**Erro `404 Not Found`:** se o cliente não existir.

### `DELETE /clientes/{id}`

Exclui um cliente do banco de dados com base no seu `id`.

**Resposta `204 No Content`** em caso de sucesso.

**Erro `404 Not Found`:** se o cliente não existir.

## Configuração (application.properties)

| Propriedade | Descrição | Padrão |
| --- | --- | --- |
| `server.port` | Porta HTTP da aplicação | `8080` |
| `spring.datasource.url` | URL do banco H2 | `jdbc:h2:mem:testdb` |
| `spring.h2.console.enabled` | Habilita o console web do H2 | `true` |
| `spring.h2.console.path` | Caminho do console H2 | `/h2-console` |
| `spring.jpa.hibernate.ddl-auto` | Estratégia de criação do schema (`update` apenas para dev) | `update` |
| `spring.jpa.open-in-view` | Desabilita o anti-pattern Open Session in View | `false` |
| `viacep.url` | URL base da API ViaCEP | `https://viacep.com.br/ws` |
| `spring.cloud.openfeign.client.config.default.connect-timeout` | Timeout de conexão do OpenFeign | `3000` ms |
| `spring.cloud.openfeign.client.config.default.read-timeout` | Timeout de leitura do OpenFeign | `5000` ms |
| `springdoc.swagger-ui.path` | Caminho do Swagger UI | `/swagger-ui.html` |

> **Importante**: `spring.jpa.hibernate.ddl-auto=update` é adequado apenas para desenvolvimento. Em produção, use migrações (Flyway/Liquibase).

## Tratamento de Erros

Todas as exceções são tratadas de forma centralizada em `GlobalExceptionHandler` (`@RestControllerAdvice`) e retornadas no formato `ProblemDetail` (RFC 9457):

| Cenário | HTTP Status | Title |
| --- | --- | --- |
| Cliente/Endereço não encontrado | `404 Not Found` | Recurso não encontrado |
| Falha na API ViaCEP | `502 Bad Gateway` | Falha na integração externa |
| Falha de validação (Bean Validation) | `400 Bad Request` | Requisição inválida |
| CEP ausente/inválido, corpo malformado | `400 Bad Request` | Requisição inválida |
| Erro não tratado | `500 Internal Server Error` | Erro interno |

## Testes

O projeto inclui testes automatizados com o Spring Boot Starter Test (JUnit 5 + MockMvc):

- **`ClienteServiceImplTest`** — testes unitários da camada de serviço (Strategy/Integração ViaCEP)
- **`ClienteRestControllerTest`** — testes de integração dos endpoints REST
- **`LabPadroesProjetoSpringApplicationTests`** — teste de contexto da aplicação

Para executar:

```bash
.\mvnw.cmd test
```

## Como Contribuir

Ajustes e melhorias são sempre bem-vindos! Se você tem alguma sugestão para aprimorar este projeto, sinta-se à vontade para seguir estes passos:

1. Faça um **Fork** do projeto.
2. Crie uma nova **Branch** (`git checkout -b feature/sua-feature`).
3. Faça **Commit** de suas alterações (`git commit -m 'Adiciona sua feature'`).
4. Faça **Push** para a Branch (`git push origin feature/sua-feature`).
5. Abra um **Pull Request**.

## Licença

Projeto didático de código aberto, inspirado no repositório [lab-padroes-projeto-spring-gof](https://github.com/digitalinnovationone/lab-padroes-projeto-spring-gof) da DIO. Consulte o repositório original para detalhes de licenciamento.