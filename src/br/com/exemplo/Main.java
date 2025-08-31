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
        JFrame frame = new JFrame("Clonar Repositórios do GitHub");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 200);
        frame.setLayout(new FlowLayout());

        JButton btnSelecionar = new JButton("Selecionar CSV e Pasta de Destino");
        btnSelecionar.addActionListener(e -> {
            try {
                // Escolher arquivo CSV
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Escolha o arquivo CSV");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                if (fileChooser.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(frame, "Nenhum arquivo CSV selecionado.");
                    return;
                }
                File arquivoCSV = fileChooser.getSelectedFile();

                // Escolher pasta de destino
                JFileChooser dirChooser = new JFileChooser();
                dirChooser.setDialogTitle("Escolha a pasta de destino para clonar os repositórios");
                dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                if (dirChooser.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(frame, "Nenhuma pasta selecionada.");
                    return;
                }
                File pastaDestino = dirChooser.getSelectedFile();

                // Processar CSV
                List<String> repositorios = CSVReader.lerRepositorios(arquivoCSV.getAbsolutePath());
                if (repositorios.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Nenhum repositório encontrado no CSV.");
                    return;
                }

                for (String repo : repositorios) {
                    GitUtils.clonarRepositorio(repo, pastaDestino);
                    AntUtils.gerarJar(pastaDestino, repo);
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
