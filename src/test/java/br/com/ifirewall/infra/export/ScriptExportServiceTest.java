package br.com.ifirewall.infra.export;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class ScriptExportServiceTest {

    private final ScriptExportService service = new ScriptExportService();

    @Test
    void writesScriptContent(@TempDir Path dir) throws IOException {
        Path target = dir.resolve("firewall.sh");
        String content = "#!/bin/bash\niptables -F\n";

        service.writeScript(content, target);

        assertEquals(content, Files.readString(target));
    }

    @Test
    void setsExecutablePermissionOnPosix(@TempDir Path dir) throws IOException {
        assumeTrue(FileSystems.getDefault().supportedFileAttributeViews().contains("posix"),
                "Permissão POSIX só se aplica em sistemas POSIX (Linux/macOS).");

        Path target = dir.resolve("firewall.sh");
        service.writeScript("#!/bin/bash\n", target);

        assertTrue(Files.getPosixFilePermissions(target).contains(PosixFilePermission.OWNER_EXECUTE),
                "O arquivo .sh deve ter permissão de execução para o dono.");
    }
}
