# Credit Customer Service

Microservice responsável pelo cadastro, consulta e gerenciamento de clientes dentro da **Credit Journey Platform**.

Doc do sistema completo:

```text
https://github.com/diogobackend/credit-journey-platform
```

Este serviço representa o contexto de clientes em uma plataforma fictícia de crédito para banco digital.

Ele é o ponto inicial da jornada: antes de avaliar elegibilidade, calcular limite ou enviar comunicações, o cliente precisa existir e ter seus dados cadastrais registrados.

---

## Responsabilidade do serviço

O `credit-customer-service` é responsável por:

- cadastrar clientes;
- consultar clientes;
- listar clientes com paginação e filtros;
- atualizar dados cadastrais;
- alterar status do cliente;
- excluir clientes;
- validar regras de domínio;
- expor endpoints REST;
- versionar banco com Flyway;
- expor endpoints operacionais com Actuator;
- gerar logs automáticos nos use cases.

Exemplo prático da jornada:

```text
Cliente cadastrado
      |
      v
Customer Service registra cliente
      |
      v
Rules Engine avalia elegibilidade
      |
      v
Limit Service calcula limite
      |
      v
Communication Service notifica o cliente
      |
      v
Audit Service registra a jornada
```

---

## Stack técnica

- Kotlin
- Java 21
- Spring Boot 4
- Gradle Kotlin DSL
- Spring Web MVC
- Spring Data JPA
- Bean Validation
- Flyway
- MySQL
- Spring Boot Actuator
- Springdoc OpenAPI / Swagger
- Docker Compose
- MockK
- AssertJ
- JaCoCo
- ktlint

---

## Status atual

```text
CRUD básico implementado
```

Funcionalidades já implementadas:

- criação de cliente;
- consulta por ID;
- listagem com paginação;
- filtros por status, search, name e income;
- atualização parcial;
- alteração de status;
- exclusão real;
- validações de domínio;
- exceptions específicas;
- handler global de erro;
- Swagger/OpenAPI;
- Actuator;
- Flyway;
- MySQL;
- testes unitários;
- JaCoCo;
- ktlint;
- logs automáticos com AOP.

---

# Arquitetura

Este serviço segue **Arquitetura Hexagonal / Ports and Adapters**.

Regra principal:

```text
O domínio não deve depender de Spring, banco de dados, HTTP, mensageria ou qualquer detalhe de infraestrutura.
```

---

## Estrutura base

```text
src/main/kotlin/com/creditjourney/customer/
├── CreditCustomerServiceApplication.kt
├── core/
│   ├── common/
│   │   └── messages/
│   ├── domain/
│   │   ├── model/
│   │   ├── exception/
│   │   └── valueobject/
│   ├── port/
│   │   ├── input/
│   │   └── output/
│   └── usecase/
└── app/
    ├── adapter/
    │   ├── input/
    │   │   ├── messaging/
    │   │   └── web/
    │   │       ├── controllers/
    │   │       ├── handler/
    │   │       ├── mappers/
    │   │       ├── requests/
    │   │       ├── responses/
    │   │       └── swagger/
    │   └── output/
    │       ├── messaging/
    │       └── persistence/
    │           ├── entity/
    │           ├── mapper/
    │           └── repository/
    └── configuration/
        └── logs/
```

---

## Responsabilidade das camadas

### core/domain

Contém os modelos e regras centrais do domínio.

Exemplos:

```text
Customer
CustomerStatus
Document
Email
Income
```

Essa camada não deve conhecer Spring, JPA, DTOs, controllers, banco de dados ou qualquer detalhe de infraestrutura.

---

### core/port/input

Define o que a aplicação sabe fazer.

Exemplos:

```text
CreateCustomerPort
FindCustomerByIdPort
FindAllCustomersPort
UpdateCustomerPort
ChangeCustomerStatusPort
DeleteCustomerPort
```

Controllers devem chamar portas de entrada.

---

### core/port/output

Define o que a aplicação precisa acessar fora do domínio.

Exemplo:

```text
CustomerRepositoryPort
```

Use cases dependem dessas portas, não de repositories Spring Data diretamente.

---

### core/usecase

Contém a implementação dos casos de uso.

Exemplos:

```text
CreateCustomerUseCase
FindCustomerByIdUseCase
FindAllCustomersUseCase
UpdateCustomerUseCase
ChangeCustomerStatusUseCase
DeleteCustomerUseCase
```

Aqui ficam as regras de aplicação.

---

### app/adapter/input/web

Camada de entrada HTTP.

Contém:

```text
controllers
requests
responses
mappers
handler
swagger
```

O controller não executa regra de negócio diretamente.

Ele recebe a requisição, valida os dados, converte o payload e chama uma porta de entrada.

---

### app/adapter/output/persistence

Camada de persistência.

Contém:

```text
entities JPA
repositories Spring Data
mappers Entity <-> Domain
adapter de persistência
```

A entity JPA não deve ser usada como modelo de domínio.

---

### app/configuration

Contém configurações Spring.

Exemplos:

```text
UseCaseConfiguration
LogInfoAspect
```

---

# Configuração local

## Porta da aplicação

A aplicação roda localmente na porta:

```text
8081
```

URL base:

```text
http://localhost:8081
```

---

## Banco de dados local

Este serviço utiliza banco próprio, seguindo a estratégia `database per service`.

Banco:

```text
customer_db
```

Host local:

```text
localhost
```

Porta local:

```text
3307
```

Usuário local:

```text
customer_user
```

Senha local:

```text
customer_pass
```

URL JDBC:

```text
jdbc:mysql://localhost:3307/customer_db
```

---

## Docker Compose

Arquivo esperado:

```text
docker-compose.yml
```

Configuração local do MySQL:

```yaml
services:
  mysql:
    image: mysql:8.4
    container_name: credit-customer-mysql
    environment:
      MYSQL_DATABASE: customer_db
      MYSQL_USER: customer_user
      MYSQL_PASSWORD: customer_pass
      MYSQL_ROOT_PASSWORD: root
    ports:
      - "3307:3306"
```

A porta interna do MySQL continua sendo `3306`, mas na máquina local o acesso é feito pela porta `3307`.

---

## application.yml

Arquivo:

```text
src/main/resources/application.yml
```

Configuração local:

```yaml
spring:
  application:
    name: credit-customer-service

  datasource:
    url: jdbc:mysql://localhost:3307/customer_db
    username: customer_user
    password: customer_pass
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true

  flyway:
    enabled: true

server:
  port: 8081

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      probes:
        enabled: true

springdoc:
  swagger-ui:
    path: /swagger-ui.html
  api-docs:
    path: /v3/api-docs
```

---

# Migrations

As migrations ficam em:

```text
src/main/resources/db/migration
```

Padrão de nome:

```text
V1__create_customers_table.sql
V2__add_unique_constraints_to_customers.sql
V3__create_customers_pagination_index.sql
```

Regras:

- usar dois underlines depois da versão;
- nunca alterar uma migration já aplicada;
- criar uma nova migration para cada mudança de banco;
- manter nomes claros e objetivos.

---

## Tabela principal

### customers

Tabela principal de clientes.

Campos principais:

```text
customer_id
name
document
email
phone
income
status
created_at
updated_at
```

---

# Domínio

## Customer

Representa o cliente dentro do domínio.

Campos principais:

```text
customerId
name
document
email
phone
income
status
createdAt
updatedAt
```

---

## CustomerStatus

Status possíveis:

```text
ACTIVE
BLOCKED
INACTIVE
```

---

## Value Objects

### Document

Responsável por validar documento.

Regras:

- não pode ser vazio;
- deve conter apenas números;
- deve possuir 11 dígitos.

---

### Email

Responsável por validar e-mail.

Regras:

- não pode ser vazio;
- deve possuir formato válido.

---

### Income

Responsável por validar renda.

Regra:

```text
Income não pode ser negativo.
```

---

# API / Swagger

A documentação completa da API está disponível via Swagger.

Swagger UI:

```text
http://localhost:8081/swagger-ui.html
```

OpenAPI JSON:

```text
http://localhost:8081/v3/api-docs
```

Os endpoints, contratos de request/response, códigos HTTP e exemplos devem ser consultados diretamente pelo Swagger.

---

# Actuator

A aplicação expõe endpoints operacionais.

## Health

```http
GET /actuator/health
```

Exemplo:

```json
{
  "status": "UP"
}
```

## Metrics

```http
GET /actuator/metrics
```

## Prometheus

```http
GET /actuator/prometheus
```

---

# Como rodar localmente

## 1. Clonar o repositório

```bash
git clone https://github.com/diogobackend/credit-customer-service.git
cd credit-customer-service
```

---

## 2. Subir o MySQL

```bash
docker compose up -d
```

Verificar container:

```bash
docker ps
```

Esperado:

```text
credit-customer-mysql
```

---

## 3. Rodar a aplicação

```bash
./gradlew bootRun
```

A aplicação deve subir em:

```text
http://localhost:8081
```

---

## 4. Validar health

```bash
curl http://localhost:8081/actuator/health
```

Esperado:

```json
{
  "status": "UP"
}
```

---

## 5. Validar Swagger

Acessar no navegador:

```text
http://localhost:8081/swagger-ui.html
```

---

## 6. Acessar o MySQL

```bash
docker exec -it credit-customer-mysql mysql -u customer_user -pcustomer_pass customer_db
```

Dentro do MySQL:

```sql
SHOW TABLES;
```

---

# Comandos mais usados no dia a dia

## Subir infraestrutura local

```bash
docker compose up -d
```

---

## Parar infraestrutura local

```bash
docker compose down
```

---

## Parar e remover volumes

```bash
docker compose down -v
```

---

## Ver logs dos containers

```bash
docker compose logs -f
```

---

## Ver logs do MySQL

```bash
docker compose logs -f mysql
```

---

## Rodar aplicação

```bash
./gradlew bootRun
```

---

## Rodar build completo

```bash
./gradlew clean build
```

---

## Rodar testes

```bash
./gradlew test
```

---

## Rodar testes com relatório JaCoCo

```bash
./gradlew clean test jacocoTestReport
```

---

## Abrir relatório JaCoCo

```bash
xdg-open build/reports/jacoco/test/html/index.html
```

---

## Rodar ktlint check

```bash
./gradlew ktlintCheck
```

---

## Corrigir formatação com ktlint

```bash
./gradlew ktlintFormat
```

---

## Rodar validação geral antes de commit

```bash
./gradlew ktlintFormat
./gradlew ktlintCheck
./gradlew clean test jacocoTestReport
./gradlew clean build
```

---

## Rodar teste específico

```bash
./gradlew test --tests "*CreateCustomerUseCaseTest"
```

Outros exemplos:

```bash
./gradlew test --tests "*FindCustomerByIdUseCaseTest"
./gradlew test --tests "*FindAllCustomersUseCaseTest"
./gradlew test --tests "*UpdateCustomerUseCaseTest"
./gradlew test --tests "*ChangeCustomerStatusUseCaseTest"
./gradlew test --tests "*DeleteCustomerUseCaseTest"
```

---

## Limpar build local

```bash
./gradlew clean
```

---

## Ver dependências do projeto

```bash
./gradlew dependencies
```

---

## Validar status do Git

```bash
git status
```

---

## Criar commit

```bash
git add .
git commit -m "feat: implement customer management"
```

---

## Enviar alterações

```bash
git push origin master
```

---

# Logs automáticos

O serviço possui logs automáticos via AOP nos use cases.

Annotations usadas:

```kotlin
@LogInfo
@LogParameter
```

Exemplo:

```kotlin
@LogInfo(logParameters = true, logReturn = true)
fun create(@LogParameter input: CreateCustomerInput): Customer
```

Exemplo de log esperado:

```text
M=create, parameters={input=CreateCustomerInput(...)}, return=Customer(...)
```

Futuramente, essa estrutura será extraída para a lib:

```text
credit-observability-starter
```

---

# Testes

O projeto usa:

- JUnit 5;
- MockK;
- AssertJ;
- JaCoCo.

Testes principais:

```text
CreateCustomerUseCaseTest
FindCustomerByIdUseCaseTest
FindAllCustomersUseCaseTest
UpdateCustomerUseCaseTest
ChangeCustomerStatusUseCaseTest
DeleteCustomerUseCaseTest
```

---

# Boas práticas aplicadas

- Arquitetura Hexagonal;
- Separação entre domínio e infraestrutura;
- DTOs apenas nas bordas;
- Entity JPA separada do domínio;
- Mappers explícitos;
- Constructor Injection;
- Flyway para versionamento de banco;
- MySQL isolado para o serviço;
- Actuator para health e métricas;
- Swagger para documentação da API;
- Docker Compose para ambiente local;
- Configuração via `application.yml`;
- Testes unitários com MockK;
- JaCoCo para cobertura;
- ktlint para padronização de código;
- Logs automáticos com AOP.

---

# Próximas evoluções

- Publicar evento `CustomerCreated`;
- Publicar evento `CustomerUpdated`;
- Publicar evento `CustomerStatusChanged`;
- Publicar evento `CustomerDeleted`;
- Implementar Outbox Pattern;
- Integrar com Kafka;
- Propagar `correlationId`;
- Extrair logs automáticos para `credit-observability-starter`;
- Criar testes de integração;
- Criar Dockerfile;
- Integrar com infraestrutura compartilhada.

---
