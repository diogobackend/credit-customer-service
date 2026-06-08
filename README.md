# Credit Customer Service

Microservice responsável pelo cadastro, consulta e gerenciamento de clientes dentro da **Credit Journey Platform**.

Doc do sistema completo:
https://github.com/diogobackend/credit-journey-platform

Este serviço representa o contexto de clientes em uma plataforma fictícia de crédito para banco digital. Ele é o ponto inicial da jornada: antes de avaliar elegibilidade, calcular limite ou enviar comunicações, o cliente precisa existir e ter seus dados cadastrais registrados.

---

## Responsabilidade do serviço

O `credit-customer-service` é responsável por:

- cadastrar clientes;
- consultar clientes;
- atualizar dados cadastrais;
- alterar status do cliente;
- registrar histórico de alteração de status;
- publicar eventos de domínio relacionados ao cliente;
- servir como origem da jornada de crédito.

Exemplo prático da jornada:

```text
Cliente cadastrado
      |
      v
Evento CustomerCreated publicado
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
- Docker Compose

---

## Arquitetura

Este serviço segue **Arquitetura Hexagonal / Ports and Adapters**.

A regra principal é:

> O domínio não deve depender de Spring, banco de dados, HTTP, mensageria ou qualquer detalhe de infraestrutura.

Estrutura base:

```text
src/main/kotlin/com/creditjourney/customer/
├── CreditCustomerServiceApplication.kt
├── core/
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
    │   │       ├── mapper/
    │   │       ├── request/
    │   │       └── response/
    │   └── output/
    │       ├── messaging/
    │       └── persistence/
    │           ├── entity/
    │           ├── mapper/
    │           └── repository/
    └── configuration/
```

---

## Responsabilidade das camadas

### `core/domain`

Contém os modelos e regras centrais do domínio.

Exemplos futuros:

- `Customer`
- `CustomerStatus`
- `Document`
- `Email`
- `Income`

Essa camada não deve conhecer Spring, JPA, DTOs ou detalhes de infraestrutura.

---

### `core/port/input`

Define o que a aplicação sabe fazer.

Exemplos futuros:

- `CreateCustomerPort`
- `FindCustomerByIdPort`
- `UpdateCustomerPort`
- `ChangeCustomerStatusPort`

---

### `core/port/output`

Define o que a aplicação precisa acessar fora do domínio.

Exemplos futuros:

- `CustomerRepositoryPort`
- `CustomerEventPublisherPort`

---

### `core/usecase`

Contém a implementação dos casos de uso.

Exemplos futuros:

- `CreateCustomerUseCase`
- `FindCustomerByIdUseCase`
- `UpdateCustomerUseCase`
- `ChangeCustomerStatusUseCase`

---

### `app/adapter/input/web`

Camada de entrada HTTP.

Contém:

- controllers;
- requests;
- responses;
- mappers de entrada e saída.

O controller não executa regra de negócio diretamente. Ele recebe a requisição, valida os dados, converte o payload e chama uma porta de entrada.

---

### `app/adapter/input/messaging`

Camada de entrada assíncrona.

Será usada futuramente caso o serviço precise consumir eventos ou comandos externos.

---

### `app/adapter/output/persistence`

Camada de persistência.

Contém:

- entities JPA;
- repositories Spring Data;
- mappers entre Entity e Domain;
- adapter que implementa a porta de saída.

A entity JPA não deve ser usada como modelo de domínio.

---

### `app/adapter/output/messaging`

Camada responsável pela publicação de eventos.

No futuro, este serviço publicará eventos como:

- `CustomerCreated`
- `CustomerUpdated`
- `CustomerStatusChanged`

---

## Banco de dados

Este serviço utiliza banco próprio, seguindo a estratégia **database per service**.

Banco:

```text
customer_db
```

Banco local via Docker:

```text
localhost:3307/customer_db
```

Usuário local:

```text
customer_user
```

Senha local:

```text
customer_pass
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

## Configuração da aplicação

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
```

---

## Migrations

As migrations ficam em:

```text
src/main/resources/db/migration
```

Padrão de nome:

```text
V1__create_customers_table.sql
V2__create_customer_status_history_table.sql
V3__create_outbox_events_table.sql
```

Regras:

- usar dois underlines depois da versão;
- nunca alterar uma migration já aplicada;
- criar uma nova migration para cada mudança de banco;
- manter nomes claros e objetivos.

---

## Tabelas planejadas

### `customers`

Tabela principal de clientes.

Campos previstos:

```text
id
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

### `customer_status_history`

Histórico de alteração de status.

Campos previstos:

```text
id
customer_id
previous_status
current_status
reason
changed_at
```

---

### `outbox_events`

Tabela para eventos que serão publicados de forma assíncrona.

Campos previstos:

```text
id
event_id
event_type
aggregate_id
payload
status
created_at
published_at
```

---

## Endpoints planejados

> Os endpoints abaixo representam a evolução planejada do serviço.  
> Na versão inicial, o serviço ainda possui apenas endpoints operacionais do Actuator.

---

### Criar cliente

```http
POST /api/v1/customers
```

Exemplo de request:

```json
{
  "name": "João Silva",
  "document": "12345678900",
  "email": "joao.silva@email.com",
  "phone": "11999999999",
  "income": 4500.00
}
```

Exemplo de response:

```json
{
  "id": "b7a19e89-61d1-42e7-b7db-5b764a75fb9a",
  "name": "João Silva",
  "document": "12345678900",
  "email": "joao.silva@email.com",
  "phone": "11999999999",
  "income": 4500.00,
  "status": "ACTIVE",
  "createdAt": "2026-06-07T21:30:00"
}
```

---

### Consultar cliente por ID

```http
GET /api/v1/customers/{id}
```

Exemplo:

```http
GET /api/v1/customers/b7a19e89-61d1-42e7-b7db-5b764a75fb9a
```

---

### Atualizar cliente

```http
PUT /api/v1/customers/{id}
```

Exemplo de request:

```json
{
  "name": "João Silva",
  "email": "joao.novo@email.com",
  "phone": "11888888888",
  "income": 5200.00
}
```

---

### Alterar status do cliente

```http
PATCH /api/v1/customers/{id}/status
```

Exemplo de request:

```json
{
  "status": "BLOCKED",
  "reason": "Cliente bloqueado por análise de risco"
}
```

Status previstos:

```text
ACTIVE
BLOCKED
INACTIVE
```

---

## Eventos publicados

Este serviço será responsável por publicar eventos de domínio relacionados ao cliente.

---

### `CustomerCreated`

Publicado quando um cliente é criado.

Exemplo:

```json
{
  "eventId": "6c77b10d-640c-448a-b92f-c70570ec2da9",
  "eventType": "CustomerCreated",
  "eventVersion": "1.0",
  "source": "credit-customer-service",
  "correlationId": "c5e82d90-91e3-4c3d-9935-e0782e2eb0d1",
  "occurredAt": "2026-06-07T21:30:00",
  "payload": {
    "customerId": "b7a19e89-61d1-42e7-b7db-5b764a75fb9a",
    "name": "João Silva",
    "document": "12345678900",
    "email": "joao.silva@email.com",
    "income": 4500.00,
    "status": "ACTIVE"
  }
}
```

---

### `CustomerUpdated`

Publicado quando dados relevantes do cliente forem atualizados.

Payload previsto:

```json
{
  "eventId": "uuid",
  "eventType": "CustomerUpdated",
  "eventVersion": "1.0",
  "source": "credit-customer-service",
  "correlationId": "uuid",
  "occurredAt": "2026-06-07T21:35:00",
  "payload": {
    "customerId": "uuid",
    "name": "João Silva",
    "email": "joao.novo@email.com",
    "phone": "11888888888",
    "income": 5200.00
  }
}
```

---

### `CustomerStatusChanged`

Publicado quando o status do cliente mudar.

Exemplo:

```json
{
  "eventId": "7d217f4b-61bb-4698-a08f-bb14cae7228c",
  "eventType": "CustomerStatusChanged",
  "eventVersion": "1.0",
  "source": "credit-customer-service",
  "correlationId": "c5e82d90-91e3-4c3d-9935-e0782e2eb0d1",
  "occurredAt": "2026-06-07T21:40:00",
  "payload": {
    "customerId": "b7a19e89-61d1-42e7-b7db-5b764a75fb9a",
    "previousStatus": "ACTIVE",
    "currentStatus": "BLOCKED",
    "reason": "Cliente bloqueado por análise de risco"
  }
}
```

---

## Integrações futuras

| Serviço | Tipo | Finalidade |
|---|---|---|
| credit-rules-engine-service | Kafka | Consumir `CustomerCreated` para avaliar elegibilidade |
| credit-audit-service | Kafka | Consumir eventos de cliente para montar timeline |
| credit-config-server | Config Server | Buscar configurações externas |
| credit-platform-infra | Docker/Kubernetes | Executar infraestrutura local e deploy |

---

## Actuator

A aplicação expõe endpoints operacionais.

### Health

```http
GET /actuator/health
```

Exemplo:

```json
{
  "groups": [
    "liveness",
    "readiness"
  ],
  "status": "UP"
}
```

---

### Metrics

```http
GET /actuator/metrics
```

---

### Prometheus

```http
GET /actuator/prometheus
```

---

## Como rodar localmente

### 1. Subir o MySQL

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

### 2. Rodar o build

```bash
./gradlew clean build
```

---

### 3. Rodar a aplicação

```bash
./gradlew bootRun
```

A aplicação deve subir em:

```text
http://localhost:8081
```

---

### 4. Validar health

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

### 5. Validar métricas

```bash
curl http://localhost:8081/actuator/metrics
```

---

### 6. Acessar o MySQL

```bash
docker exec -it credit-customer-mysql mysql -u customer_user -pcustomer_pass customer_db
```

Dentro do MySQL:

```sql
SHOW TABLES;
```

Resultado esperado neste momento inicial:

```text
flyway_schema_history
```

A tabela `flyway_schema_history` indica que o Flyway está ativo.

---

## Como parar o ambiente

Parar containers:

```bash
docker compose down
```

Parar containers e remover volumes:

```bash
docker compose down -v
```

---

## Validações já realizadas

- Aplicação subindo na porta `8081`
- Actuator `/health` retornando `UP`
- Actuator `/metrics` retornando métricas
- MySQL rodando via Docker
- Flyway criando `flyway_schema_history`
- Build executado com sucesso

---

## Boas práticas aplicadas

- Arquitetura Hexagonal
- Separação entre domínio e infraestrutura
- DTOs apenas nas bordas
- Entidade JPA separada do domínio
- Mappers explícitos
- Constructor Injection
- Flyway para versionamento de banco
- MySQL isolado para o serviço
- Actuator para health e métricas
- Docker Compose para ambiente local
- Configuração via `application.yml`

---

## Próximas evoluções

- Criar domínio `Customer`
- Criar enum `CustomerStatus`
- Criar caso de uso `CreateCustomerUseCase`
- Criar porta de entrada para criação de cliente
- Criar porta de saída para persistência
- Criar migration `customers`
- Criar adapter REST
- Criar adapter de persistência
- Criar endpoint `POST /api/v1/customers`
- Publicar evento `CustomerCreated`
- Implementar Outbox Pattern
- Criar testes unitários e de integração

---

## Status

Em desenvolvimento.
