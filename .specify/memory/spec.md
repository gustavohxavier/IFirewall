# Spec: Iptables Visual Builder (Drag & Drop)

## Visão Geral
O objetivo desta aplicação é fornecer uma interface visual intuitiva para a criação de scripts de firewall `iptables`. O foco principal é a **filtragem de pacotes**, permitindo que o usuário monte regras complexas sem a necessidade de conhecer profundamente a sintaxe de linha de comando, utilizando uma interface de arrastar e soltar (drag and drop).

## Requisitos Funcionais

### 1. Interface de Construção (Drag & Drop)
- **Toolbox**: Painel lateral contendo os blocos fundamentais de uma regra de firewall (Chains: INPUT, OUTPUT, FORWARD; Protocolos: TCP, UDP, ICMP; Ações: ACCEPT, DROP, REJECT; Campos de IP e Porta).
- **Canvas**: Área central onde o usuário solta os blocos para compor uma regra lógica.
- **Conectores**: Os blocos devem se conectar de forma lógica (ex: uma regra deve obrigatoriamente ter uma Chain e uma Ação).

### 2. Painel de Visualização (Live Preview)
- Exibição em tempo real dos comandos `iptables` correspondentes à configuração visual atual.
- Destaque de sintaxe para facilitar a leitura das regras geradas.

### 3. Gerador de Script
- Botão para exportar a configuração como um arquivo de script bash (`.sh`).
- O script deve incluir:
    - Limpeza de regras existentes (Flush).
    - Definição de políticas padrão (DROP por padrão, recomendado por segurança).
    - Inclusão das regras customizadas criadas pelo usuário.
    - Comentários explicativos em cada bloco de código (Clean Code).

## Requisitos Não-Funcionais e Arquitetura (S.O.L.I.D. & Clean Code)

- **Single Responsibility Principle (SRP)**:
    - O motor de geração de texto (`RuleGenerator`) deve ser isolado dos componentes de UI de arrastar e soltar.
    - Cada componente de interface deve gerenciar apenas seu estado local e notificar mudanças.
- **Dependency Inversion Principle (DIP)**:
    - A interface de construção deve depender de uma abstração de "Regra", permitindo que novos tipos de filtragem sejam adicionados no futuro sem alterar o núcleo do sistema.
- **Clean Code**:
    - Nomes de funções como `addRuleToChain()`, `generateBashScript()`, `validateRuleIntegrity()`.
    - O código gerado no script Bash deve ser legível, com indentação consistente e nomes de variáveis claros.
- **Simplicidade (KISS)**:
    - O usuário não deve ser sobrecarregado com opções obscuras do iptables na interface principal. O foco é filtragem básica e eficiente.

## Experiência do Usuário (UX)
- **Feedback Visual**: Blocos que não podem se conectar devem emitir um alerta visual ou impedir o encaixe (Interface Intuitiva).
- **Undo/Redo**: Possibilidade de desfazer ações de drag and drop.
- **Exportação com Um Clique**: Facilidade total para obter o resultado final.

## Exemplo de Fluxo de Usuário
1. O usuário arrasta o bloco `INPUT` para o canvas.
2. Conecta um bloco de `Protocol: TCP`.
3. Define a porta como `22` (SSH).
4. Conecta a ação `ACCEPT`.
5. O painel lateral mostra instantaneamente: `iptables -A INPUT -p tcp --dport 22 -j ACCEPT`.
6. O usuário clica em "Gerar Firewall" e recebe um arquivo completo e documentado.

## Critérios de Aceite
- [ ] Possibilidade de criar regras para as 3 chains principais.
- [ ] Geração de comandos válidos para TCP, UDP e ICMP.
- [ ] Interface funcional de Drag & Drop sem erros de console.
- [ ] Exportação de arquivo `.sh` funcional que pode ser executado em ambiente Linux.
- [ ] Cobertura de testes unitários para a lógica de tradução de blocos para texto.