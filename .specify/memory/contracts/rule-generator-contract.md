# Contract: RuleGenerator (Motor de Geração de Script)

Contrato do caso de uso que traduz o domínio em texto `iptables`.
Pacote: `br.com.ifirewall.core.service`.

## Interface

```java
public interface RuleGenerator {
    String generate(List<FirewallRule> rules);
}
```

- **Entrada**: lista (possivelmente vazia ou `null`) de `FirewallRule`.
- **Saída**: `String` com o script completo (cabeçalho + regras).
- **Implementação**: `RuleGeneratorService` (deve `implements RuleGenerator` — ver D4 em research.md).

## Formato do script gerado

**Cabeçalho (sempre presente):**
1. Shebang `#!/bin/bash` + comentário de identificação.
2. Flush: `iptables -F`, `-X`, `-Z`.
3. Políticas padrão DROP: `iptables -P INPUT DROP` / `FORWARD` / `OUTPUT`.
4. Loopback: `iptables -A INPUT -i lo -j ACCEPT` / `OUTPUT -o lo`.
5. Comentário `# Regras Customizadas`.

**Linha de regra** (ordem fixa dos componentes):
```
iptables -A <CHAIN> [-p <proto>] [-s <srcIp>] [-d <dstIp>] [--dport <port>] -j <ACTION>
```

| Componente | Condição | Exemplo |
|------------|----------|---------|
| `-p <proto>` | `protocol != null` | `-p tcp` (minúsculo) |
| `-s <srcIp>` | `sourceIp != null` | `-s 192.168.1.10` |
| `-d <dstIp>` | `destinationIp != null` | `-d 8.8.8.8` |
| `--dport <port>` | `port != null` | `--dport 22` |

**Lista vazia/`null`**: emite `# Nenhuma regra customizada foi definida.`

## Exemplos (casos de teste)

| Regra | Saída esperada |
|-------|----------------|
| INPUT, TCP, :22, ACCEPT | `iptables -A INPUT -p tcp --dport 22 -j ACCEPT` |
| INPUT, UDP, :53, ACCEPT | `iptables -A INPUT -p udp --dport 53 -j ACCEPT` |
| INPUT, ICMP, ACCEPT | `iptables -A INPUT -p icmp -j ACCEPT` |
| FORWARD, TCP, s=192.168.1.10, d=8.8.8.8, :443, ACCEPT | `iptables -A FORWARD -p tcp -s 192.168.1.10 -d 8.8.8.8 --dport 443 -j ACCEPT` |
| INPUT, s=10.0.0.5, DROP | `iptables -A INPUT -s 10.0.0.5 -j DROP` |
