package br.com.ifirewall.ui.component;

import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

/**
 * A reusable, styled Label that can be dragged.
 * It carries its type and value for identification on drop.
 */
public class DraggableNode extends Label {

    public static final DataFormat DRAGGABLE_NODE_FORMAT = new DataFormat("br.com.ifirewall.ui.DraggableNode");

    private final String type;
    private final String value;

    public DraggableNode(String text, String type, String value) {
        super(text);
        this.type = type;
        this.value = value;

        setStyle("-fx-border-color: #b0b0b0; -fx-border-width: 1; -fx-background-color: #ffffff; -fx-padding: 8px; -fx-background-radius: 4; -fx-border-radius: 4;");
        setPrefWidth(180);

        setOnDragDetected(event -> {
            Dragboard db = startDragAndDrop(TransferMode.COPY);
            ClipboardContent content = new ClipboardContent();
            content.put(DRAGGABLE_NODE_FORMAT, this.type + ":" + this.value);
            db.setContent(content);
            event.consume();
        });
    }
}