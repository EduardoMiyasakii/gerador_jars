package br.com.exemplo;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::criarInterface);
    }

    private static void criarInterface() {
        JFrame frame = new JFrame("Clonar Repositórios e Gerar JARs");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 200);
        frame.setLayout(new FlowLayout());

        JButton btnSelecionar = new JButton("Selecionar CSV e Pastas");
        btnSelecionar.addActionListener(e -> {
            try {
                // 1. Escolher arquivo CSV
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Escolha o arquivo CSV");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                if (fileChooser.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(frame, "Nenhum arquivo CSV selecionado.");
                    return;
                }
                File arquivoCSV = fileChooser.getSelectedFile();

                // 2. Escolher pasta para clonar os repositórios
                JFileChooser cloneChooser = new JFileChooser();
                cloneChooser.setDialogTitle("Escolha a pasta para clonar os repositórios");
                cloneChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                if (cloneChooser.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(frame, "Nenhuma pasta selecionada para clonar.");
                    return;
                }
                File pastaClones = cloneChooser.getSelectedFile();

                // 3. Escolher pasta para salvar os JARs
                JFileChooser jarChooser = new JFileChooser();
                jarChooser.setDialogTitle("Escolha a pasta para salvar os JARs");
                jarChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                if (jarChooser.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(frame, "Nenhuma pasta selecionada para os JARs.");
                    return;
                }
                File pastaJars = jarChooser.getSelectedFile();

                // Processar CSV
                List<String> repositorios = CSVReader.lerRepositorios(arquivoCSV.getAbsolutePath());
                if (repositorios.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Nenhum repositório encontrado no CSV.");
                    return;
                }

                for (String repo : repositorios) {
                    // Clonar
                    GitUtils.clonarRepositorio(repo, pastaClones);

                    // Gerar JARs no local escolhido
                    AntUtils.gerarJar(pastaClones, repo, pastaJars);
                }

                JOptionPane.showMessageDialog(frame, "Processo concluído com sucesso!");

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Erro: " + ex.getMessage());
            }
        });

        frame.add(btnSelecionar);
        frame.setVisible(true);
    }
}
