# Tarefas de Implementação: Iptables Visual Builder

Este documento detalha as tarefas necessárias para implementar o "Iptables Visual Builder", com base no plano de implementação.

## Fase 1: Fundação do Projeto (Setup)
- **Objetivo**: Configurar a estrutura base do projeto Maven.

- [x] TSK001: **Setup do `pom.xml`**: Criar o arquivo `pom.xml` definindo o projeto para Java 17+ e adicionando as dependências do `javafx-controls`, `javafx-fxml`, `junit-jupiter-api`, e `mockito-core`.
- [x] TSK002: **Configurar Fat JAR**: Adicionar e configurar o `maven-shade-plugin` no `pom.xml` para empacotar todas as dependências em um único arquivo JAR executável.
- [x] TSK003: **Criar Estrutura de Pacotes**: Criar a estrutura de diretórios inicial: `src/main/java/br/com/ifirewall/core`, `.../infra`, e `.../ui`.

## Fase 2: Lógica de Negócio (Core)
- **Objetivo**: Implementar o domínio e a lógica de geração de regras.

- [x] TSK004: **Implementar Modelos de Domínio**: Criar os records/classes imutáveis para `Chain`, `Protocol`, `Action`, `Port`, e `IPAddress` no pacote `core.model`.
- [x] TSK005: **Criar Agregado de Regra**: Implementar a classe `FirewallRule` que compõe os objetos de domínio para formar uma regra de firewall completa.
- [x] TSK006: **Desenvolver Gerador de Script**: Criar a classe `RuleGeneratorService` no pacote `core.service` que recebe uma `List<FirewallRule>` e possui um método `generate()`.
- [x] TSK007: **Implementar Lógica do Cabeçalho**: Adicionar a lógica no `RuleGeneratorService` para gerar o cabeçalho do script (flush, políticas padrão DROP, regra de loopback).

## Fase 3: Testes Unitários (TDD)
- **Objetivo**: Garantir a corretude do motor de geração de regras.

- [x] TSK008: **[P] Testar Geração de Regra TCP**: Escrever um teste unitário para o `RuleGeneratorService` que valida a geração de uma regra `iptables` com protocolo TCP e porta.
- [x] TSK009: **[P] Testar Geração de Regra UDP**: Escrever um teste unitário que valida a geração de uma regra com protocolo UDP e porta.
- [x] TSK010: **[P] Testar Geração de Regra ICMP**: Escrever um teste unitário que valida a geração de uma regra com protocolo ICMP.
- [x] TSK011: **Testar Script Completo**: Escrever um teste que valida a geração do script completo, incluindo cabeçalho e múltiplas regras.

## Fase 4: Interface do Usuário (UI)
- **Objetivo**: Construir a interface visual com JavaFX.

- [x] TSK012: **Criar Janela Principal**: Desenvolver a janela principal da aplicação (`MainView.fxml`) com as áreas para a Toolbox, o Canvas e o Live Preview.
- [x] TSK013: **Desenvolver Blocos Visuais**: Criar os componentes visuais em JavaFX que representam cada elemento do domínio (`Chain`, `Protocol`, etc.).
- [x] TSK014: **Implementar Canvas Drag & Drop**: Implementar a lógica de arrastar e soltar no Canvas, permitindo que os blocos sejam adicionados e conectados.
- [x] TSK015: **Conectar UI ao Core**: Fazer com que as ações no Canvas (adicionar/remover/conectar blocos) atualizem uma lista de `FirewallRule` e chamem o `RuleGeneratorService`.
- [x] TSK016: **Implementar Live Preview**: Exibir a string retornada pelo `RuleGeneratorService` em um `TextArea` ou componente similar no painel de Live Preview.

## Fase 5: Infraestrutura e Exportação (Infra)
- **Objetivo**: Implementar as funcionalidades de saída do script.

- [ ] TSK017: **Implementar Cópia para Clipboard**: Criar um `ClipboardService` e conectá-lo a um botão "Copiar Script" na UI.
- [ ] TSK018: **Implementar Exportação para .txt**: Criar um `FileExportService` que usa o `FileChooser` do JavaFX para salvar o script em um arquivo `.txt`. Conectar a um botão "Gerar .txt".

## Fase 6: Finalização e Validação (Polish)
- **Objetivo**: Revisar, testar e empacotar a aplicação.

- [ ] TSK019: **Revisão de Código**: Realizar uma passagem de refatoração para garantir que o código segue os princípios da Constituição (Clean Code, SOLID).
- [ ] TSK020: **Teste de Aceite Manual**: Executar o JAR gerado em pelo menos dois sistemas operacionais diferentes (ex: Windows e Linux) para validar a portabilidade e a funcionalidade completa.