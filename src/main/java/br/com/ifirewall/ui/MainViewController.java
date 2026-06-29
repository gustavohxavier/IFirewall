package br.com.ifirewall.ui;

import br.com.ifirewall.core.model.Action;
import br.com.ifirewall.core.model.Chain;
import br.com.ifirewall.core.model.IPAddress;
import br.com.ifirewall.core.model.Port;
import br.com.ifirewall.core.model.Protocol;
import br.com.ifirewall.core.service.RuleGenerator;
import br.com.ifirewall.core.service.RuleGeneratorService;
import br.com.ifirewall.infra.export.ClipboardService;
import br.com.ifirewall.infra.export.FileExportService;
import br.com.ifirewall.infra.export.ScriptExportService;
import br.com.ifirewall.infra.persistence.ProjectPersistenceService;
import br.com.ifirewall.infra.persistence.ProjectState;
import br.com.ifirewall.ui.component.DraggableNode;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;

/**
 * Controlador da janela principal: liga a interação visual (drag & drop) ao
 * núcleo de geração de regras através da abstração {@link RuleGenerator} (DIP).
 */
public class MainViewController {

    @FXML private VBox toolbox;
    @FXML private Pane canvas;
    @FXML private TextArea livePreviewTextArea;
    @FXML private TextField portField;
    @FXML private TextField sourceIpField;
    @FXML private Label draftLabel;

    // DIP: a UI depende da abstração, não do concreto.
    private final RuleGenerator ruleGenerator = new RuleGeneratorService();
    private final CanvasModel canvasModel = new CanvasModel(ruleGenerator);
    private final RuleDraft draft = new RuleDraft();

    private final ClipboardService clipboardService = new ClipboardService();
    private final FileExportService fileExportService = new FileExportService();
    private final ScriptExportService scriptExportService = new ScriptExportService();
    private final ProjectPersistenceService persistenceService = new ProjectPersistenceService();

    @FXML
    public void initialize() {
        populateToolbox();
        configureCanvasDropTarget();
        refreshDraftLabel();
        updateLivePreview();
    }

    private void populateToolbox() {
        toolbox.getChildren().add(new Label("Chains"));
        for (Chain c : Chain.values()) {
            toolbox.getChildren().add(new DraggableNode(c.name(), "CHAIN", c.name()));
        }
        toolbox.getChildren().add(new Label("Protocolos"));
        for (Protocol p : Protocol.values()) {
            toolbox.getChildren().add(new DraggableNode(p.name(), "PROTOCOL", p.name()));
        }
        toolbox.getChildren().add(new Label("Ações"));
        for (Action a : Action.values()) {
            toolbox.getChildren().add(new DraggableNode(a.name(), "ACTION", a.name()));
        }
    }

    private void configureCanvasDropTarget() {
        canvas.setOnDragOver(event -> {
            if (event.getDragboard().hasContent(DraggableNode.DRAGGABLE_NODE_FORMAT)) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });
        canvas.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean accepted = false;
            if (db.hasContent(DraggableNode.DRAGGABLE_NODE_FORMAT)) {
                accepted = applyDroppedBlock((String) db.getContent(DraggableNode.DRAGGABLE_NODE_FORMAT));
            }
            event.setDropCompleted(accepted);
            event.consume();
        });
    }

    /** Aplica um bloco "TIPO:VALOR" solto no canvas ao rascunho da regra. */
    private boolean applyDroppedBlock(String payload) {
        String[] parts = payload.split(":", 2);
        if (parts.length != 2) {
            return false;
        }
        String type = parts[0];
        String value = parts[1];
        switch (type) {
            case "CHAIN" -> draft.setChain(Chain.valueOf(value));
            case "PROTOCOL" -> draft.setProtocol(Protocol.valueOf(value));
            case "ACTION" -> draft.setAction(Action.valueOf(value));
            default -> {
                return false;
            }
        }
        refreshDraftLabel();
        return true;
    }

    /** Lê os campos de porta/IP, valida e adiciona a regra ao canvas. */
    @FXML
    public void onAddRule() {
        try {
            applyOptionalFields();
            canvasModel.addRule(draft.build());
            draft.reset();
            portField.clear();
            sourceIpField.clear();
            refreshDraftLabel();
            updateLivePreview();
        } catch (IllegalStateException | IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void applyOptionalFields() {
        String portText = portField.getText();
        if (portText != null && !portText.isBlank()) {
            draft.setPort(new Port(Integer.parseInt(portText.trim())));
        }
        String ipText = sourceIpField.getText();
        if (ipText != null && !ipText.isBlank()) {
            draft.setSourceIp(new IPAddress(ipText.trim()));
        }
    }

    @FXML
    public void onClear() {
        canvasModel.clear();
        draft.reset();
        portField.clear();
        sourceIpField.clear();
        refreshDraftLabel();
        updateLivePreview();
    }

    // ----- Exportação (US2) -----

    @FXML
    public void onCopyScript() {
        clipboardService.copy(canvasModel.currentScript());
        showInfo("Script copiado para a área de transferência.");
    }

    @FXML
    public void onExportTxt() {
        File target = chooseSaveFile("Salvar script (.txt)", "firewall.txt", "Texto", "*.txt");
        if (target == null) {
            return;
        }
        try {
            fileExportService.writeText(canvasModel.currentScript(), target.toPath());
            showInfo("Arquivo .txt gerado: " + target.getName());
        } catch (IOException e) {
            showError("Falha ao gravar o arquivo: " + e.getMessage());
        }
    }

    @FXML
    public void onExportSh() {
        File target = chooseSaveFile("Salvar script (.sh)", "firewall.sh", "Shell script", "*.sh");
        if (target == null) {
            return;
        }
        try {
            scriptExportService.writeScript(canvasModel.currentScript(), target.toPath());
            showInfo("Arquivo .sh gerado: " + target.getName());
        } catch (IOException e) {
            showError("Falha ao gravar o arquivo: " + e.getMessage());
        }
    }

    // ----- Persistência (US3) -----

    @FXML
    public void onSaveProject() {
        File target = chooseSaveFile("Salvar projeto", "projeto.json", "Projeto JSON", "*.json");
        if (target == null) {
            return;
        }
        try {
            persistenceService.save(ProjectState.of(canvasModel.rules()), target.toPath());
            showInfo("Projeto salvo: " + target.getName());
        } catch (IOException e) {
            showError("Falha ao salvar o projeto: " + e.getMessage());
        }
    }

    @FXML
    public void onOpenProject() {
        File source = chooseOpenFile("Abrir projeto", "Projeto JSON", "*.json");
        if (source == null) {
            return;
        }
        try {
            ProjectState state = persistenceService.load(source.toPath());
            canvasModel.replaceAll(state.rules());
            draft.reset();
            refreshDraftLabel();
            updateLivePreview();
            showInfo("Projeto carregado: " + source.getName());
        } catch (IOException | IllegalArgumentException e) {
            showError("Falha ao abrir o projeto: " + e.getMessage());
        }
    }

    private File chooseSaveFile(String title, String initialName, String filterDesc, String ext) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(title);
        chooser.setInitialFileName(initialName);
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(filterDesc, ext));
        return chooser.showSaveDialog(ownerWindow());
    }

    private File chooseOpenFile(String title, String filterDesc, String ext) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(title);
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(filterDesc, ext));
        return chooser.showOpenDialog(ownerWindow());
    }

    private Window ownerWindow() {
        return canvas.getScene() == null ? null : canvas.getScene().getWindow();
    }

    private void refreshDraftLabel() {
        draftLabel.setText(draft.describe());
    }

    private void updateLivePreview() {
        livePreviewTextArea.setText(canvasModel.currentScript());
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atenção");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
