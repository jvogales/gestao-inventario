# Gestão de Inventário

Sistema web para controle de equipamentos e histórico patrimonial. Projeto da disciplina de **Programação Orientada a Objetos II**.

O inventário reúne cadastro de categorias, localizações e equipamentos, pesquisa por patrimônio ou identificação, filtros, alteração de estado, movimentações, manutenções e baixa patrimonial. A aplicação usa Java 17, Spring Boot, Thymeleaf e PostgreSQL com JDBC.

## Executar no Windows ou no NetBeans

1. Instale um **JDK 17** e o **PostgreSQL**. No pgAdmin, crie o banco `gestao_inventario` com o usuário que a aplicação vai usar. Para criar via `psql`, execute `CREATE DATABASE gestao_inventario;` conectado ao banco `postgres`.
2. Abra o PowerShell na pasta do `pom.xml` e configure as variáveis da conexão. Use as credenciais do seu próprio PostgreSQL.

```powershell
$env:DB_URL = 'jdbc:postgresql://localhost:5432/gestao_inventario'
$env:DB_USERNAME = 'postgres'
$env:DB_PASSWORD = Read-Host 'Senha do PostgreSQL' -MaskInput
.\mvnw.cmd spring-boot:run
```

Use PowerShell 7 para `Read-Host -MaskInput`. No Linux/macOS, exporte as mesmas variáveis no terminal e rode `./mvnw spring-boot:run`. No NetBeans, abra a pasta como projeto Maven. Se for usar o botão **Run**, inicie a IDE a partir de um ambiente com as três variáveis já definidas.

Abra [localhost:8081](http://localhost:8081). A verificação de saúde está em [localhost:8081/actuator/health](http://localhost:8081/actuator/health). Encerre com `Ctrl+C`.

O Spring executa os [scripts SQL](src/main/resources/db) em ordem ao iniciar. Eles podem ser reaplicados e preservam as categorias existentes de uma instalação com o esquema original. A conta usada pela aplicação precisa poder criar tabelas e índices. Os mesmos scripts podem ser aplicados manualmente na ordem numérica via pgAdmin ou `psql`. Em um banco administrado separadamente, configure `SPRING_SQL_INIT_MODE=never` após aplicar os scripts.

## Testar e gerar o JAR

Crie um banco separado chamado `gestao_inventario_test` e dê acesso ao mesmo usuário configurado acima. Para apontar para outra instância de teste, defina `TEST_DB_URL`. Os testes preparam o esquema ao iniciar e usam o banco de teste.

```powershell
.\mvnw.cmd clean verify
```

No Linux/macOS use `./mvnw clean verify`. O JAR executável fica em `target/gestao-inventario-0.0.1-SNAPSHOT.jar`. Para executar o JAR com as variáveis do banco de desenvolvimento configuradas, use `java -jar target/gestao-inventario-0.0.1-SNAPSHOT.jar`.

## Roteiro de demonstração

1. Em **Categorias**, cadastre Informática. Em **Localizações**, cadastre Laboratório 1 e Almoxarifado.
2. Cadastre um equipamento com patrimônio `PAT-001`. Pesquise por esse patrimônio e combine os filtros de categoria, localização e estado.
3. Abra a ficha, edite a identificação e altere o estado. Movimente o equipamento para o Almoxarifado e confira a localização atual e o histórico.
4. Registre uma manutenção preventiva. Edite a manutenção, informe a data de fim e marque-a como concluída.
5. Registre a baixa com data e motivo. Filtre a lista por **Baixado** e abra a ficha para conferir a baixa e os dois históricos.
6. Reinicie a aplicação e repita a consulta para demonstrar a persistência.

O ator Administrador é o papel definido para a demonstração acadêmica local. A aplicação não possui autenticação.

## Documentação

- [Requisitos e diagramas](docs/README.md)
- [API REST](docs/api.md)
- [Arquitetura](docs/arquitetura.md)
- [Modelo de domínio](docs/modelo-dominio.md)

## Autor

[João Vitor Orlandini Gales](https://github.com/jvogales)
