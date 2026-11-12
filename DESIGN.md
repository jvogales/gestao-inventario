# Direção visual — Gestão de Inventário

## North Star

Painel operacional de patrimônio acadêmico inspirado em etiquetas técnicas e plantas de laboratório: claro, direto e confiável, sem aparência de template corporativo genérico.

## Tokens

- `ink`: `#14213D` — texto e navegação.
- `paper`: `#F4F7FB` — fundo da aplicação.
- `panel`: `#FFFFFF` — superfícies de trabalho.
- `cobalt`: `#2556D8` — ação principal e foco.
- `signal`: `#B8F03D` — destaque de inventário ativo.
- `danger`: `#C93B49` — baixa e erros.
- Tipografia: system sans para leitura; `ui-monospace` em patrimônio, estados e metadados.
- Raio: 14px em painéis, 8px em controles.

## Layout e assinatura

Navegação lateral escura, área de trabalho clara e uma faixa de “etiqueta patrimonial” no topo. A assinatura visual é o padrão discreto de grade técnica no cabeçalho e os identificadores monoespaçados.

## Comportamento

- CRUD e operações retornam ao painel com mensagem de sucesso/erro.
- Formulários usam validação da aplicação (`novalidate`) e campos rotulados.
- Selects e datas são nativos; a geometria do popup pertence ao sistema operacional.
- Listas são tabelas semânticas com rolagem horizontal em telas estreitas.
- Nenhum dado é apagado pela baixa; o equipamento permanece consultável.
