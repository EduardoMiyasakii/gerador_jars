package br.com.exemplo;

import java.io.File;

public class AntUtils {

    public static void gerarJar(File pastaDestino, String repoUrl, File pastaJars) {
        try {
            String nomeRepo = repoUrl.substring(repoUrl.lastIndexOf("/") + 1).replace(".git", "");
            File buildFile = new File(pastaDestino, nomeRepo + "/empacotamento/ant/build.xml");

            if (!buildFile.exists()) {
                System.out.println("Nenhum build.xml encontrado em " + buildFile.getAbsolutePath());
                return;
            }

            System.out.println("Build file: " + buildFile.getAbsolutePath());

            // Passa o diretório de saída como propriedade ANT_OPTS
            ProcessBuilder builder = criarProcessBuilder(buildFile, pastaJars);
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

    private static ProcessBuilder criarProcessBuilder(File buildFile, File pastaJars) {
        String os = System.getProperty("os.name").toLowerCase();
        String outputProp = "-Ddist.dir=" + pastaJars.getAbsolutePath(); // Assume que build.xml usa ${dist.dir}

        if (os.contains("win")) {
            return new ProcessBuilder(
                    "cmd.exe", "/c", "ant",
                    outputProp,
                    "-f", buildFile.getAbsolutePath()
            );
        } else {
            return new ProcessBuilder(
                    "ant",
                    outputProp,
                    "-f", buildFile.getAbsolutePath()
            );
        }
    }
}
