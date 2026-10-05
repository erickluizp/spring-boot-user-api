# 👤 Spring Boot User API

API REST para gerenciamento de usuários, desenvolvida com Java e Spring Boot. O projeto implementa as operações de CRUD seguindo uma arquitetura em camadas, com tratamento de exceções e testes automatizados.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-database-blue)
![Status](https://img.shields.io/badge/status-conclu%C3%ADdo-brightgreen)

## 📋 Sobre o projeto

Projeto pessoal criado para praticar o desenvolvimento backend com o ecossistema Spring. A API permite cadastrar, listar, buscar, atualizar e excluir usuários, retornando respostas HTTP adequadas para cada situação.

## ✨ Funcionalidades

- Cadastro de usuários
- Listagem de todos os usuários
- Busca de usuário por ID
- Atualização de dados
- Exclusão de usuário
- Tratamento de exceções com respostas HTTP padronizadas (ex.: `404` para usuário não encontrado)
- Testes unitários com JUnit e Mockito
- Containerização da aplicação e do banco de dados com Docker
- Autenticação e autorização com Spring Security (login e controle de acesso às rotas)
- Documentação interativa dos endpoints com Swagger/OpenAPI

## 🛠️ Tecnologias utilizadas

- **Java**
- **Spring Boot**
- **Spring Data JPA**
- **Spring Security** (autenticação e autorização)
- **Swagger / OpenAPI** (documentação da API)
- **Hibernate**
- **PostgreSQL**
- **JUnit** e **Mockito**
- **Maven**
- **Docker**
- **Git e GitHub**
- **Postman** (testes manuais dos endpoints)

> Ajuste a lista conforme o que o projeto realmente usa (por exemplo, H2 nos testes, Docker, DTOs e Bean Validation).

## 🏗️ Arquitetura

O projeto segue a arquitetura em camadas:

```
src/main/java/.../
├── controller/   # Recebe as requisições HTTP e devolve as respostas
├── service/      # Regras de negócio
├── repository/   # Acesso ao banco de dados (Spring Data JPA)
├── entity/       # Entidades JPA
├── security/     # Configuração de autenticação e autorização
└── exception/    # Exceções personalizadas e tratamento global
```

> Confirme os nomes dos pacotes no seu repositório e ajuste se forem diferentes.

## 📡 Endpoints

| Método | Rota           | Descrição                  |
|--------|----------------|----------------------------|
| POST   | `/users`       | Cria um novo usuário       |
| GET    | `/users`       | Lista todos os usuários    |
| GET    | `/users/{id}`  | Busca um usuário por ID    |
| PUT    | `/users/{id}`  | Atualiza um usuário        |
| DELETE | `/users/{id}`  | Remove um usuário          |

### Exemplo de requisição

`POST /users`

```json
{
  "name": "Maria Silva",
  "email": "maria@email.com"
}
```

### Exemplo de resposta

```json
{
  "id": 1,
  "name": "Maria Silva",
  "email": "maria@email.com"
}
```

> Substitua as rotas e os campos pelos reais da sua API.

## 🔐 Autenticação e autorização

A API usa Spring Security para proteger as rotas. O fluxo é:

1. O usuário se autentica na rota de login (ex.: `POST /auth/login`) e recebe um token.
2. Nas demais requisições, envia o token no cabeçalho `Authorization: Bearer <token>`.
3. Cada rota exige um perfil de acesso (ex.: `USER` ou `ADMIN`). Quem não tem permissão recebe `401 Unauthorized` ou `403 Forbidden`.

> Ajuste esta seção ao que você implementou: tipo de autenticação (JWT, Basic, sessão), rotas públicas, perfis existentes e quais ações cada perfil pode fazer.

## 📖 Documentação (Swagger)

Com a aplicação rodando, a documentação interativa dos endpoints fica disponível em:

```
http://localhost:8080/swagger-ui/index.html
```

Nela é possível ver todas as rotas, os modelos de dados e testar as requisições direto pelo navegador. Para rotas protegidas, use o botão **Authorize** e informe o token.

> Confirme a URL: ela muda conforme a biblioteca (`springdoc-openapi` usa `/swagger-ui/index.html`).

## 🚀 Como executar

### Pré-requisitos

- JDK 17 ou superior (ajuste conforme a versão do seu projeto)
- Maven
- PostgreSQL instalado e em execução
- Git

### Passo a passo

```bash
# Clone o repositório
git clone https://github.com/erickluizp/spring-boot-user-api.git

# Acesse a pasta do projeto
cd spring-boot-user-api
```

Crie um banco de dados no PostgreSQL (por exemplo, `userdb`) e configure o arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/userdb
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
spring.jpa.hibernate.ddl-auto=update
```

Depois, execute a aplicação:

```bash
mvn spring-boot:run
```

A API ficará disponível em `http://localhost:8080`.

### 🐳 Executando com Docker

Com o Docker instalado, não é preciso ter Java, Maven ou PostgreSQL na máquina:

```bash
docker compose up --build
```

Esse comando constrói a imagem da API e sobe os containers da aplicação e do banco de dados. Para parar:

```bash
docker compose down
```

> Se o seu projeto tem apenas um `Dockerfile` e não um `docker-compose.yml`, troque o comando por `docker build -t spring-boot-user-api .` seguido de `docker run -p 8080:8080 spring-boot-user-api`, e explique como conectar ao banco.

## 🧪 Testes

Para rodar os testes automatizados:

```bash
mvn test
```

## 📚 Aprendizados

- Construção de uma API REST com Spring Boot
- Persistência de dados com Spring Data JPA e Hibernate
- Separação de responsabilidades em camadas (Controller, Service e Repository)
- Tratamento de exceções e uso correto de códigos de status HTTP
- Testes unitários com JUnit e Mockito
- Segurança de APIs com autenticação e autorização
- Documentação de APIs com OpenAPI e containerização com Docker

## 🔮 Próximos passos

- [x] Documentação da API com Swagger/OpenAPI
- [x] Autenticação e autorização com Spring Security
- [x] Containerização com Docker
- [ ] Pipeline de CI com GitHub Actions

## 👤 Autor

**Erick Luiz**

[![GitHub](https://img.shields.io/badge/GitHub-erickluizp-181717?logo=github)](https://github.com/erickluizp)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-erick--luiz09-0A66C2?logo=linkedin)](https://linkedin.com/in/erick-luiz09)
