package br.com.exemplo;

import java.io.File;

public class GitUtils {

    public static void clonarRepositorio(String urlRepo, File pastaDestino) throws Exception {
        ProcessBuilder builder = new ProcessBuilder("git", "clone","--branch","feature/ALFANET", urlRepo);
        builder.directory(pastaDestino);
        builder.inheritIO(); // mostra saída no console
        Process process = builder.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("Falha ao clonar: " + urlRepo);
        }
    }
}
