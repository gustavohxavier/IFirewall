# Tasks: Iptables Visual Builder (Drag & Drop)

**Input**: Design documents em `.specify/memory/` (plan.md, spec.md, research.md, data-model.md, contracts/, quickstart.md)

**Tests**: Incluídos — a spec exige cobertura de testes unitários (Critérios de Aceite) e a Constituição (Princípio IV) exige testes unitários + integração.

**Organization**: Tarefas agrupadas por user story para implementação e teste independentes.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Pode rodar em paralelo (arquivos diferentes, sem dependências)
- **[Story]**: User story à qual a tarefa pertence (US1, US2, US3)
- Caminhos de arquivo são relativos à raiz do repositório

## User Stories (prioridade)

- **US1 (P1)** 🎯 MVP — Montar uma regra visualmente e ver o comando `iptables` em tempo real.
- **US2 (P2)** — Exportar o script gerado (clipboard, `.txt`, `.sh`).
- **US3 (P3)** — Persistir configurações (Salvar/Carregar em JSON e "Novo Projeto").

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Inicialização do projeto Maven e dependências.

- [x] T001 Criar `pom.xml` (Java 17, `javafx-controls`, `javafx-fxml`, `junit-jupiter-api`, `mockito-core`) em `pom.xml`
- [x] T002 [P] Configurar plugins de teste/execução em `pom.xml`: `maven-surefire-plugin` 3.2.5, `junit-jupiter-engine`, `javafx-maven-plugin` 0.0.8 (`mvn javafx:run`)
- [x] T003 Configurar `maven-shade-plugin` (fat JAR) com `mainClass` = `br.com.ifirewall.ui.Launcher` em `pom.xml`, e criar `src/main/java/br/com/ifirewall/ui/Launcher.java`
- [x] T004 [P] Adicionar dependência JSON (`jackson-databind`) em `pom.xml` (ver D1 em research.md)
- [x] T005 Criar estrutura de pacotes `core`/`infra`/`ui` em `src/main/java/br/com/ifirewall/`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Domínio e motor de geração — usados por todas as user stories.

**⚠️ CRITICAL**: Nenhuma user story pode ser concluída antes desta fase.

- [x] T006 [P] Criar enums `Chain`, `Protocol`, `Action` em `src/main/java/br/com/ifirewall/core/model/`
- [x] T007 [P] Criar value objects `Port` e `IPAddress` (com validação) em `src/main/java/br/com/ifirewall/core/model/`
- [x] T008 Criar agregado `FirewallRule` (validação de `chain`/`action` obrigatórios) em `src/main/java/br/com/ifirewall/core/model/FirewallRule.java` (depende de T006, T007)
- [x] T009 [P] Definir interface `RuleGenerator` em `src/main/java/br/com/ifirewall/core/service/RuleGenerator.java`
- [x] T010 Implementar `RuleGeneratorService.generate()` + cabeçalho (flush, políticas DROP, loopback) em `src/main/java/br/com/ifirewall/core/service/RuleGeneratorService.java`
- [x] T011 Implementar `RuleGeneratorService.translateRule()` conforme `contracts/rule-generator-contract.md`
- [x] T012 (DIP) Fazer `RuleGeneratorService implements RuleGenerator` e a UI depender da abstração `RuleGenerator` (ver D4 em research.md)

**Checkpoint**: Domínio + motor prontos — user stories podem começar.

---

## Phase 3: User Story 1 - Montagem visual com Live Preview (Priority: P1) 🎯 MVP

**Goal**: O usuário arrasta blocos (Chain, Protocol, Port, IP, Action) para o Canvas, conecta-os, e vê o comando `iptables` correspondente em tempo real no Live Preview.

**Independent Test**: Montar `INPUT + TCP + porta 22 + ACCEPT` e confirmar que o Live Preview exibe `iptables -A INPUT -p tcp --dport 22 -j ACCEPT` sem erros de console.

### Tests for User Story 1 ⚠️

- [x] T013 [P] [US1] Teste unitário de regra TCP em `src/test/java/br/com/ifirewall/core/service/RuleGeneratorServiceTest.java`
- [x] T014 [P] [US1] Teste unitário de regra UDP em `src/test/java/br/com/ifirewall/core/service/RuleGeneratorServiceTest.java`
- [x] T015 [P] [US1] Teste unitário de regra ICMP em `src/test/java/br/com/ifirewall/core/service/RuleGeneratorServiceTest.java`
- [x] T016 [US1] Teste unitário do script completo (cabeçalho + múltiplas regras) em `src/test/java/br/com/ifirewall/core/service/RuleGeneratorServiceTest.java`
- [x] T017 [US1] Teste de integração UI↔Core (ações no canvas refletem em `List<FirewallRule>` e no preview) em `src/test/java/br/com/ifirewall/ui/CanvasModelIT.java`

### Implementation for User Story 1

- [x] T018 [US1] Construir `MainView.fxml` com áreas Toolbox / Canvas / Live Preview em `src/main/resources/br/com/ifirewall/ui/MainView.fxml`
- [x] T019 [P] [US1] Implementar blocos visuais arrastáveis para cada elemento do domínio em `src/main/java/br/com/ifirewall/ui/component/DraggableNode.java` (toolbox populada em `MainViewController`)
- [x] T020 [US1] Implementar drag & drop no Canvas + conectores lógicos (`RuleDraft` valida Chain/Action e porta→TCP/UDP, com alerta visual) em `src/main/java/br/com/ifirewall/ui/`
- [x] T021 [US1] Conectar ações do Canvas a `CanvasModel`/`List<FirewallRule>` e ao `RuleGenerator` (abstração) em `src/main/java/br/com/ifirewall/ui/MainViewController.java`
- [x] T022 [US1] Exibir a saída do gerador no `TextArea` de Live Preview em `src/main/java/br/com/ifirewall/ui/MainViewController.java`

**Checkpoint**: US1 funcional e testável de forma independente (MVP).

---

## Phase 4: User Story 2 - Exportação do script (Priority: P2)

**Goal**: A partir da configuração atual, o usuário exporta o script para a área de transferência, para `.txt` e para `.sh` executável.

**Independent Test**: Com uma regra no canvas, usar cada botão de exportação e verificar clipboard, arquivo `.txt` e `.sh` (executável em Linux) com o conteúdo do preview.

### Implementation for User Story 2

- [x] T023 [US2] `ClipboardService` + botão "Copiar Script" em `src/main/java/br/com/ifirewall/infra/export/ClipboardService.java`
- [x] T024 [US2] `FileExportService` (`.txt`) + botão "Gerar .txt" (FileChooser na UI) em `src/main/java/br/com/ifirewall/infra/export/FileExportService.java`
- [x] T025 [US2] `ScriptExportService` (`.sh` com permissão de execução em POSIX, ver D2) + botão "Gerar .sh" em `src/main/java/br/com/ifirewall/infra/export/ScriptExportService.java`

**Checkpoint**: US1 e US2 funcionam independentemente.

---

## Phase 5: User Story 3 - Persistência de configurações (Priority: P3)

**Goal**: O usuário salva o estado do canvas em JSON, recarrega depois, e pode iniciar um projeto novo (canvas limpo).

**Independent Test**: Salvar um canvas com regras, fechar/recarregar via "Abrir" e confirmar que as regras e posições são restauradas; "Novo Projeto" limpa tudo.

### Tests for User Story 3 ⚠️

- [x] T026 [P] [US3] Teste de round-trip de serialização do `ProjectState` (save→load preserva regras) em `src/test/java/br/com/ifirewall/infra/persistence/ProjectPersistenceServiceTest.java`

### Implementation for User Story 3

- [x] T027 [P] [US3] Criar DTO `ProjectState` (regras + versão; layout dos nós adiado, ver contrato) em `src/main/java/br/com/ifirewall/infra/persistence/ProjectState.java`
- [x] T028 [US3] Implementar `ProjectPersistenceService` (Save/Load JSON via `FileChooser`) + botões "Salvar"/"Abrir" em `src/main/java/br/com/ifirewall/infra/persistence/ProjectPersistenceService.java`
- [x] T029 [US3] Implementar ação "Novo Projeto" (limpa canvas e reseta estado) — botão "Novo / Limpar" em `src/main/java/br/com/ifirewall/ui/MainViewController.java`

**Checkpoint**: Todas as user stories funcionais de forma independente.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Qualidade e validação final.

- [x] T030 [P] Revisão de código (Clean Code / SOLID conforme Constituição) em `src/`
- [ ] T031 Validação manual cross-OS (Windows + Linux) executando `quickstart.md` e os Critérios de Aceite

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: sem dependências.
- **Foundational (Phase 2)**: depende do Setup — **bloqueia** todas as user stories.
- **User Stories (Phase 3+)**: dependem da Phase 2. US2 e US3 integram com a UI da US1, mas são testáveis isoladamente (export e persistência podem usar `RuleGenerator`/`ProjectState` diretamente).
- **Polish (Phase 6)**: depende das stories desejadas.

### Within Each User Story

- Testes escritos antes da implementação (devem falhar primeiro).
- Modelos antes de serviços; serviços antes da UI; núcleo antes da integração.

### Parallel Opportunities

- Setup: T002 e T004 são `[P]`.
- Foundational: T006, T007 e T009 são `[P]`.
- US1: testes T013–T015 `[P]`; bloco visual T019 `[P]`.
- US3: T026 e T027 são `[P]`.
- Após a Phase 2, US1/US2/US3 podem ser tocadas em paralelo por pessoas diferentes.

---

## Parallel Example: User Story 1

```bash
# Testes unitários de US1 em paralelo:
Task: "Teste unitário de regra TCP em RuleGeneratorServiceTest.java"
Task: "Teste unitário de regra UDP em RuleGeneratorServiceTest.java"
Task: "Teste unitário de regra ICMP em RuleGeneratorServiceTest.java"
```

---

## Implementation Strategy

### MVP First (apenas US1)

1. Phase 1: Setup → 2. Phase 2: Foundational (crítico) → 3. Phase 3: US1.
4. **PARAR e VALIDAR**: testar US1 isoladamente (montar regra → preview correto).
5. Demonstrar o MVP.

### Incremental Delivery

1. Setup + Foundational → base pronta.
2. + US1 → MVP (montagem visual + preview).
3. + US2 → exportação (clipboard/.txt/.sh).
4. + US3 → persistência (JSON + Novo Projeto).

---

## Estado atual (resumo)

- **Concluído**: Setup completo, todo o Foundational, **US1 completa (T013–T022)**, **US2 completa (T023–T025)**, **US3 completa (T026–T029)**, revisão de código (T030). Testes: **10 unitários (1 skip POSIX no Windows) + 3 integração passando**; GUI inicia sem erros.
- **Pendente**: T031 (aceite manual cross-OS — Windows + Linux).

## Notes

- `[P]` = arquivos diferentes, sem dependências.
- A UI (US1) é o maior bloco pendente; export (US2) e persistência (US3) dependem dela apenas para os botões, mas a lógica de serviço é testável isoladamente.
- Commitar após cada tarefa ou grupo lógico.
