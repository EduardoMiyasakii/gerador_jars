package br.com.exemplo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    public static List<String> lerRepositorios(String caminhoArquivo) throws IOException {
        List<String> repositorios = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String cabecalho = br.readLine();
            if (cabecalho == null) {
                throw new IOException("Arquivo CSV vazio!");
            }

            String[] colunas = cabecalho.split(",");
            int idxRepositorio = -1;

            // Descobre qual coluna é "repositorio"
            for (int i = 0; i < colunas.length; i++) {
                if (colunas[i].trim().equalsIgnoreCase("repositorio")) {
                    idxRepositorio = i;
                    break;
                }
            }

            if (idxRepositorio == -1) {
                throw new IOException("Coluna 'repositorio' não encontrada no CSV!");
            }

            // Lê as linhas restantes
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] campos = linha.split(",");
                if (campos.length > idxRepositorio) {
                    repositorios.add(campos[idxRepositorio].trim());
                }
            }
        }
        return repositorios;
    }
}
