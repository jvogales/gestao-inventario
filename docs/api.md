# API REST — CRUD em memória

Execute `./mvnw spring-boot:run` (Windows: `.\mvnw.cmd spring-boot:run`).
No Postman, use `http://localhost:8080` como base e selecione **Body → raw → JSON**
para POST e PUT. Envie `Content-Type: application/json`.

Os dados ficam em memória e são perdidos ao reiniciar. IDs são gerados no servidor.
Não envie um ID para escolher o identificador de um cadastro.

## Rotas e respostas

As mesmas operações estão disponíveis para `/equipamentos`, `/categorias` e
`/localizacoes`:

| Método | Caminho | Sucesso | Recurso inexistente |
| --- | --- | --- | --- |
| GET | `/{recurso}` | `200 OK`, lista (pode ser vazia) | — |
| GET | `/{recurso}/{id}` | `200 OK`, objeto | `404 Not Found` |
| POST | `/{recurso}` | `201 Created`, objeto e cabeçalho `Location` | `404` se uma referência não existir |
| PUT | `/{recurso}/{id}` | `200 OK`, objeto atualizado | `404 Not Found` |
| DELETE | `/{recurso}/{id}` | `204 No Content`, sem corpo | `404 Not Found` |

PUT recebe todos os campos obrigatórios; não é uma atualização parcial.
O ID do caminho identifica o registro. Campos opcionais omitidos no PUT ficam nulos.

## Sequência para testar no Postman

### 1. Cadastrar categoria

`POST /categorias`

```json
{
  "nome": "Informática",
  "descricao": "Computadores e periféricos"
}
```

Guarde o `id` retornado.

### 2. Cadastrar localização

`POST /localizacoes`

```json
{
  "bloco": "A",
  "local": "Laboratório 1",
  "descricao": "Laboratório de informática"
}
```

Guarde o `id` retornado.

### 3. Cadastrar equipamento

`POST /equipamentos`. Substitua os IDs pelos valores das respostas anteriores.

```json
{
  "numeroPatrimonio": "PAT-001",
  "identificacao": "Computador do laboratório",
  "marca": "Dell",
  "modelo": "OptiPlex",
  "observacao": "Equipamento de uso acadêmico",
  "estado": "DISPONIVEL",
  "categoriaId": 1,
  "localizacaoId": 1
}
```

O corpo de entrada usa IDs para referenciar registros existentes. A resposta traz
`categoria` e `localizacaoAtual` como objetos completos. Um DTO (`EquipamentoRequest`)
separa esse formato de entrada da entidade.

### 4. Consultar, editar e excluir

- `GET /equipamentos`: lista todos.
- `GET /equipamentos/1`: consulta um registro pelo ID retornado no cadastro.
- `PUT /equipamentos/1`: envie o JSON do cadastro com os novos dados.
- `DELETE /equipamentos/1`: remove o registro e retorna corpo vazio.
- `GET /equipamentos/1` após excluir: retorna `404`.

Para categorias e localizações, use os mesmos verbos nas respectivas rotas,
com seus próprios campos. Categoria/localização vinculada a equipamento não pode
ser excluída: retorna `409 Conflict`. Editar seus dados atualiza também a referência
exibida nos equipamentos.

### 5. Pesquisar e filtrar

`GET /equipamentos?pesquisa=computador&categoriaId=1&localizacaoId=1&estado=DISPONIVEL`

Todos os parâmetros são opcionais e podem ser combinados. A pesquisa procura
trechos do patrimônio ou identificação sem diferenciar maiúsculas e minúsculas.
Os estados iniciais são `DISPONIVEL`, `EM_USO`, `EM_MANUTENCAO` e `BAIXADO`.

## Erros

- `400 Bad Request`: campos obrigatórios ausentes, IDs de referência não positivos,
  JSON inválido ou valor de enum inválido.
- `404 Not Found`: registro ou categoria/localização referenciada inexistente.
- `409 Conflict`: patrimônio duplicado ou tentativa de excluir categoria/localização em uso.

Erros usam `ProblemDetail` com `status` e `detail`. Erros de validação também
possuem `campos`, relacionando os nomes dos campos às mensagens.
Patrimônios são comparados sem diferenciar maiúsculas/minúsculas e ignorando
espaços nas extremidades; essa é a convenção adotada neste CRUD inicial.

## Arquitetura e limites desta etapa

```text
Requisição HTTP → Controller → Service → Repository em memória
```

`@Data` do Lombok gera getters, setters, `equals`, `hashCode` e `toString` na
compilação. `@NoArgsConstructor` e `@AllArgsConstructor` geram os construtores.
`@RequiredArgsConstructor` gera a injeção por construtor dos campos `final`.
Os controllers recebem HTTP e usam `ResponseEntity`; os services aplicam as
regras; os repositories armazenam os objetos.

O DELETE é parte do exercício de CRUD e **não implementa a baixa patrimonial**
do RF22. A baixa exige registro e preservação do histórico. Movimentação,
manutenção e suas regras, incluindo histórico de alterações de localização e
transições de estado, ainda não estão implementadas. O PUT atual edita os dados
básicos e não substitui essas operações. A persistência futura continuará sendo
PostgreSQL com JDBC, conforme os requisitos.
