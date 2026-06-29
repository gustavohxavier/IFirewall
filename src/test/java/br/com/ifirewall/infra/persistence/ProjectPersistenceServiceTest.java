package br.com.ifirewall.infra.persistence;

import br.com.ifirewall.core.model.Action;
import br.com.ifirewall.core.model.Chain;
import br.com.ifirewall.core.model.FirewallRule;
import br.com.ifirewall.core.model.IPAddress;
import br.com.ifirewall.core.model.Port;
import br.com.ifirewall.core.model.Protocol;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProjectPersistenceServiceTest {

    private final ProjectPersistenceService service = new ProjectPersistenceService();

    @Test
    void saveThenLoadPreservesRules(@TempDir Path dir) throws IOException {
        Path file = dir.resolve("projeto.json");
        List<FirewallRule> rules = List.of(
                new FirewallRule(Chain.INPUT, Protocol.TCP, null, null, new Port(22), Action.ACCEPT),
                new FirewallRule(Chain.FORWARD, null, new IPAddress("10.0.0.5"), null, null, Action.DROP));

        service.save(ProjectState.of(rules), file);
        ProjectState loaded = service.load(file);

        assertEquals(ProjectState.CURRENT_VERSION, loaded.version());
        assertEquals(rules, loaded.rules());
    }
}
