package br.com.exemplo;

import java.io.File;

public class AntUtils {

    public static void gerarJar(File pastaDestino, String repoUrl) {
        try {
            String nomeRepo = repoUrl.substring(repoUrl.lastIndexOf("/") + 1).replace(".git", "");
            File buildFile = new File(pastaDestino, nomeRepo + "/empacotamento/ant/build.xml");

            if (!buildFile.exists()) {
                System.out.println("Nenhum build.xml encontrado em " + buildFile.getAbsolutePath());
                return;
            }

            System.out.println("Build file: " + buildFile.getAbsolutePath());

            ProcessBuilder builder = criarProcessBuilder(buildFile);
            builder.directory(buildFile.getParentFile());
            builder.inheritIO();

            Process process = builder.start();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException("Falha ao gerar JAR do projeto: " + nomeRepo);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static ProcessBuilder criarProcessBuilder(File buildFile) {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            // Windows: usa cmd /c ant (assume que ant está no PATH)
            return new ProcessBuilder(
                    "cmd.exe", "/c", "ant",
                    "-f", buildFile.getAbsolutePath()
            );
        } else {
            // Linux/macOS: chama ant direto
            return new ProcessBuilder(
                    "ant",
                    "-f", buildFile.getAbsolutePath()
            );
        }
    }
}
