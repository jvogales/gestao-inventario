# API REST

URL padrão `http://localhost:8081`. Para POST e PUT, envie `Content-Type: application/json`.

## Catálogos e equipamentos

| Recurso | Operações |
| --- | --- |
| `/categorias` | GET lista e POST cadastra. Em `/{id}`, GET consulta, PUT edita e DELETE exclui se estiver sem vínculos. |
| `/localizacoes` | GET lista e POST cadastra. Em `/{id}`, GET consulta, PUT edita e DELETE exclui se estiver sem vínculos, inclusive históricos. |
| `/equipamentos` | GET lista e POST cadastra. Em `/{id}`, GET consulta e PUT edita os dados e o estado. |

POST de cadastro retorna `201` com `Location`. PUT recebe todos os campos obrigatórios e retorna `200`. IDs são gerados no servidor.

Exemplo de sequência de cadastro. Substitua os IDs pelos retornados pela aplicação.

| Requisição | JSON |
| --- | --- |
| `POST /categorias` | `{"nome":"Informática","descricao":"Computadores"}` |
| `POST /localizacoes` | `{"bloco":"A","local":"Laboratório","descricao":"Sala 1"}` |
| `POST /equipamentos` | `{"numeroPatrimonio":"PAT-001","identificacao":"Computador","estado":"DISPONIVEL","categoriaId":1,"localizacaoId":1}` |

`marca`, `modelo` e `observacao` são opcionais no equipamento. A resposta inclui os objetos `categoria` e `localizacaoAtual`. A edição usa o ID da localização atual. Para alterar a localização, registre uma movimentação.

`GET /equipamentos?pesquisa=pat-001&categoriaId=1&localizacaoId=1&estado=DISPONIVEL` aceita qualquer combinação dos filtros. A pesquisa encontra trechos do patrimônio ou da identificação, sem diferenciar maiúsculas de minúsculas.

## Histórico patrimonial

Os caminhos abaixo são relativos a `/equipamentos/{equipamentoId}`.

| Método e caminho | Resultado |
| --- | --- |
| `GET /movimentacoes` | Lista o histórico de movimentações. |
| `POST /movimentacoes` | Retorna `201`, grava origem, destino, data/hora e observação e atualiza a localização atual. |
| `GET /manutencoes` | Lista o histórico de manutenções. |
| `POST /manutencoes` | Retorna `201` com a manutenção registrada. |
| `PUT /manutencoes/{id}` | Retorna `200` com a manutenção atualizada. |
| `POST /baixa` | Retorna `201`, registra a baixa e muda o estado para `BAIXADO`. |
| `GET /baixa` | Consulta a baixa registrada. |

- Movimentação: `{"destinoId":2,"observacao":"Transferência para o almoxarifado"}`
- Manutenção: `{"tipo":"PREVENTIVA","dataInicio":"2026-10-20","descricao":"Limpeza","status":"ABERTA"}`
- Conclusão: informe também `"dataFim":"2026-10-21"` e use `"status":"CONCLUIDA"`. Os tipos são `PREVENTIVA` e `CORRETIVA`. Os status são `ABERTA`, `EM_ANDAMENTO`, `CONCLUIDA` e `CANCELADA`.
- Baixa: `{"data":"2026-10-22","motivo":"Fim da vida útil","observacao":"Laudo arquivado"}`

A rota legada `DELETE /equipamentos/{id}` retorna `204` e registra baixa lógica com motivo padrão. Para informar o motivo, use `POST /baixa`. O equipamento e seus históricos continuam disponíveis pelas rotas GET.

## Respostas de erro

- `400` para dados inválidos, enum desconhecido ou tentativa de definir `BAIXADO` pelo cadastro/edição.
- `404` para registros ou referências ausentes.
- `409` para patrimônio ou nome de categoria duplicado, exclusão de catálogo em uso, movimentação para a origem, mudança direta de localização ou alteração de equipamento já baixado.

As respostas de erro usam `ProblemDetail`. Erros de validação do JSON incluem o objeto `campos`.
