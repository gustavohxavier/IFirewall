package br.com.ifirewall.ui;

import br.com.ifirewall.core.model.Action;
import br.com.ifirewall.core.model.Chain;
import br.com.ifirewall.core.model.Port;
import br.com.ifirewall.core.model.Protocol;
import br.com.ifirewall.core.service.RuleGeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Teste de integração da camada de UI (CanvasModel) com o núcleo real
 * (RuleGeneratorService) — Princípio IV da Constituição.
 * Não inicializa a toolkit JavaFX: valida a colaboração de estado + geração.
 */
class CanvasModelIT {

    private CanvasModel canvasModel;

    @BeforeEach
    void setUp() {
        // Liga o modelo do canvas ao gerador concreto via a abstração RuleGenerator.
        canvasModel = new CanvasModel(new RuleGeneratorService());
    }

    @Test
    @DisplayName("TSK011b: adicionar regra reflete no script gerado (UI -> Core)")
    void addingRuleUpdatesGeneratedScript() {
        canvasModel.addRule(buildSshRule());

        String script = canvasModel.currentScript();

        assertTrue(script.contains("iptables -A INPUT -p tcp --dport 22 -j ACCEPT"),
                "O script deve refletir a regra adicionada no canvas.");
        assertFalse(script.contains("Nenhuma regra customizada"),
                "Com uma regra presente, o placeholder de vazio não deve aparecer.");
    }

    @Test
    @DisplayName("Limpar o canvas volta o preview ao estado vazio")
    void clearingCanvasResetsScript() {
        canvasModel.addRule(buildSshRule());
        canvasModel.clear();

        assertTrue(canvasModel.currentScript().contains("# Nenhuma regra customizada foi definida."),
                "Após limpar, o preview deve indicar ausência de regras customizadas.");
    }

    @Test
    @DisplayName("RuleDraft monta a mesma regra que o núcleo traduz corretamente")
    void draftBuildsRuleConsumedByCore() {
        RuleDraft draft = new RuleDraft();
        draft.setChain(Chain.INPUT);
        draft.setProtocol(Protocol.TCP);
        draft.setPort(new Port(22));
        draft.setAction(Action.ACCEPT);

        canvasModel.addRule(draft.build());

        assertTrue(canvasModel.currentScript().contains("iptables -A INPUT -p tcp --dport 22 -j ACCEPT"));
    }

    private static br.com.ifirewall.core.model.FirewallRule buildSshRule() {
        return new br.com.ifirewall.core.model.FirewallRule(
                Chain.INPUT, Protocol.TCP, null, null, new Port(22), Action.ACCEPT);
    }
}
