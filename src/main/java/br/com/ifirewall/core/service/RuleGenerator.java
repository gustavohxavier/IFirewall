package br.com.ifirewall.core.service;

import br.com.ifirewall.core.model.FirewallRule;

import java.util.List;

public interface RuleGenerator {
    String generate(List<FirewallRule> rules);
}