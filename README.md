# Gestão de Inventário

Sistema web para gerenciar equipamentos, localizações e o histórico de movimentações e manutenções. Projeto da disciplina de **Programação Orientada a Objetos II**.

> **Em desenvolvimento:** a base Spring Boot e as entidades iniciais (`Equipamento`, `Categoria` e `Localizacao`) estão implementadas. Telas, serviços e persistência serão desenvolvidos nas próximas etapas.

## Tecnologias

| Componente | Tecnologia |
| --- | --- |
| Linguagem | Java 17 ou superior |
| Backend | Spring Boot 3.5 / Spring MVC |
| Interface prevista | Thymeleaf, HTML e CSS |
| Build | Maven 3.9.11 via Wrapper |
| Persistência prevista | PostgreSQL com JDBC |

## Executar

Instale um **JDK 17 ou superior**. O Maven Wrapper baixa o Maven automaticamente; PostgreSQL ainda não é necessário.

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

Verifique `http://localhost:8080/actuator/health`: a resposta esperada é `{"status":"UP"}`. A rota `/` ainda não possui tela. Encerre com `Ctrl+C`.

## Testar e gerar o JAR

```bash
./mvnw clean verify
```

No Windows, use `.\mvnw.cmd clean verify`. O comando executa o teste de inicialização HTTP e gera o JAR em `target/`.

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
- [Requisitos funcionais e não funcionais](docs/requisitos/requisitos.md).
- [Diagramas e modelagem](docs/README.md).
- [Modelo de domínio e decisões de implementação](docs/modelo-dominio.md).
- [Definição inicial do projeto (PDF)](docs/definicao-projeto.pdf).

## Escopo previsto

- Cadastro e consulta de equipamentos, categorias e localizações.
- Controle do estado e baixa dos equipamentos.
- Registro e histórico de movimentações e manutenções.

## Autor

[João Vitor Orlandini Gales](https://github.com/jvogales)
