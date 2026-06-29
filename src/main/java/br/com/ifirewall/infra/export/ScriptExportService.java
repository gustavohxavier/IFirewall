package br.com.ifirewall.infra.export;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

/**
 * Grava o script como arquivo executável (`.sh`).
 *
 * <p>Em sistemas POSIX (Linux/macOS) aplica permissão de execução; no Windows a
 * permissão POSIX não se aplica e é silenciosamente ignorada (ver D2 em research.md).</p>
 */
public class ScriptExportService {

    public void writeScript(String content, Path target) throws IOException {
        Files.writeString(target, content == null ? "" : content, StandardCharsets.UTF_8);
        makeExecutableIfPosix(target);
    }

    private void makeExecutableIfPosix(Path target) throws IOException {
        boolean posix = FileSystems.getDefault().supportedFileAttributeViews().contains("posix");
        if (!posix) {
            return;
        }
        Set<PosixFilePermission> perms = Files.getPosixFilePermissions(target);
        perms.add(PosixFilePermission.OWNER_EXECUTE);
        perms.add(PosixFilePermission.GROUP_EXECUTE);
        perms.add(PosixFilePermission.OTHERS_EXECUTE);
        Files.setPosixFilePermissions(target, perms);
    }
}
