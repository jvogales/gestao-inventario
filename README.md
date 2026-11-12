# Gestão de Inventário

Sistema web completo para cadastro e controle do ciclo de vida de equipamentos, desenvolvido com Java, Spring Boot, Thymeleaf, JDBC e PostgreSQL.

## Funcionalidades

- Categorias, localizações e equipamentos persistidos no PostgreSQL.
- Pesquisa e filtros de equipamentos pela API.
- Movimentação com atualização automática da localização e histórico.
- Manutenção preventiva/corretiva, conclusão e histórico.
- Baixa patrimonial sem apagar o equipamento ou seus registros.
- Painel web responsivo e API REST para testes no Postman.
- Validação, respostas HTTP e proteção contra patrimônio duplicado.

## Executar no Windows / NetBeans

Pré-requisitos: JDK 17 ou superior e PostgreSQL em execução.

1. Crie o banco com `CREATE DATABASE gestao_inventario;`.
2. Defina a variável de usuário `DB_PASSWORD` com a senha do usuário `postgres`.
3. Feche e abra o NetBeans para carregar a variável.
4. Atualize com `git pull`, abra como projeto Maven e clique em **Run**.
5. Acesse [http://localhost:8081](http://localhost:8081).

O `schema.sql` cria automaticamente todas as tabelas. Nenhuma senha é salva no GitHub.

## Executar e testar pelo terminal

```bash
./mvnw spring-boot:run
./mvnw clean verify
```

No PowerShell, use `.\mvnw.cmd`. Os testes usam H2 isolado em memória e não alteram o PostgreSQL de desenvolvimento.

## Rotas principais

| Recurso | Rotas |
| --- | --- |
| Interface | `/` ou `/app` |
| Equipamentos | `/equipamentos` |
| Categorias | `/categorias` |
| Localizações | `/localizacoes` |
| Movimentações | `/movimentacoes` |
| Manutenções | `/manutencoes` |
| Baixas | `/baixas` |
| Saúde | `/actuator/health` |

## Regras implementadas

- Número patrimonial é único.
- Categoria e localização precisam existir antes do equipamento.
- Movimentação registra origem/destino e atualiza a localização na mesma transação.
- Equipamento baixado não pode ser movimentado nem receber manutenção.
- Baixa preserva o equipamento e todo o histórico.
- Categoria/localização em uso não pode ser excluída.

Documentação adicional está em [`docs/`](docs/README.md).

## Autor

[João Vitor Orlandini Gales](https://github.com/jvogales)
