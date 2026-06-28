# Checklist de Implementação: Iptables Visual Builder

**Propósito**: Garantir que todos os requisitos funcionais e não funcionais para o Iptables Visual Builder sejam atendidos antes do lançamento.
**Criação**: 2026-06-28
**Feature**: `e:\OneDrive\Documentos\TCC-projeto\IFirewall\.specify\memory\spec.md`

## 1. Fundação do Projeto (Maven & Estrutura)

- [ ] CHK001: O `pom.xml` está configurado para Java 17+ e inclui as dependências do JavaFX, JUnit 5 e Mockito.
- [ ] CHK002: O plugin `maven-shade-plugin` está configurado para gerar um "fat JAR" executável.
- [ ] CHK003: O build com `mvn package` cria um JAR funcional que pode ser executado com `java -jar`.
- [ ] CHK004: A estrutura de pacotes (`core`, `infra`, `ui`) está criada para seguir os princípios de Clean Architecture.

## 2. Core & Lógica de Negócio

- [ ] CHK005: Classes/Records imutáveis para `Chain`, `Protocol`, `Action`, `Port` e `IPAddress` foram implementadas.
- [ ] CHK006: A classe `FirewallRule` que agrega os componentes de uma regra está implementada.
- [ ] CHK007: Validações de domínio (ex: porta requer protocolo TCP/UDP) estão funcionando.
- [ ] CHK008: O `RuleGeneratorService` traduz corretamente uma lista de objetos `FirewallRule` para um script `iptables`.
- [ ] CHK009: O script gerado inclui o cabeçalho de segurança (flush de regras, políticas padrão DROP).

## 3. Testes

- [ ] CHK010: Testes unitários para o `RuleGeneratorService` cobrem todos os cenários de tradução de regras (TCP, UDP, ICMP).
- [ ] CHK011: A cobertura de testes para a lógica de negócio atinge o mínimo definido pelo projeto.

## 4. Interface do Usuário (JavaFX)

- [ ] CHK012: O painel de ferramentas (Toolbox) exibe todos os blocos de construção de regras.
- [ ] CHK013: A funcionalidade de arrastar e soltar (Drag & Drop) no Canvas está funcional e intuitiva.
- [ ] CHK014: A interface fornece feedback visual para conexões de blocos inválidas.
- [ ] CHK015: O painel de "Live Preview" é atualizado em tempo real conforme as regras são montadas no Canvas.
- [ ] CHK016: A funcionalidade de "Novo Projeto" limpa o canvas para uma nova configuração.
- [ ] CHK017: A interface possui botões para "Copiar Script" e "Gerar .txt".

## 5. Funcionalidades de Exportação e Persistência

- [ ] CHK018: O botão "Copiar Script" copia o conteúdo do Live Preview para a área de transferência do sistema.
- [ ] CHK019: O botão "Gerar .txt" abre um seletor de arquivos e salva o script em um arquivo de texto.
- [ ] CHK020: A funcionalidade de Salvar/Carregar o estado do canvas em um arquivo JSON está implementada e funcionando.

## 6. Critérios de Aceite Finais

- [ ] CHK021: A aplicação é executável e funcional em Windows, Linux e macOS (com JVM instalada).
- [ ] CHK022: O script `.sh` gerado (via exportação de texto) é sintaticamente válido e executável em um ambiente Linux.
- [ ] CHK023: A experiência do usuário é fluida, sem erros de console durante o uso normal.