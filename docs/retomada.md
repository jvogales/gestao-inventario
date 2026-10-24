# Ponto de retomada — integração com PostgreSQL

## Já salvo no repositório

- Base Spring Boot com Lombok e CRUD REST em memória.
- Exemplos de Postman e tratamento de respostas HTTP.
- Propriedade `start-class` para executar pelo Run do NetBeans.

## Trabalho feito no computador local durante a aula

Estas alterações foram orientadas pelo chat e executadas no PC do usuário.
Ainda não estavam disponíveis no workspace remoto ao registrar esta nota:

1. Adicionar `spring-boot-starter-jdbc` e o driver `postgresql` ao `pom.xml`.
2. Configurar `application.properties` para o banco local `gestao_inventario`:

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/gestao_inventario
   spring.datasource.username=${DB_USERNAME:postgres}
   spring.datasource.password=${DB_PASSWORD}
   ```

3. Definir `DB_PASSWORD` nas variáveis do Windows, sem gravar a senha no Git.
4. Substituir `CategoriaRepository` por implementação com `JdbcTemplate` e
   `RowMapper<Categoria>`, usando SELECT, INSERT RETURNING id, UPDATE e DELETE.
5. Usar a porta 8081 no PC local, pois a 8080 estava ocupada.

É necessário fazer commit e push desses arquivos no computador local para
preservar a implementação exata. O banco PostgreSQL e as variáveis do Windows
não são enviados pelo Git.

## Última validação observada

O log do computador local confirmou que o servidor iniciou na porta 8081 e que
o pool abriu uma conexão PostgreSQL. A consulta de categorias falhou com:

```text
ERRO: relação "categoria" não existe
```

A persistência ainda não foi validada. No Query Tool do banco correto, conferir:

```sql
SELECT current_database(), current_schema();
SELECT to_regclass('public.categoria');
```

O banco esperado é `gestao_inventario`. Se a tabela não existir, executar o
[script de categoria](sql/categoria.sql) nesse banco. Depois testar POST e GET
`/categorias` e conferir `SELECT * FROM public.categoria;` no pgAdmin.

## Depois de resolver a tabela

- Confirmar que a categoria permanece após reiniciar a aplicação.
- Revisar os testes para a nova dependência de banco antes de afirmar que o
  build com JDBC passa. Os oito testes anteriores validavam o CRUD em memória.
- Migrar localização e equipamento para JDBC em etapas posteriores.
- Revisar as referências de categoria: a versão em memória compartilhava o
  objeto Java; a versão JDBC lê novos objetos, então referências em equipamentos
  ainda mantidos em memória podem ficar desatualizadas após editar uma categoria.
- Atualizar os guias de desenvolvimento e da API após salvar e validar a migração.
