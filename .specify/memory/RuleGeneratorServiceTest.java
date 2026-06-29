package br.com.ifirewall.core.service;

import br.com.ifirewall.core.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RuleGeneratorServiceTest {

    private RuleGeneratorService ruleGeneratorService;

    @BeforeEach
    void setUp() {
        ruleGeneratorService = new RuleGeneratorService();
    }

    @Test
    @DisplayName("TSK008: Should generate a correct TCP rule with port")
    void testGenerateTcpRule() {
        FirewallRule rule = new FirewallRule(Chain.INPUT, Protocol.TCP, null, null, new Port(22), Action.ACCEPT);
        String script = ruleGeneratorService.generate(Collections.singletonList(rule));
        String expectedRule = "iptables -A INPUT -p tcp --dport 22 -j ACCEPT";
        assertTrue(script.contains(expectedRule), "Script should contain the correct TCP rule.");
    }

    @Test
    @DisplayName("TSK009: Should generate a correct UDP rule with port")
    void testGenerateUdpRule() {
        FirewallRule rule = new FirewallRule(Chain.INPUT, Protocol.UDP, null, null, new Port(53), Action.ACCEPT);
        String script = ruleGeneratorService.generate(Collections.singletonList(rule));
        String expectedRule = "iptables -A INPUT -p udp --dport 53 -j ACCEPT";
        assertTrue(script.contains(expectedRule), "Script should contain the correct UDP rule.");
    }

    @Test
    @DisplayName("TSK010: Should generate a correct ICMP rule")
    void testGenerateIcmpRule() {
        FirewallRule rule = new FirewallRule(Chain.INPUT, Protocol.ICMP, null, null, null, Action.ACCEPT);
        String script = ruleGeneratorService.generate(Collections.singletonList(rule));
        String expectedRule = "iptables -A INPUT -p icmp -j ACCEPT";
        assertTrue(script.contains(expectedRule), "Script should contain the correct ICMP rule.");
    }

    @Test
    @DisplayName("Should generate a rule with source and destination IP")
    void testGenerateRuleWithIp() {
        FirewallRule rule = new FirewallRule(
                Chain.FORWARD,
                Protocol.TCP,
                new IPAddress("192.168.1.10"),
                new IPAddress("8.8.8.8"),
                new Port(443),
                Action.ACCEPT
        );
        String script = ruleGeneratorService.generate(Collections.singletonList(rule));
        String expectedRule = "iptables -A FORWARD -p tcp -s 192.168.1.10 -d 8.8.8.8 --dport 443 -j ACCEPT";
        assertTrue(script.contains(expectedRule), "Script should contain the rule with source and destination IPs.");
    }

    @Test
    @DisplayName("TSK011: Should generate a complete script with header and multiple rules")
    void testGenerateCompleteScript() {
        FirewallRule rule1 = new FirewallRule(Chain.INPUT, Protocol.TCP, null, null, new Port(80), Action.ACCEPT);
        FirewallRule rule2 = new FirewallRule(Chain.OUTPUT, Protocol.UDP, null, null, new Port(123), Action.ACCEPT);
        FirewallRule rule3 = new FirewallRule(Chain.INPUT, null, new IPAddress("10.0.0.5"), null, null, Action.DROP);

        String script = ruleGeneratorService.generate(List.of(rule1, rule2, rule3));

        // Check header
        assertTrue(script.startsWith("#!/bin/bash"), "Script should start with shebang.");
        assertTrue(script.contains("iptables -P INPUT DROP"), "Script should contain default drop policy.");
        assertTrue(script.contains("iptables -A INPUT -i lo -j ACCEPT"), "Script should allow loopback traffic.");

        // Check rules
        assertTrue(script.contains("iptables -A INPUT -p tcp --dport 80 -j ACCEPT"), "Script should contain TCP rule.");
        assertTrue(script.contains("iptables -A OUTPUT -p udp --dport 123 -j ACCEPT"), "Script should contain UDP rule.");
        assertTrue(script.contains("iptables -A INPUT -s 10.0.0.5 -j DROP"), "Script should contain IP-based DROP rule.");
    }

    @Test
    @DisplayName("Should generate a script with no custom rules")
    void testGenerateScriptWithNoRules() {
        String script = ruleGeneratorService.generate(Collections.emptyList());
        assertTrue(script.contains("# Nenhuma regra customizada foi definida."), "Script should contain placeholder for no rules.");
    }
}