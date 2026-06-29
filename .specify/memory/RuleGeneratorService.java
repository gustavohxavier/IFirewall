package br.com.ifirewall.core.service;

import br.com.ifirewall.core.model.FirewallRule;

import java.util.List;
import java.util.stream.Collectors;

public class RuleGeneratorService {

    public String generate(List<FirewallRule> rules) {
        StringBuilder script = new StringBuilder();

        appendHeader(script);
        appendRules(script, rules);

        return script.toString();
    }

    private void appendHeader(StringBuilder script) {
        script.append("#!/bin/bash\n\n");
        script.append("# Script de Firewall Gerado pelo Iptables Visual Builder\n\n");

        script.append("# 1. Limpando todas as regras existentes (Flush)\n");
        script.append("iptables -F\n");
        script.append("iptables -X\n");
        script.append("iptables -Z\n\n");

        script.append("# 2. Definindo políticas padrão (Default DROP)\n");
        script.append("iptables -P INPUT DROP\n");
        script.append("iptables -P FORWARD DROP\n");
        script.append("iptables -P OUTPUT DROP\n\n");

        script.append("# 3. Permitindo tráfego de loopback (essencial para o sistema)\n");
        script.append("iptables -A INPUT -i lo -j ACCEPT\n");
        script.append("iptables -A OUTPUT -o lo -j ACCEPT\n\n");

        script.append("# 4. Regras Customizadas\n");
    }

    private void appendRules(StringBuilder script, List<FirewallRule> rules) {
        if (rules == null || rules.isEmpty()) {
            script.append("# Nenhuma regra customizada foi definida.\n");
            return;
        }
        // A lógica de tradução de cada regra será implementada aqui.
        // Por enquanto, apenas um placeholder.
    }
}