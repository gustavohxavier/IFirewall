# Contract: Arquivo de Projeto (Persistência JSON)

Formato do arquivo salvo/carregado pelo `ProjectPersistenceService`
(pacote `br.com.ifirewall.infra.persistence`). Serializa o `ProjectState`.

## Esquema

```json
{
  "version": "1.0",
  "rules": [
    {
      "chain": "INPUT",
      "protocol": "TCP",
      "sourceIp": null,
      "destinationIp": null,
      "port": 22,
      "action": "ACCEPT"
    }
  ],
  "nodes": [
    { "id": "n1", "type": "CHAIN", "value": "INPUT", "x": 120.0, "y": 80.0 }
  ]
}
```

## Regras do contrato

- `version`: string do formato; usada para migração futura.
- `rules[]`: cada item corresponde a um `FirewallRule`. `protocol`, `sourceIp`,
  `destinationIp`, `port` podem ser `null`. `chain` e `action` são obrigatórios.
- `nodes[]`: layout visual dos blocos no canvas (`type` ∈ CHAIN/PROTOCOL/ACTION/
  IP/PORT). Permite reconstruir a tela ao recarregar.
- **Load**: valores inválidos (porta fora de faixa, IP malformado, chain/action
  ausentes) devem falhar com mensagem clara — não carregar estado corrompido.
- **Save**: serializa o estado atual do canvas; sobrescreve o arquivo escolhido
  no `FileChooser`.
- **Novo Projeto**: não envolve arquivo — apenas limpa `rules` e `nodes` em memória.
