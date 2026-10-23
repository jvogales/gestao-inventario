# Requisitos do Sistema

## Sistema de Inventário e Controle de Equipamentos

Este documento apresenta os requisitos funcionais e não funcionais do sistema.

**Ator do sistema:** Administrador

---

## Requisitos Funcionais

| ID | Requisito | Descrição |
|---|---|---|
| RF01 | Listar equipamentos | O sistema deve permitir ao Administrador visualizar os equipamentos cadastrados no inventário. |
| RF02 | Pesquisar equipamentos | O sistema deve permitir ao Administrador pesquisar equipamentos cadastrados por seus dados de identificação. |
| RF03 | Filtrar equipamentos | O sistema deve permitir ao Administrador filtrar os equipamentos por categoria, localização ou estado. |
| RF04 | Cadastrar equipamento | O sistema deve permitir ao Administrador cadastrar novos equipamentos no inventário. |
| RF05 | Consultar equipamento | O sistema deve permitir ao Administrador visualizar os dados detalhados de um equipamento cadastrado. |
| RF06 | Editar equipamento | O sistema deve permitir ao Administrador alterar os dados cadastrais de um equipamento. |
| RF07 | Cadastrar categoria | O sistema deve permitir ao Administrador cadastrar categorias de equipamentos. |
| RF08 | Consultar categorias | O sistema deve permitir ao Administrador visualizar as categorias de equipamentos cadastradas. |
| RF09 | Editar categoria | O sistema deve permitir ao Administrador alterar uma categoria cadastrada. |
| RF10 | Cadastrar localização | O sistema deve permitir ao Administrador cadastrar localizações para os equipamentos. |
| RF11 | Consultar localizações | O sistema deve permitir ao Administrador visualizar as localizações cadastradas. |
| RF12 | Editar localização | O sistema deve permitir ao Administrador alterar uma localização cadastrada. |
| RF13 | Consultar estado do equipamento | O sistema deve permitir ao Administrador visualizar o estado atual de um equipamento. |
| RF14 | Alterar estado do equipamento | O sistema deve permitir ao Administrador alterar o estado atual de um equipamento. |
| RF15 | Registrar movimentação | O sistema deve permitir ao Administrador registrar a transferência de um equipamento para outra localização. |
| RF16 | Atualizar localização após movimentação | O sistema deve atualizar a localização atual do equipamento após uma movimentação. |
| RF17 | Registrar histórico de movimentações | O sistema deve armazenar as movimentações realizadas para cada equipamento. |
| RF18 | Consultar histórico de movimentações | O sistema deve permitir ao Administrador consultar o histórico de movimentações de um equipamento. |
| RF19 | Registrar manutenção | O sistema deve permitir ao Administrador registrar uma manutenção para um equipamento. |
| RF20 | Editar manutenção | O sistema deve permitir ao Administrador atualizar as informações de uma manutenção registrada. |
| RF21 | Consultar histórico de manutenções | O sistema deve permitir ao Administrador consultar as manutenções registradas para um equipamento. |
| RF22 | Registrar baixa | O sistema deve permitir ao Administrador registrar a baixa de um equipamento. |
| RF23 | Consultar equipamento baixado | O sistema deve permitir ao Administrador consultar equipamentos que tenham sido baixados. |

---

## Requisitos Não Funcionais

| ID | Requisito | Descrição |
|---|---|---|
| RNF01 | Plataforma Web | O sistema deve ser disponibilizado como uma aplicação web acessível por navegador. |
| RNF02 | Linguagem de desenvolvimento | O backend do sistema deve ser desenvolvido em Java. |
| RNF03 | Arquitetura | O sistema deve utilizar o padrão arquitetural MVC. |
| RNF04 | Banco de dados | Os dados persistentes do sistema devem ser armazenados em PostgreSQL. |
| RNF05 | Acesso aos dados | A comunicação entre a aplicação e o banco de dados deve utilizar JDBC. |
| RNF06 | Gerenciamento de dependências | O projeto deve utilizar Maven para gerenciamento de dependências. |
| RNF07 | Usabilidade | A interface deve apresentar as funcionalidades do sistema de forma clara e de fácil utilização. |
| RNF08 | Validação de dados | O sistema deve validar os dados fornecidos antes de armazená-los. |
| RNF09 | Manutenibilidade | O código-fonte deve possuir classes e métodos com responsabilidades bem definidas. |
| RNF10 | Versionamento | O código-fonte e os documentos do projeto devem ser versionados utilizando Git e GitHub. |

---

## Observação

A definição dos atributos específicos de cada categoria de equipamento permanecerá em aberto nesta etapa e será tratada posteriormente durante a elaboração do diagrama de classes.
