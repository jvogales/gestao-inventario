# Arquitetura inicial — Sistema de Inventário e Controle de Equipamentos

> Documento de referência para orientar a implementação incremental. Não afirma que todos os módulos já estão prontos.

## Objetivo e escopo

Aplicação web para um único ator (**Administrador**) cadastrar e consultar equipamentos, categorias e localizações; controlar estado; registrar transferências, manutenções e baixas com histórico. Fonte funcional: [requisitos](requisitos/requisitos.md) e [modelo de domínio](modelo-dominio.md).

## Decisões técnicas

| Aspecto | Decisão | Situação |
| --- | --- | --- |
| Linguagem | Java 17 como versão de compilação | Configurada no `pom.xml`; execução em JDK superior é possível |
| Backend | Spring Boot 3.5, Spring MVC | Implementado |
| Interface | Thymeleaf, HTML e CSS | Implementada em `/app` |
| Banco de dados | PostgreSQL | Implementado com schema automático |
| Persistência | Spring JDBC / `JdbcTemplate` | Implementada conforme RNF05 |
| Build e dependências | Maven Wrapper | Disponível |
| Versionamento | Git + GitHub | Disponível |

## Componentes e dependências

```text
Navegador (interface Thymeleaf)
          |
          v
Controller  -> recebe HTTP, valida entrada e prepara resposta/navegação
          |
          v
Service     -> regras de negócio e coordenação das operações
          |
          v
Repository  -> operações SQL com JdbcTemplate
          |
          v
PostgreSQL  -> dados persistentes e integridade referencial
```

O módulo de interface web pode coexistir com endpoints REST durante a migração. A divisão em camadas **não** implica criar controllers que acessem SQL diretamente. O Service é o lugar apropriado para decidir, por exemplo, se uma movimentação ou baixa é permitida.

## Situação da entrega

Todos os módulos previstos possuem entidade, repository JDBC, service, endpoint
REST e fluxo na interface web. O schema é aplicado automaticamente. Os testes
usam H2 isolado e a aplicação de desenvolvimento usa PostgreSQL na porta 8081.

## Modelo de domínio previsto

- **Categoria**: classifica equipamentos; um equipamento pertence a uma categoria.
- **Localizacao**: descreve o local onde um equipamento está; a localização atual é mantida no equipamento.
- **Equipamento**: patrimônio, identificação, dados descritivos, categoria, localização atual e estado.
- **Movimentacao**: equipamento, origem, destino e data; registra histórico, sem apagar a movimentação anterior.
- **Manutencao**: equipamento, tipo, situação e dados da intervenção; preserva histórico.
- **Baixa**: registro associado a no máximo um equipamento, com data, motivo e observação; não deve excluir fisicamente o equipamento.

Detalhes exatos de atributos, cardinalidades e estados devem ser confrontados com o [diagrama de classes](diagramas/classes/diagrama-de-classes-entidades.png) antes da implementação. A baixa deve preservar os dados e impedir operações incompatíveis, conforme regras ainda a detalhar.

## Regras de projeto

1. Identificador patrimonial deve ser único; impor a restrição também no banco, não apenas no Service.
2. Categoria e localização referenciadas devem existir.
3. Transferência deve registrar o histórico **e** atualizar a localização atual na mesma transação.
4. Baixa não é `DELETE`: equipamentos baixados permanecem consultáveis.
5. Não permitir exclusão física de registros que sejam necessários ao histórico.
6. Validação de entrada não substitui constraints do banco.
7. Segredos de conexão ficam fora do repositório. `.env` e `target/` devem permanecer ignorados.

## Persistência e evolução do banco

O projeto usa JDBC, portanto criar classes Java não cria tabelas automaticamente. Na implementação definitiva, **preferir migrações SQL versionadas** (por exemplo, Flyway, se a equipe decidir incorporá-lo). Evitar depender de tabelas criadas manualmente em cada computador.

O DDL inicial de `categoria` executado na UENP serve como teste de conexão, mas ainda precisa ser revisado frente ao modelo completo antes de virar uma migração definitiva.

## Sequência de implementação

1. Alinhar o código local funcional com uma branch versionada, sem incluir senha/arquivos de ambiente.
2. Consolidar **Categoria** (schema SQL, repository JDBC, service, validações e testes).
3. Consolidar **Localizacao**.
4. Persistir **Equipamento**, com relações e unicidade de patrimônio.
5. Implementar **Movimentacao** transacional com histórico.
6. Implementar **Manutencao** e suas regras.
7. Implementar **Baixa** sem exclusão física.
8. Desenvolver telas Thymeleaf e percorrer os fluxos principais pelo navegador.

Cada etapa deve produzir uma funcionalidade verificável, documentação atualizada e commit separado.

## Decisões adotadas na entrega

- Tabelas e restrições ficam em `schema.sql` e são criadas automaticamente.
- Baixa bloqueia movimentação e manutenção; conclusão de manutenção libera o equipamento.
- Categorias e localizações em uso não podem ser excluídas.
- Testes usam H2 compatível com PostgreSQL e isolado do banco de desenvolvimento.
- A interface usa baixa patrimonial; endpoints DELETE permanecem apenas na API de CRUD.
