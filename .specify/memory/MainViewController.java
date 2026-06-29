package br.com.ifirewall.ui;

import br.com.ifirewall.core.model.FirewallRule;
import br.com.ifirewall.core.service.RuleGeneratorService;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.List;

public class MainViewController {

    @FXML
    private TextArea livePreviewTextArea;

    private final RuleGeneratorService ruleGeneratorService = new RuleGeneratorService();
    private final List<FirewallRule> rules = new ArrayList<>();

    @FXML
    public void initialize() {
        // TSK016: Implement Live Preview
        updateLivePreview();
    }

    private void updateLivePreview() {
        // TSK015: Conectar UI ao Core
        String script = ruleGeneratorService.generate(rules);
        livePreviewTextArea.setText(script);
    }
}