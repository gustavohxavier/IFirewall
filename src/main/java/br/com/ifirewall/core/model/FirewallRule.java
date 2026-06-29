package br.com.ifirewall.core.model;

import java.util.Objects;

/**
 * Represents a complete, single firewall rule.
 * This record is immutable and aggregates all components of a rule.
 *
 * @param chain The target chain (e.g., INPUT, OUTPUT). Must not be null.
 * @param protocol The protocol (e.g., TCP, UDP). Can be null.
 * @param sourceIp The source IP address. Can be null.
 * @param destinationIp The destination IP address. Can be null.
 * @param port The destination port. Can be null.
 * @param action The action to take (e.g., ACCEPT, DROP). Must not be null.
 */
public record FirewallRule(
    Chain chain,
    Protocol protocol,
    IPAddress sourceIp,
    IPAddress destinationIp,
    Port port,
    Action action
) {
    public FirewallRule {
        Objects.requireNonNull(chain, "Chain cannot be null");
        Objects.requireNonNull(action, "Action cannot be null");
        if (port != null && protocol != Protocol.TCP && protocol != Protocol.UDP) {
            throw new IllegalArgumentException("Uma porta exige o protocolo TCP ou UDP.");
        }
    }
}