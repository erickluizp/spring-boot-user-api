# 🛒 Spring Boot API: Usuários, Pedidos e Produtos

API REST desenvolvida com Java e Spring Boot para gerenciar usuários, pedidos, produtos e categorias. Conta com autenticação e autorização via JWT, documentação interativa com Swagger, testes automatizados e execução em containers Docker.

![Java](https://img.shields.io/badge/Java-17%2B-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-API%20REST-brightgreen)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-6DB33F)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-database-blue)
![Docker](https://img.shields.io/badge/Docker-compose-2496ED)

## 📋 Sobre o projeto

Projeto desenvolvido a partir do curso de Java e Spring Boot do professor Nelio Alves (web services com Spring Boot e JPA/Hibernate), que fornece o modelo de domínio e a base da API. Sobre essa base, acrescentei:

- Autenticação e autorização com **Spring Security e JWT**, com perfis `USER` e `ADMIN`
- Documentação dos endpoints com **Swagger/OpenAPI**
- Containerização com **Docker** e **Docker Compose**
- Perfis de configuração separados (padrão, `test` e `docker`) e variáveis de ambiente
- Testes automatizados de serviços, controllers e segurança

## ✨ Funcionalidades

- CRUD de usuários, com cadastro público e senha armazenada com hash (BCrypt)
- Login que retorna um token JWT
- Controle de acesso por perfil: algumas operações são exclusivas de administradores
- Gerenciamento de pedidos, itens de pedido, pagamentos, produtos e categorias
- Tratamento de exceções com respostas HTTP padronizadas
- Documentação interativa com Swagger, com suporte a token Bearer
- Execução completa com `docker compose`

## 🛠️ Tecnologias utilizadas

- **Java**
- **Spring Boot**
- **Spring Data JPA** e **Hibernate**
- **Spring Security** com **JWT**
- **Swagger / OpenAPI**
- **PostgreSQL** (execução em container) e **H2** (perfil de testes)
- **JUnit** e **Mockito**
- **Maven**
- **Docker** e **Docker Compose**
- **Git e GitHub**
- **Postman**

## 🧩 Modelo de domínio

Entidades principais: `User`, `Order`, `OrderItem`, `Payment`, `Product` e `Category`, além da enumeração `OrderStatus`.

## 🏗️ Arquitetura

O projeto segue a arquitetura em camadas:

```
src/main/java/com/educandoweb/course/
├── config/       # Configurações: segurança (JWT), Swagger e dados de teste
│   └── security/ # Filtro JWT, UserDetails e SecurityConfig
├── dto/          # Objetos de transferência de dados
├── entities/     # Entidades JPA
├── repository/   # Acesso ao banco de dados (Spring Data JPA)
├── resources/    # Controllers REST e tratamento de exceções
├── service/      # Regras de negócio (incluindo o serviço de JWT)
└── CourseApplication.java

src/main/resources/
├── application.properties          # Configuração padrão
├── application-test.properties     # Perfil de testes
└── application-docker.properties   # Perfil para execução em container

src/test/java/    # Testes de resources, security e service
```

## 🔐 Autenticação e autorização

A API usa Spring Security com tokens JWT:

1. O usuário se cadastra em `POST /users` (rota pública).
2. Faz login em `POST /login` e recebe um token JWT.
3. Nas demais requisições, envia o token no cabeçalho:

```
Authorization: Bearer <token>
```

4. Um filtro (`JwtAuthenticationFilter`) valida o token em cada requisição e carrega o usuário e o perfil dele. Token inválido ou ausente retorna `401 Unauthorized`.

### Regras de acesso

| Rota                          | Acesso                          |
|-------------------------------|---------------------------------|
| `POST /users` e `POST /login` | Público                         |
| `/swagger-ui/**`, `/v3/api-docs/**` | Público                   |
| `PUT /users/**`               | Somente `ADMIN`                 |
| `DELETE /users/**`            | Somente `ADMIN`                 |
| Demais rotas                  | Qualquer usuário autenticado    |

Um usuário autenticado sem permissão para a operação recebe `403 Forbidden`.

### Exemplo de login

`POST /login`

```json
{
  "email": "alex@gmail.com",
  "password": "123456"
}
```

### Usuários de exemplo (perfil `test`)

No perfil `test`, a aplicação carrega dados de exemplo para facilitar os testes manuais:

| Usuário      | E-mail            | Senha    | Perfil       |
|--------------|-------------------|----------|--------------|
| Maria Brown  | maria@gmail.com   | 123456   | `ROLE_USER`  |
| Alex Green   | alex@gmail.com    | 123456   | `ROLE_ADMIN` |
| Bob Brown    | bob@gmail.com     | 123456   | `ROLE_USER`  |

> Esses usuários existem apenas para desenvolvimento e testes. Não use essas credenciais em produção.

## 📖 Documentação (Swagger)

Com a aplicação rodando, a documentação interativa fica em:

```
http://localhost:8080/swagger-ui/index.html
```

Nela é possível ver todas as rotas e modelos e testar as requisições pelo navegador. Para rotas protegidas, faça login em `POST /login`, copie o token, clique em **Authorize** e cole o valor.

## 🚀 Como executar

### Pré-requisitos

- JDK 17 ou superior
- Maven (ou o `mvnw` incluído no projeto)
- Docker e Docker Compose (para a execução em container)
- Git

### Clonar o repositório

```bash
git clone https://github.com/erickluizp/spring-boot-user-api.git
cd spring-boot-user-api
```

### Opção 1: com Docker (recomendado)

Não é preciso ter Java, Maven ou PostgreSQL instalados.

Crie o arquivo de variáveis de ambiente a partir do modelo e preencha com seus valores:

```bash
cp .env.example .env
```

Suba os containers:

```bash
docker compose up --build
```

O container usa o perfil `docker` (`application-docker.properties`). Para parar:

```bash
docker compose down
```

### Opção 2: localmente, com o perfil de testes

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=test
```

Esse perfil carrega os dados de exemplo descritos acima.

A API ficará disponível em `http://localhost:8080`.

## 🧪 Testes

```bash
./mvnw test
```

## 📚 Aprendizados

- Construção de uma API REST com Spring Boot, JPA e Hibernate
- Autenticação stateless com JWT e controle de acesso por perfil com Spring Security
- Criptografia de senhas com BCrypt
- Documentação de APIs com OpenAPI/Swagger, incluindo autenticação Bearer
- Separação de responsabilidades em camadas e uso de DTOs
- Perfis de configuração e variáveis de ambiente
- Containerização com Docker e Docker Compose
- Testes automatizados com JUnit e Mockito

## 🔮 Próximos passos

- [x] Documentação da API com Swagger/OpenAPI
- [x] Autenticação e autorização com Spring Security
- [x] Containerização com Docker
- [ ] Pipeline de CI com GitHub Actions
- [ ] Paginação e filtros nas listagens
- [ ] Deploy em nuvem (AWS)

## 👤 Autor

**Erick Luiz**

[![GitHub](https://img.shields.io/badge/GitHub-erickluizp-181717?logo=github)](https://github.com/erickluizp)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-erick--luiz09-0A66C2?logo=linkedin)](https://linkedin.com/in/erick-luiz09)
