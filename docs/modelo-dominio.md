# Modelo de domínio — primeira etapa

Esta etapa implementa `Equipamento`, `Categoria`, `Localizacao` e o enum
`EstadoEquipamento`, conforme o [diagrama de classes](diagramas/classes/diagrama-de-classes-entidades.png).
Os nomes e tipos dos atributos foram mantidos. As associações foram representadas
em `Equipamento` por `categoria` e `localizacaoAtual`: vários equipamentos podem
referenciar a mesma categoria ou localização, e cada equipamento deve ter uma de cada.

As classes usam `@Data`, `@NoArgsConstructor` e `@AllArgsConstructor` do Lombok
para gerar getters/setters e construtores na compilação. São objetos Java sem
anotações JPA, pois o RNF05 exige JDBC. IDs ficam nulos antes do cadastro;
o PostgreSQL gera os identificadores.

## Validação

O RNF08 e o diagrama de cadastro exigem validar os dados antes de salvar, mas não
enumeram todos os campos obrigatórios. Nesta implementação, adotamos:

- `Categoria`: nome preenchido.
- `Localizacao`: bloco e local preenchidos.
- `Equipamento`: patrimônio, identificação, estado, categoria e localização atual preenchidos.

As referências de categoria e localização também são validadas com `@Valid`
quando o modelo é validado diretamente. Na API, `EquipamentoRequest` recebe
os IDs dessas referências; o service busca os registros existentes.
Marca, modelo, descrições e observações permanecem opcionais; não foram inventados
limites de tamanho. As anotações são executadas pelo Bean Validation: os
controllers acionam a validação de entrada com `@Valid`. Setters não fazem
validação automática.

## Decisões ainda abertas na documentação original

O diagrama cita `EstadoEquipamento`, mas não especifica suas constantes. A proposta
inicial no código é `DISPONIVEL`, `EM_USO`, `EM_MANUTENCAO` e `BAIXADO`.
Esses valores precisam acompanhar a evolução dos requisitos; não há regras de
transição presumidas nesta etapa.

Os atributos específicos por categoria continuam em aberto, conforme a observação
final do documento de requisitos.

## Módulos concluídos

`Movimentacao`, `Manutencao`, `Baixa` e seus enums foram implementados conforme
o diagrama. A unicidade do patrimônio, referências, históricos, permissões de
movimentação e persistência JDBC estão ativas. Consulte as [rotas da API](api.md).
