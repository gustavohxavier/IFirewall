package br.com.ifirewall.infra.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Salva e carrega o {@link ProjectState} em arquivos JSON locais.
 * Apenas IO/serialização — a escolha de caminho (FileChooser) é da camada de UI.
 */
public class ProjectPersistenceService {

    private final ObjectMapper objectMapper;

    public ProjectPersistenceService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void save(ProjectState state, Path target) throws IOException {
        objectMapper.writeValue(target.toFile(), state);
    }

    public ProjectState load(Path source) throws IOException {
        if (!Files.exists(source)) {
            throw new IOException("Arquivo de projeto não encontrado: " + source);
        }
        return objectMapper.readValue(source.toFile(), ProjectState.class);
    }
}
