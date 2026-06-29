package br.com.ifirewall.infra.export;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileExportServiceTest {

    private final FileExportService service = new FileExportService();

    @Test
    void writesContentToTxtFile(@TempDir Path dir) throws IOException {
        Path target = dir.resolve("firewall.txt");
        String content = "iptables -A INPUT -p tcp --dport 22 -j ACCEPT\n";

        service.writeText(content, target);

        assertEquals(content, Files.readString(target));
    }
}
