# Plano de implementação

Este plano deriva de [arquitetura](arquitetura.md), [requisitos](requisitos/requisitos.md) e do código-base existente. Itens abaixo são **trabalho a realizar**, não funcionalidades concluídas.

| Etapa | Entrega | Critério mínimo de conclusão |
| --- | --- | --- |
| 0 | Ambiente e base versionados | Aplicação inicia pela IDE; informações sensíveis fora do Git |
| 1 | Categoria persistida | Tabela versionada, CRUD JDBC coerente, validações e testes |
| 2 | Localização persistida | Cadastro e consulta no PostgreSQL; referências validadas |
| 3 | Equipamento persistido | Patrimônio único, categoria e localização com integridade referencial |
| 4 | Movimentação | Transação atômica, localização atual atualizada e histórico consultável |
| 5 | Manutenção | Preventiva/corretiva, atualização e histórico por equipamento |
| 6 | Baixa | Registro sem apagar equipamento, consulta posterior preservada |
| 7 | Interface web | Telas Thymeleaf para os fluxos centrais do Administrador |
| 8 | Qualidade e entrega | Testes, documentação, revisão dos RF/RNF e roteiro de demonstração |

## Regra de trabalho

Para cada etapa: (1) explicar a regra de negócio, (2) revisar modelo/tabela, (3) implementar repository, service e controller, (4) testar o comportamento, (5) atualizar a documentação e registrar um commit.

Não começar a etapa seguinte enquanto a anterior depender de uma configuração que só funciona em um computador.
