package br.com.ifirewall.infra.export;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Grava o script em um arquivo de texto puro (`.txt`).
 * A escolha do caminho (FileChooser) fica a cargo da camada de UI; aqui só há IO,
 * o que mantém o serviço testável e independente do JavaFX (SRP).
 */
public class FileExportService {

    public void writeText(String content, Path target) throws IOException {
        Files.writeString(target, content == null ? "" : content, StandardCharsets.UTF_8);
    }
}
