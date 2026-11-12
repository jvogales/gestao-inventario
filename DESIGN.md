# Interface do inventário

Interface em português brasileiro para o administrador consultar e manter o patrimônio.
O patrimônio aparece em tipo monoespaçado, como nas etiquetas dos bens, e abre a ficha do equipamento.

- Superfícies em branco e fundo azul-acinzentado. Azul-marinho na navegação e verde-petróleo nas ações principais.
- Barlow para títulos, Source Sans 3 para leitura e IBM Plex Mono para patrimônio, com fontes de sistema como alternativas locais.
- Cabeçalho, alertas e validação em `layout.html`. Cores, espaçamento, estados e foco em `inventario.css`.
- Formulários usam controles nativos e mensagens de validação do servidor. Após salvar, redirecionam para a lista ou a ficha.
- A lista mantém filtros na URL. Em telas estreitas, o formulário se reorganiza e a tabela rola dentro da própria região.
- A baixa tem uma tela de confirmação com motivo e data obrigatórios. A ficha continua acessível com estado e histórico.
