package br.com.ifirewall.infra.export;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

/**
 * Copia o script gerado para a área de transferência do sistema.
 * Usa a API de Clipboard do JavaFX (deve ser chamado na thread da aplicação).
 */
public class ClipboardService {

    public void copy(String content) {
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(content == null ? "" : content);
        Clipboard.getSystemClipboard().setContent(clipboardContent);
    }
}
