# Data Model: Iptables Visual Builder

Entidades do domínio (pacote `br.com.ifirewall.core.model`) e o DTO de
persistência (`br.com.ifirewall.infra.persistence`). Todas as entidades de
domínio são imutáveis (`enum`/`record`), agnósticas à UI e ao formato de saída.

## Enums

| Tipo | Valores |
|------|---------|
| `Chain` | `INPUT`, `OUTPUT`, `FORWARD` |
| `Protocol` | `TCP`, `UDP`, `ICMP` |
| `Action` | `ACCEPT`, `DROP`, `REJECT` |

## Value Objects (records)

### `Port`
- **Campos**: `int value`
- **Validação**: `0 <= value <= 65535` (senão `IllegalArgumentException`).

### `IPAddress`
- **Campos**: `String value`
- **Validação**: deve casar com o padrão IPv4 (`0-255` por octeto); `null` ou
  formato inválido → `IllegalArgumentException`.

## Agregado

### `FirewallRule`
Representa uma regra completa.

| Campo | Tipo | Obrigatório | Observação |
|-------|------|-------------|------------|
| `chain` | `Chain` | sim | `Objects.requireNonNull` |
| `protocol` | `Protocol` | não | `null` ⇒ regra sem `-p` |
| `sourceIp` | `IPAddress` | não | mapeia para `-s` |
| `destinationIp` | `IPAddress` | não | mapeia para `-d` |
| `port` | `Port` | não | requer protocolo TCP/UDP (regra de negócio) |
| `action` | `Action` | sim | `Objects.requireNonNull` |

**Regras de integridade** (`validateRuleIntegrity`):
- `chain` e `action` são obrigatórios.
- `port` só faz sentido com `protocol` ∈ {TCP, UDP}.

## DTO de Persistência

### `ProjectState`
Estado serializável do canvas (ver [contracts/project-file-contract.md](contracts/project-file-contract.md)).

| Campo | Tipo | Observação |
|-------|------|------------|
| `rules` | `List<FirewallRule>` | regras compostas no canvas |
| `nodes` | `List<NodeLayout>` | posição visual dos blocos (`id`, `type`, `value`, `x`, `y`) |
| `version` | `String` | versão do formato do arquivo (ex.: `"1.0"`) |

> O domínio (`FirewallRule` etc.) permanece independente do DTO; a serialização
> vive em `infra`, preservando o SRP.
