# Quickstart: Iptables Visual Builder

Guia de build, execução e validação. Detalhes de design em
[data-model.md](data-model.md) e [contracts/](contracts/).

## Pré-requisitos

- JDK 17+ (validado com JDK 25) — `JAVA_HOME` apontando para o JDK.
- Maven 3.8+ (o repositório usa o Maven do wrapper em `~/.m2/wrapper`).
- Conexão para baixar dependências JavaFX/JUnit na primeira execução.

## Build e testes

```bash
mvn test       # compila e roda os testes unitários (JUnit 5 via Surefire)
mvn package    # gera o fat JAR em target/iptables-visual-builder-1.0.0.jar
```

Resultado esperado dos testes: `Tests run: 6, Failures: 0, Errors: 0`.

## Executar

```bash
# Forma 1 — desenvolvimento (resolve módulos/nativos do JavaFX automaticamente)
mvn javafx:run

# Forma 2 — fat JAR
java -jar target/iptables-visual-builder-1.0.0.jar
```

> A main-class do JAR é `br.com.ifirewall.ui.Launcher` (evita o erro
> "JavaFX runtime components are missing"). Para um binário distribuível por SO,
> ver D3 em [research.md](research.md) (jpackage/jlink — fora do MVP).

## Cenários de validação (mapeados aos Critérios de Aceite)

| # | Cenário | Resultado esperado |
|---|---------|--------------------|
| 1 | Criar regra nas 3 chains (INPUT/OUTPUT/FORWARD) | Live Preview mostra `iptables -A <CHAIN> ...` para cada uma |
| 2 | Regra TCP :22 ACCEPT em INPUT | `iptables -A INPUT -p tcp --dport 22 -j ACCEPT` no preview |
| 3 | Regras UDP e ICMP | linhas `-p udp --dport ...` e `-p icmp` corretas |
| 4 | Drag & drop dos blocos | sem erros no console; bloco encaixa/alerta conforme conectores |
| 5 | "Gerar .sh" e executar em Linux | arquivo `.sh` válido e executável (`chmod +x` automático em POSIX) |
| 6 | "Gerar .txt" / "Copiar Script" | arquivo `.txt` gerado / script na área de transferência |
| 7 | Salvar e recarregar projeto (JSON) | canvas reconstruído com as mesmas regras e posições |
| 8 | "Novo Projeto" | canvas limpo, preview volta ao cabeçalho padrão |

Os cenários 1–3 são cobertos por testes unitários em
`RuleGeneratorServiceTest`; o fluxo UI↔Core será coberto por teste de integração
(TSK011b).
