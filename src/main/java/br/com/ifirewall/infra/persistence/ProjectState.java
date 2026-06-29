package br.com.ifirewall.infra.persistence;

import br.com.ifirewall.core.model.FirewallRule;

import java.util.List;

/**
 * Estado serializável de um projeto (conteúdo do arquivo `.json`).
 *
 * <p>Persiste as regras compostas no canvas. O layout visual dos blocos
 * (posições) fica como evolução futura — o MVP compõe uma regra por vez via
 * slots, sem grafo de nós (ver contracts/project-file-contract.md).</p>
 */
public record ProjectState(String version, List<FirewallRule> rules) {

    public static final String CURRENT_VERSION = "1.0";

    public static ProjectState of(List<FirewallRule> rules) {
        return new ProjectState(CURRENT_VERSION, List.copyOf(rules));
    }
}
