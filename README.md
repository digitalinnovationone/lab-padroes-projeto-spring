# API REST de Clientes com Padrões de Projeto e Integração ViaCEP

API REST desenvolvida em **Java** com **Spring Boot**, criada a partir do laboratório de **Design Patterns com Spring** da [Digital Innovation One (DIO)](https://www.dio.me/) e evoluída como entrega final do desafio prático de consolidação de padrões de projeto.

---

## 📌 Sobre o Projeto

O objetivo principal desta aplicação é demonstrar a aplicação prática de **Design Patterns (GoF e Enterprise Patterns)** no ecossistema Spring Boot, aliando boas práticas de arquitetura REST, desacoplamento de camadas, injeção de dependências e integração com serviços externos (API do [ViaCEP](https://viacep.com.br/)).

A aplicação gerencia o cadastro de clientes e realiza o preenchimento e a persistência automática do endereço completo a partir do CEP informado via OpenFeign.

---

## 🚀 Funcionalidades

- **CRUD Completo de Clientes:** Criação, leitura, atualização e exclusão de clientes.
- **Enriquecimento Automático de Endereço:** Integração com a API externa do ViaCEP via Spring Cloud OpenFeign.
- **Cache Local de Endereços:** Reutilização de endereços já persistidos para evitar chamadas externas redundantes.
- **Camada de Transferência (DTOs):** Separação clara entre modelos de persistência (JPA) e contratos da API REST.
- **Tratamento Global de Exceções:** Respostas HTTP padronizadas (ex: `404 Not Found`) para recursos inexistentes.
- **Documentação Interativa com OpenAPI/Swagger:** Interface para visualização e testes dos endpoints.
- **Banco de Dados em Memória:** Persistência relacional gerenciada pelo H2 Database.

---

## 🧱 Design Patterns e Padrões Arquiteturais

Abaixo estão descritos os padrões de projeto aplicados e onde eles se encontram no código-fonte:

### 1. Strategy
- **Onde se aplica:** Interface [`ClienteService`](src/main/java/one/digitalinnovation/gof/service/ClienteService.java) e classe [`ClienteServiceImpl`](src/main/java/one/digitalinnovation/gof/service/impl/ClienteServiceImpl.java).
- **Problema resolvido:** Desacopla o contrato de operações de negócio da sua implementação concreta, permitindo que novas estratégias de persistência ou regras de clientes possam ser introduzidas sem impactar os controladores.

### 2. Facade
- **Onde se aplica:** 
  - [`ClienteRestController`](src/main/java/one/digitalinnovation/gof/controller/ClienteRestController.java): Provê uma interface REST simples para o cliente consumidor, abstraindo todo o fluxo interno da aplicação.
  - [`ClienteServiceImpl`](src/main/java/one/digitalinnovation/gof/service/impl/ClienteServiceImpl.java): Atua como Facade de domínio ao orquestrar a consulta externa ao ViaCEP, o tratamento de endereço e o salvamento em múltiplos repositórios JPA.

### 3. Repository
- **Onde se aplica:** [`ClienteRepository`](src/main/java/one/digitalinnovation/gof/model/ClienteRepository.java) e [`EnderecoRepository`](src/main/java/one/digitalinnovation/gof/model/EnderecoRepository.java).
- **Problema resolvido:** Encapsula a lógica de acesso e persistência a dados através do Spring Data JPA (`CrudRepository`), isolando o domínio das consultas SQL/relacionais.

### 4. Data Transfer Object (DTO)
- **Onde se aplica:** [`ClienteRequestDTO`](src/main/java/one/digitalinnovation/gof/dto/ClienteRequestDTO.java) e [`ClienteResponseDTO`](src/main/java/one/digitalinnovation/gof/dto/ClienteResponseDTO.java).
- **Problema resolvido:** Evita expor diretamente as entidades JPA (`Cliente` e `Endereco`) na API pública, prevenindo acoplamento indesejado e reduzindo a complexidade do payload enviado pelo cliente.

### 5. Inversion of Control (IoC) & Dependency Injection (DI)
- **Onde se aplica:** Em todas as classes gerenciadas pelo Spring (`@RestController`, `@Service`, `@Repository`, `@Configuration`).
- **Problema resolvido:** Gerenciamento do ciclo de vida dos componentes pelo container do Spring (atuando como *Singletons* por padrão) e injeção explícita via **construtor** nas classes [`ClienteRestController`](src/main/java/one/digitalinnovation/gof/controller/ClienteRestController.java) e [`ClienteServiceImpl`](src/main/java/one/digitalinnovation/gof/service/impl/ClienteServiceImpl.java).

---

## 💡 Evoluções em Relação ao Laboratório Original

Para a entrega final do desafio da DIO, o projeto original foi evoluído com as seguintes melhorias autorais:

1. **Implementação de DTOs (`ClienteRequestDTO` e `ClienteResponseDTO`):**
   - O laboratório original recebia a entidade `Cliente` completa aninhada com `Endereco`.
   - Agora, o cliente da API envia apenas `{ "nome": "...", "cep": "..." }` e recebe um objeto estruturado de resposta.
2. **Tratamento Global de Exceções (`@RestControllerAdvice`):**
   - Criação da exceção de domínio [`RecursoNaoEncontradoException`](src/main/java/one/digitalinnovation/gof/exception/RecursoNaoEncontradoException.java) e do payload [`ErroResposta`](src/main/java/one/digitalinnovation/gof/exception/ErroResposta.java).
   - Eliminação de erros 500 (`NoSuchElementException`) ao consultar IDs inexistentes, retornando `404 Not Found` estruturado.
3. **Refatoração para Injeção por Construtor:**
   - Remoção de anotações `@Autowired` em campos privados, adotando atributos `final` e injeção via construtor (recomendação oficial do Spring Framework).
4. **Robustez e Validação na Camada Service:**
   - Tratamento de CEPs em branco/nulos e verificação segura de retornos do ViaCEP antes de persistir no banco.
5. **Documentação Swagger/OpenAPI Enriquecida:**
   - Adição de [`OpenApiConfig`](src/main/java/one/digitalinnovation/gof/config/OpenApiConfig.java), tags, descrições operacionais e mapeamento de códigos HTTP (`200`, `201`, `204`, `400`, `404`).

---

## 🛠️ Tecnologias Utilizadas

As seguintes tecnologias e versões foram utilizadas no projeto:

| Tecnologia | Versão | Função |
| :--- | :--- | :--- |
| **Java** | 11 | Linguagem de programação |
| **Spring Boot** | 2.5.4 | Framework principal |
| **Spring Data JPA** | 2.5.4 | Camada de persistência relacional |
| **Spring Cloud OpenFeign** | 2020.0.3 | Cliente HTTP declarativo para integração com ViaCEP |
| **H2 Database** | Runtime | Banco de dados relacional em memória |
| **Springdoc OpenAPI UI** | 1.5.10 | Documentação interativa da API (Swagger UI) |
| **Maven Wrapper** | 3.8.x | Gerenciador de dependências e build |

---

## 📁 Estrutura do Projeto

```text
src/
├── main/
│   ├── java/one/digitalinnovation/gof/
│   │   ├── Application.java               # Classe principal Spring Boot
│   │   ├── config/
│   │   │   └── OpenApiConfig.java         # Configuração de metadados do Swagger
│   │   ├── controller/
│   │   │   ├── ClienteRestController.java # Endpoints REST da aplicação
│   │   │   └── GlobalExceptionHandler.java# Tratamento global de exceções
│   │   ├── dto/
│   │   │   ├── ClienteRequestDTO.java     # DTO de entrada (nome, cep)
│   │   │   └── ClienteResponseDTO.java    # DTO de saída (id, nome, endereco)
│   │   ├── exception/
│   │   │   ├── ErroResposta.java          # Modelo de payload de erro HTTP
│   │   │   └── RecursoNaoEncontradoException.java # Exceção 404
│   │   ├── model/
│   │   │   ├── Cliente.java               # Entidade JPA Cliente
│   │   │   ├── ClienteRepository.java     # Repositório de Clientes
│   │   │   ├── Endereco.java              # Entidade JPA Endereco
│   │   │   └── EnderecoRepository.java    # Repositório de Endereços
│   │   └── service/
│   │       ├── ClienteService.java        # Interface de negócio (Strategy)
│   │       ├── ViaCepService.java         # Feign Client para o ViaCEP
│   │       └── impl/
│   │           └── ClienteServiceImpl.java# Implementação da Strategy e Facade
└── test/
    └── java/one/digitalinnovation/gof/
        └── LabPadroesProjetoSpringApplicationTests.java # Teste de carregamento de contexto
```

---

## ⚙️ Como Executar o Projeto

### Pré-requisitos
- **Java JDK 11** (ou superior) instalado e configurado no `PATH`.
- Git instalado (opcional, para clonar).

### Passo a passo

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/seu-usuario/lab-padroes-projeto-spring.git
   ```

2. **Acesse o diretório do projeto:**
   ```bash
   cd lab-padroes-projeto-spring
   ```

3. **Execute a aplicação usando o Maven Wrapper:**
   - **Linux / macOS:**
     ```bash
     ./mvnw spring-boot:run
     ```
   - **Windows (PowerShell / CMD):**
     ```powershell
     .\mvnw.cmd spring-boot:run
     ```

4. **Acesse a documentação Swagger:**
   A aplicação estará disponível em `http://localhost:8080`.  
   Abra no seu navegador: **`http://localhost:8080/swagger-ui.html`**

---

## 🔌 Endpoints da API

| Método | Endpoint | Descrição | Status Sucesso | Status Erro |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/clientes` | Lista todos os clientes cadastrados | `200 OK` | - |
| `GET` | `/clientes/{id}` | Busca um cliente pelo ID | `200 OK` | `404 Not Found` |
| `POST` | `/clientes` | Cadastra um novo cliente com CEP | `201 Created` | `400 Bad Request` |
| `PUT` | `/clientes/{id}` | Atualiza dados de um cliente existente | `200 OK` | `404 Not Found` |
| `DELETE` | `/clientes/{id}` | Remove um cliente pelo ID | `204 No Content` | `404 Not Found` |

---

## 📝 Exemplos de Requisição e Resposta

### Cadastrar Cliente (`POST /clientes`)

**Requisição:**
```json
{
  "nome": "Pedro Silva",
  "cep": "01001000"
}
```

**Resposta (`201 Created`):**
```json
{
  "id": 1,
  "nome": "Pedro Silva",
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
```

### Consultar Cliente Inexistente (`GET /clientes/999`)

**Resposta (`404 Not Found`):**
```json
{
  "timestamp": "2026-08-19T19:00:00.000Z",
  "status": 404,
  "erro": "Recurso Não Encontrado",
  "mensagem": "Cliente não encontrado para o ID: 999"
}
```

---

## 🎓 Desafio DIO

Este projeto foi desenvolvido como resolução do desafio **"Explorando Padrões de Projetos na Prática com Java"**, integrante do bootcamp da **[Digital Innovation One](https://www.dio.me/)**. 

A proposta cumpriu o objetivo de reproduzir o laboratório de padrões com Spring Boot e evoluí-lo com características próprias, aplicando princípios de Clean Code, desacoplamento e robustez em APIs REST.
