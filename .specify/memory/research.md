# Research: Iptables Visual Builder

Consolida as decisões técnicas do plano. Não há itens `NEEDS CLARIFICATION` em
aberto — a stack foi fixada nas *Clarifications* da spec (Sessão 2026-06-17).

## Technical Context

| Item | Escolha |
|------|---------|
| Linguagem | Java 17 (compilação `source/target 17`; ambiente de dev usa JDK 25) |
| Build | Maven (wrapper 3.8.5) |
| UI | JavaFX 17.0.2 (`javafx-controls`, `javafx-fxml`) |
| Testes | JUnit 5 (Jupiter) + Mockito; `maven-surefire-plugin` 3.2.5 |
| Persistência | Arquivos JSON locais |
| Empacotamento | `maven-shade-plugin` (fat JAR) + classe `Launcher`; `javafx-maven-plugin` 0.0.8 para `mvn javafx:run` |
| Arquitetura | Clean Architecture: pacotes `core` (domínio/casos de uso), `infra` (drivers), `ui` |

## Decisões

### D1 — Biblioteca de serialização JSON
- **Decisão**: Jackson (`jackson-databind`).
- **Rationale**: Suporte nativo a `record`s Java, API estável, amplamente usado.
- **Alternativas**: Gson (mais simples, porém suporte a `record` mais limitado);
  serialização manual (descartada — viola DRY/KISS).

### D2 — Exportação `.sh` com permissão de execução
- **Decisão**: gravar via `java.nio.file` e, quando o SO for POSIX
  (`FileSystems.getDefault().supportedFileAttributeViews().contains("posix")`),
  aplicar permissão de execução; em Windows, apenas gravar o arquivo.
- **Rationale**: o critério de aceite exige um `.sh` executável em Linux; em
  Windows a permissão POSIX não se aplica e deve ser ignorada sem erro.
- **Alternativas**: sempre tentar `setPosixFilePermissions` (descartada — lança
  `UnsupportedOperationException` no Windows).

### D3 — Execução e portabilidade do JAR
- **Decisão**: main-class do shade aponta para `Launcher` (classe que **não**
  estende `Application`) que chama `Application.launch(App.class, args)`.
- **Rationale**: evita o erro "JavaFX runtime components are missing" ao rodar o
  fat JAR. Para dev, `mvn javafx:run` resolve os módulos/nativos automaticamente.
- **Alternativas**: `jlink`/`jpackage` para distribuível por SO — fora do escopo
  do MVP; anotado como evolução futura para portabilidade total do binário.

### D4 — DIP: dependência da UI sobre abstração de regra
- **Decisão**: `RuleGeneratorService` deve implementar a interface `RuleGenerator`
  e a UI (`MainViewController`) deve depender de `RuleGenerator`, não do concreto.
- **Rationale**: Princípio II da Constituição (Dependency Inversion) e extensão
  futura a novos formatos sem alterar o núcleo.
- **Estado**: a interface já existe; a ligação ainda usa o tipo concreto —
  pendência de refatoração registrada no plano.

### D5 — Escopo do MVP (YAGNI)
- **Decisão**: Undo/Redo e destaque de sintaxe do Live Preview ficam fora do MVP.
- **Rationale**: Princípio YAGNI/KISS; não são critérios de aceite.
- **Alternativas**: implementar agora (descartado — sem requisito de aceite).
