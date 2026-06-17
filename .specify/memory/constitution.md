# IFirewall Constitution

## Core Principles

### I. Clean Code por Padrão
O código deve ser escrito prioritariamente para seres humanos. Funções devem ser pequenas e ter apenas uma responsabilidade. Nomes de variáveis, classes e métodos devem ser descritivos, evitando comentários óbvios. O código deve ser autoexplicativo ("Simple over Complex").

### II. Arquitetura S.O.L.I.D.
O desenvolvimento deve seguir os princípios S.O.L.I.D. para garantir manutenibilidade e escalabilidade:
- Single Responsibility (Responsabilidade Única).
- Open/Closed (Aberto para extensão, fechado para modificação).
- Liskov Substitution (Substituição de subtipos).
- Interface Segregation (Segregação de interfaces).
- Dependency Inversion (Inversão de dependência através de abstrações).

### III. Simplicidade e Foco no Usuário
O produto final deve ser de fácil utilização. Interfaces (CLI ou API) devem ser intuitivas, com mensagens de erro claras e documentação acessível. "Não me faça pensar": o fluxo principal de uso deve ser óbvio e requerer o mínimo de configuração manual possível.

### IV. Testabilidade e Qualidade
Nenhum código entra em produção sem testes automatizados. Testes unitários devem cobrir a lógica de negócio, enquanto testes de integração garantem que os componentes S.O.L.I.D. colaborem corretamente conforme o esperado.

### V. Evolução Constante e Refatoração
Refatoração não é uma tarefa à parte, mas parte do fluxo de desenvolvimento. Siga a "Regra do Escoteiro": deixe o código sempre um pouco mais limpo do que você o encontrou.

## Padrões de Desenvolvimento

- **DRY (Don't Repeat Yourself)**: Evitar duplicação lógica.
- **YAGNI (You Ain't Gonna Need It)**: Implementar apenas o necessário para os requisitos atuais.
- **KISS (Keep It Simple, Stupid)**: Priorizar a solução mais simples que resolve o problema.

## Governança
Esta constituição é a autoridade máxima sobre as decisões técnicas. Qualquer desvio dos princípios S.O.L.I.D. ou de Clean Code deve ser tecnicamente justificado em revisões de código. Mudanças nesta constituição exigem revisão e aprovação coletiva.

**Version**: 1.0.0 | **Ratified**: 2024-06-17 | **Last Amended**: 2024-06-17
