# Plano de Implementação: Iptables Visual Builder

Este plano detalha os passos para desenvolver a aplicação "Iptables Visual Builder", com foco nos requisitos de portabilidade (Java/Maven) e nas opções de exportação de script, seguindo a Constituição do projeto.

## 1. Estrutura do Projeto e Ambiente (Foundation)

- **Objetivo**: Criar um projeto Maven robusto que sirva como base para toda a aplicação, garantindo a portabilidade.
- **Ações**:
    - Inicializar um novo projeto Maven.
    - Configurar o `pom.xml`:
        - Propriedades para usar `Java 17+`.
        - Dependências do `JavaFX` (`javafx-controls`, `javafx-fxml`).
        - Dependências de teste (`JUnit 5`, `Mockito`).
        - Plugin `maven-shade-plugin` para criar um "fat JAR" executável que funcione em qualquer JVM.
    - Definir a estrutura de pacotes seguindo os princípios de Clean Architecture: `core` (domínio, casos de uso), `infra` (frameworks, drivers), `ui` (interface).

## 2. Modelo de Domínio (Core)

- **Objetivo**: Modelar as entidades do firewall de forma agnóstica à interface ou ao formato de saída.
- **Ações**:
    - Criar classes/records imutáveis para os componentes de uma regra: `Chain` (INPUT, OUTPUT), `Protocol` (TCP, UDP), `Action` (ACCEPT, DROP), `Port`, `IPAddress`.
    - Criar uma classe `FirewallRule` que agrega esses componentes, representando uma regra completa.
    - Implementar validações no domínio (ex: uma regra com porta deve ter um protocolo TCP ou UDP).

## 3. Motor de Geração de Script (Use Case)

- **Objetivo**: Isolar a lógica que traduz o modelo de domínio em texto `iptables` (SRP).
- **Ações**:
    - Criar a classe `RuleGeneratorService` que recebe uma lista de `FirewallRule` e retorna uma `String` com o script completo.
    - Implementar a geração do cabeçalho do script (flush de regras, políticas padrão, etc.).
    - **Testes**: Criar testes unitários para o `RuleGeneratorService` que verifiquem a correta tradução de cada tipo de regra para o formato de texto esperado.

## 4. Interface Visual com JavaFX (UI)

- **Objetivo**: Construir a interface de arrastar e soltar.
- **Ações**:
    - Desenvolver os componentes visuais para os blocos de regras.
    - Implementar o Canvas que recebe os blocos e gerencia as conexões lógicas.
    - Conectar o estado do Canvas ao `RuleGeneratorService` para alimentar o painel de "Live Preview".

## 5. Funcionalidades de Exportação (Infra)

- **Objetivo**: Implementar as opções de saída para o usuário, conforme solicitado.
- **Ações**:
    - Criar um `ClipboardService` que utiliza o `java.awt.Toolkit` para copiar o script gerado para a área de transferência.
    - Criar um `FileExportService` que utiliza `java.nio.file` para salvar o script em um arquivo `.txt`, usando o `FileChooser` do JavaFX para que o usuário escolha o local.
    - Integrar esses serviços à UI através de botões ("Copiar Script", "Gerar .txt").

## Critérios de Sucesso

- [ ] O projeto é buildado com `mvn package` e gera um JAR único e executável.
- [ ] A aplicação inicia e exibe a interface em Windows, macOS e Linux (com JVM instalada).
- [ ] A funcionalidade de "Copiar Script" funciona corretamente.
- [ ] A aplicação gera um arquivo `.txt` com o conteúdo do script de firewall.
- [ ] A lógica de geração de regras possui cobertura de testes unitários.