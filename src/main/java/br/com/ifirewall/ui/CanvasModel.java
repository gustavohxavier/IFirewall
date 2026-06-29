package br.com.ifirewall.ui;

import br.com.ifirewall.core.model.FirewallRule;
import br.com.ifirewall.core.service.RuleGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Estado do canvas, independente do JavaFX.
 *
 * <p>Mantém a lista de regras compostas e delega a geração do script ao
 * {@link RuleGenerator} (abstração — Princípio DIP). Por não depender da UI,
 * pode ser testado em integração com o núcleo sem inicializar a toolkit gráfica.</p>
 */
public class CanvasModel {

    private final RuleGenerator ruleGenerator;
    private final List<FirewallRule> rules = new ArrayList<>();

    public CanvasModel(RuleGenerator ruleGenerator) {
        this.ruleGenerator = Objects.requireNonNull(ruleGenerator, "ruleGenerator cannot be null");
    }

    public void addRule(FirewallRule rule) {
        rules.add(Objects.requireNonNull(rule, "rule cannot be null"));
    }

    public void clear() {
        rules.clear();
    }

    /** Substitui todas as regras (usado ao carregar um projeto salvo). */
    public void replaceAll(List<FirewallRule> newRules) {
        Objects.requireNonNull(newRules, "newRules cannot be null");
        rules.clear();
        rules.addAll(newRules);
    }

    public List<FirewallRule> rules() {
        return Collections.unmodifiableList(rules);
    }

    /** Script {@code iptables} correspondente ao estado atual do canvas. */
    public String currentScript() {
        return ruleGenerator.generate(rules);
    }
}
