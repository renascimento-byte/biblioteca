# BiblioSystem — Sistema de Controle de Biblioteca

Sistema web completo para gerenciamento de bibliotecas, desenvolvido com **Spring Boot 3** e **MongoDB Atlas**. Permite controlar livros, usuários, empréstimos, reservas e multas por meio de uma interface moderna com painel lateral.

---

## Tecnologias

| Camada | Tecnologia |
|---|---|
| Backend | Java 17 + Spring Boot 3.2 |
| Banco de Dados | MongoDB Atlas (Spring Data MongoDB) |
| Frontend | Thymeleaf + CSS customizado |
| Build | Maven |
| Utilitários | Lombok, Spring Validation |

---

## Funcionalidades

- **Dashboard** — visão geral com contadores de livros, usuários, empréstimos e reservas ativas
- **Livros** — cadastro, edição, remoção e busca por título; controle de quantidade disponível
- **Usuários** — cadastro e gerenciamento de leitores
- **Empréstimos** — registro de retirada, prazo de devolução, detecção automática de atraso e devolução
- **Reservas** — reserva de livros por usuário com controle de status
- **Multas** — listagem de multas geradas por atrasos

---

## Estrutura do Projeto

```
biblioteca/
├── src/
│   ├── main/
│   │   ├── java/com/biblioteca/
│   │   │   ├── BibliotecaApplication.java
│   │   │   ├── config/
│   │   │   │   ├── DataInitializer.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── WebConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── HomeController.java
│   │   │   │   ├── LivroController.java
│   │   │   │   ├── UsuarioController.java
│   │   │   │   ├── EmprestimoController.java
│   │   │   │   ├── ReservaController.java
│   │   │   │   └── MultaController.java
│   │   │   ├── model/
│   │   │   │   ├── Livro.java
│   │   │   │   ├── Usuario.java
│   │   │   │   ├── Emprestimo.java
│   │   │   │   ├── Reserva.java
│   │   │   │   └── Multa.java
│   │   │   ├── repository/
│   │   │   └── service/
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/css/style.css
│   │       └── templates/
│   │           ├── index.html
│   │           ├── layout.html
│   │           ├── livros/
│   │           ├── usuarios/
│   │           ├── emprestimos/
│   │           ├── reservas/
│   │           └── multas/
│   └── test/
└── pom.xml
```

---

## Configuração e Execução

### Pré-requisitos

- Java 17+
- Maven 3.8+
- Conta no [MongoDB Atlas](https://www.mongodb.com/cloud/atlas) (ou instância MongoDB local)

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/sistema-biblioteca.git
cd sistema-biblioteca/biblioteca
```

### 2. Configure o banco de dados

Edite o arquivo `src/main/resources/application.properties` e insira sua string de conexão do MongoDB Atlas:

```properties
spring.data.mongodb.uri=mongodb+srv://<usuario>:<senha>@<cluster>.mongodb.net/biblioteca?retryWrites=true&w=majority&appName=<AppName>
spring.data.mongodb.database=biblioteca
```

> **Nunca suba suas credenciais reais para o GitHub.** Use variáveis de ambiente (veja seção abaixo).

### 3. Execute a aplicação

```bash
mvn spring-boot:run
```

Acesse em: [http://localhost:8080](http://localhost:8080)

---

## Boas Práticas de Segurança (Credenciais)

Para não expor a senha do banco, use variáveis de ambiente no `application.properties`:

```properties
spring.data.mongodb.uri=${MONGO_URI}
```

E exporte a variável antes de rodar:

```bash
export MONGO_URI="mongodb+srv://usuario:senha@cluster.mongodb.net/..."
mvn spring-boot:run
```

Ou adicione um arquivo `.env` e use o [Spring Dotenv](https://github.com/cdimascio/dotenv-java). Certifique-se de adicionar `.env` ao `.gitignore`.

---

## Modelos de Dados

### Livro
| Campo | Tipo | Descrição |
|---|---|---|
| id | String | Identificador MongoDB |
| titulo | String | Título do livro |
| autor | String | Nome do autor |
| isbn | String | ISBN |
| anoPublicacao | Integer | Ano de publicação |
| categoria | String | Categoria/gênero |
| quantidade | Integer | Total de exemplares |
| quantidadeDisponivel | Integer | Exemplares disponíveis |

### Emprestimo
| Campo | Tipo | Descrição |
|---|---|---|
| id | String | Identificador MongoDB |
| usuarioNome | String | Nome do usuário |
| livroTitulo | String | Título do livro |
| dataRetirada | LocalDate | Data da retirada |
| dataPrevista | LocalDate | Prazo de devolução |
| dataDevolucao | LocalDate | Data efetiva (null se ativo) |
| status | String | ATIVO / DEVOLVIDO / ATRASADO |

---

## Capturas de Tela

> Interface com sidebar escura, topbar e design responsivo.

- **Dashboard** com estatísticas em cards coloridos
- **Tabelas** com busca, badges de status e ações rápidas
- **Formulários** em grid de duas colunas com validação

---

## Build para produção

```bash
mvn clean package -DskipTests
java -jar target/biblioteca-1.0.0.jar
```

---

## Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature: `git checkout -b feature/minha-feature`
3. Commit suas mudanças: `git commit -m 'feat: adiciona minha feature'`
4. Push para a branch: `git push origin feature/minha-feature`
5. Abra um Pull Request

---

## Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

> Desenvolvido com ☕ Java + Spring Boot · Banco de dados hospedado no MongoDB Atlas