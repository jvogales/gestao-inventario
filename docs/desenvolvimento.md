# Guia de desenvolvimento


Pré-requisitos: **JDK 17 ou superior** (não apenas JRE), Git e acesso à internet
para o primeiro download das dependências. Não precisa instalar Maven nem configurar
PostgreSQL nesta etapa. A versão do Maven é definida pelo Wrapper do projeto.

Clone o repositório e entre na pasta `gestao-inventario`. Se já tiver um clone,
atualize-o preservando suas alterações locais.

Confira `java -version` e `javac -version`. Se houver erro, instale um JDK e
configure `JAVA_HOME` para a pasta dele, com seu diretório `bin` no `PATH`.

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Windows (PowerShell):

```powershell
.\mvnw.cmd spring-boot:run
```

No computador onde executar, acesse `http://localhost:8080/actuator/health`.
A resposta esperada é `{"status":"UP"}`. A raiz `/` ainda não tem tela e retorna
404. Encerre o servidor com `Ctrl+C`. Para outra porta, configure a variável `PORT`.

Compilar, executar o teste de inicialização HTTP e gerar o JAR:

```bash
./mvnw clean verify
java -jar target/gestao-inventario-0.0.1-SNAPSHOT.jar
```

No Windows, substitua `./mvnw` por `.\mvnw.cmd`. No IntelliJ, Eclipse ou VS Code,
abra a pasta como projeto Maven e selecione o JDK 17 ou superior.

No NetBeans, use **Run Project** para iniciar `InventarioApplication`.
O `pom.xml` define `start-class`, usada pela ação Run da IDE.
**Clean and Build** compila e testa, mas não mantém o servidor ligado.
Se um clone antigo apresentar `ClassNotFoundException: ${start-class}`,
atualize com `git pull` e recarregue o projeto Maven antes de executar novamente.

As entidades usam Lombok (`@Data` e anotações de construtores). Se a IDE marcar
os métodos gerados como ausentes, habilite o processamento de anotações e o
suporte a Lombok da IDE; o build pelo Wrapper já processa as anotações.

O Wrapper instala Maven, mas não instala Java. Em computadores da universidade
com instalação restrita, use um JDK autorizado pela instituição ou execute o
projeto no ambiente em nuvem. Não é necessário copiar a pasta `target` entre máquinas.
Para compartilhar mudanças, faça commit e push no GitHub e atualize o clone na outra máquina.

## Estrutura inicial

```text
src/main/java/com/jvogales/inventario/
├── InventarioApplication.java
├── controller/       # Requisições e respostas HTTP
├── service/          # Regras de negócio
├── repository/       # Acesso a dados (futuramente JDBC)
└── model/
    ├── entity/       # Equipamento, Categoria, Localizacao etc.
    └── enums/        # Estados e tipos do domínio
src/main/resources/
├── application.properties
├── templates/        # Futuras páginas Thymeleaf
└── static/           # CSS, JavaScript e imagens
```

Controllers, services e repositories implementam o CRUD em memória descrito
no [guia da API](api.md). Os pacotes estão documentados com `package-info.java`. `Equipamento`, `Categoria`,
`Localizacao` e `EstadoEquipamento` formam o modelo inicial; veja as
[decisões de implementação](modelo-dominio.md). Spring Web, Thymeleaf, Validation, Actuator e suporte a testes já estão
declarados no `pom.xml`. Somente o endpoint de saúde do Actuator está exposto.
JDBC e o driver PostgreSQL serão adicionados junto com a configuração de persistência,
para manter esta base executável sem banco e sem credenciais no repositório.

