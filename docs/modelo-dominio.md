# Modelo de domínio

O [diagrama de classes](diagramas/classes/diagrama-de-classes-entidades.png) define seis entidades.

| Entidade | Dados e associações |
| --- | --- |
| Categoria | Nome obrigatório e descrição opcional. Classifica vários equipamentos. |
| Localização | Bloco e local obrigatórios, descrição opcional. Pode ser localização atual ou fazer parte de uma transferência. |
| Equipamento | Patrimônio único, identificação, marca, modelo, observação, estado, categoria e localização atual. |
| Movimentação | Equipamento, origem, destino, data/hora e observação. Um equipamento pode ter várias. |
| Manutenção | Equipamento, tipo, datas de início/fim, descrição, status e observação. Um equipamento pode ter várias. |
| Baixa | Equipamento, data, motivo e observação. Cada equipamento pode ter uma baixa. |

## Regras adotadas para a entrega

- O estado de um equipamento ativo pode ser `DISPONIVEL`, `EM_USO` ou `EM_MANUTENCAO`. O administrador escolhe o estado, inclusive durante a manutenção. A manutenção tem status próprio.
- Para trocar a localização atual, o administrador registra uma movimentação para um destino cadastrado diferente da origem.
- A manutenção é preventiva ou corretiva. A data final deve ser igual ou posterior à inicial e é obrigatória para concluir a manutenção.
- A baixa é definitiva. O equipamento fica disponível para consulta com seus históricos, e o serviço bloqueia novas alterações.
- Categorias e localizações vinculadas ao inventário ou a movimentações continuam disponíveis para referência.
- Os atributos específicos por categoria permanecem em aberto no documento de requisitos.

As classes de cadastro usam Lombok e Bean Validation. Os registros de histórico usam records Java. A persistência usa JDBC e o banco gera os IDs.


A proposta inicial em PDF menciona responsáveis, setores e especificações. Nesta versão,
bloco e local representam a localização organizacional. A lista RF01–RF23 e o diagrama de
classes orientam a entrega, com cadastro de responsáveis e especificações por categoria
fora do modelo implementado.
