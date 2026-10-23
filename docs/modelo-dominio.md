# Modelo de domínio — primeira etapa

Esta etapa implementa `Equipamento`, `Categoria`, `Localizacao` e o enum
`EstadoEquipamento`, conforme o [diagrama de classes](diagramas/classes/diagrama-de-classes-entidades.png).
Os nomes e tipos dos atributos foram mantidos. As associações foram representadas
em `Equipamento` por `categoria` e `localizacaoAtual`: vários equipamentos podem
referenciar a mesma categoria ou localização, e cada equipamento deve ter uma de cada.

As classes possuem atributos privados, construtor vazio, construtor completo e
getters/setters. São objetos Java sem anotações JPA, pois o RNF05 exige JDBC.
IDs podem ficar nulos antes do cadastro; sua geração caberá à persistência.

## Validação

O RNF08 e o diagrama de cadastro exigem validar os dados antes de salvar, mas não
enumeram todos os campos obrigatórios. Nesta implementação, adotamos:

- `Categoria`: nome preenchido.
- `Localizacao`: bloco e local preenchidos.
- `Equipamento`: patrimônio, identificação, estado, categoria e localização atual preenchidos.

As referências de categoria e localização também são validadas com `@Valid`.
Marca, modelo, descrições e observações permanecem opcionais; não foram inventados
limites de tamanho. As anotações são executadas pelo Bean Validation: futuros
controllers/services precisarão acioná-las antes de persistir. Setters não fazem
validação automática.

## Decisões ainda abertas na documentação original

O diagrama cita `EstadoEquipamento`, mas não especifica suas constantes. A proposta
inicial no código é `DISPONIVEL`, `EM_USO`, `EM_MANUTENCAO` e `BAIXADO`.
Esses valores precisam acompanhar a evolução dos requisitos; não há regras de
transição presumidas nesta etapa.

Os atributos específicos por categoria continuam em aberto, conforme a observação
final do documento de requisitos.

## Próximas etapas

`Movimentacao`, `Manutencao`, `Baixa` e seus enums continuam previstos no diagrama.
Serão implementados nas próximas etapas, junto de seus comportamentos.
A unicidade do patrimônio, a consulta de referências existentes, o registro de
históricos, as permissões de movimentação e a persistência são responsabilidades
das futuras camadas de serviço e repositório. Nenhum endpoint de cadastro ou
CRUD foi criado nesta etapa.
