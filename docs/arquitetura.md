# Arquitetura do inventário

Aplicação Spring MVC com páginas Thymeleaf e endpoints JSON. Os dois tipos de entrada usam os mesmos serviços.

```text
Navegador / cliente REST
        ↓
Controllers — recebem HTTP, validam entrada e renderizam páginas ou JSON
        ↓
Services — aplicam regras e delimitam transações
        ↓
Repositories — executam SQL com JdbcTemplate
        ↓
PostgreSQL — mantém registros, chaves estrangeiras e restrições
```

Os DTOs recebem campos editáveis e IDs de referência. Os repositórios de equipamento e histórico consultam os catálogos por JOIN, portanto os nomes exibidos refletem o cadastro atual. Índices únicos garantem a unicidade de patrimônio e de nome de categoria com normalização de caixa e espaços nas extremidades.

Movimentação e baixa bloqueiam a linha do equipamento em uma transação. Cada operação registra seu histórico e atualiza o equipamento dentro da mesma transação. Edição de equipamento e manutenção usa o mesmo bloqueio para coordenar alterações concorrentes com a baixa.

Os scripts SQL numerados estão em [src/main/resources/db](../src/main/resources/db). O Spring os aplica em ordem no início, inclusive no perfil de testes. O primeiro e o segundo scripts reaproveitam o esquema de Categoria das versões anteriores. Os testes de integração executam sobre PostgreSQL, em um banco separado configurado pelo perfil `test`.

Veja [requisitos](requisitos/requisitos.md), [modelo de domínio](modelo-dominio.md) e [execução](../README.md).
