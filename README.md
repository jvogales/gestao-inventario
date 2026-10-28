# Gestão de Inventário

Sistema web para gerenciar equipamentos, localizações e o histórico de movimentações e manutenções. Projeto da disciplina de **Programação Orientada a Objetos II**.

> **Em desenvolvimento:** CRUD REST com **categorias persistidas em PostgreSQL** e equipamentos/localizações ainda em memória. Entidades usam Lombok. Telas, históricos e persistência dos demais módulos serão desenvolvidos nas próximas etapas.

## Tecnologias

| Componente | Tecnologia |
| --- | --- |
| Linguagem | Java 17 ou superior |
| Backend | Spring Boot 3.5 / Spring MVC |
| Interface prevista | Thymeleaf, HTML e CSS |
| Build | Maven 3.9.11 via Wrapper |
| Persistência | PostgreSQL com JDBC (Categoria); demais módulos em memória |

## Executar

Instale um **JDK 17 ou superior**. O Maven Wrapper baixa o Maven automaticamente; PostgreSQL é necessário para esta versão, inclusive para os testes de integração.

```bash
git clone https://github.com/jvogales/gestao-inventario.git
cd gestao-inventario
```

**Linux / macOS**

```bash
./mvnw spring-boot:run
```

**Windows / PowerShell**

```powershell
.\mvnw.cmd spring-boot:run
```

Prepare o PostgreSQL e execute `docs/sql/001_categoria.sql` no banco `gestao_inventario`. Defina `DB_PASSWORD` no ambiente de execução do Spring Boot. Verifique `http://localhost:8081/actuator/health`: a resposta esperada é `{"status":"UP"}`. A rota `/` ainda não possui tela. Encerre com `Ctrl+C`.

## Testar e gerar o JAR

```bash
./mvnw clean verify
```

No Windows, use `.\mvnw.cmd clean verify`. O comando executa testes que dependem do PostgreSQL e gera o JAR em `target/`.

## Organização

```text
gestao-inventario/
├── .github/workflows/  # Validação automática no GitHub
├── .mvn/wrapper/       # Configuração do Maven Wrapper
├── docs/               # Requisitos, diagramas e guias
├── src/
│   ├── main/java/      # Aplicação e camadas do sistema
│   ├── main/resources/ # Configurações e futuros arquivos da interface
│   └── test/java/      # Testes automatizados
├── mvnw / mvnw.cmd     # Comandos Maven para Linux/macOS e Windows
└── pom.xml             # Dependências e configuração do build
```

## Documentação

- [Guia de desenvolvimento](docs/desenvolvimento.md): JDK, IDE, execução e estrutura de pacotes.
- [API REST e exemplos para Postman](docs/api.md): rotas, JSON e respostas HTTP.
- [Requisitos funcionais e não funcionais](docs/requisitos/requisitos.md).
- [Diagramas e modelagem](docs/README.md).
- [Modelo de domínio e decisões de implementação](docs/modelo-dominio.md).
- [SQL inicial da tabela Categoria](docs/sql/001_categoria.sql).
- [Definição inicial do projeto (PDF)](docs/definicao-projeto.pdf).

## Escopo previsto

- Cadastro e consulta de equipamentos, categorias e localizações.
- Controle do estado e baixa dos equipamentos.
- Registro e histórico de movimentações e manutenções.

## Autor

[João Vitor Orlandini Gales](https://github.com/jvogales)
