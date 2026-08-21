/**
 * Classe responsável pela leitura de instâncias do problema Sudoku
 */

package br.edu.sudoku.io;

import br.edu.sudoku.model.SudokuBoard;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SudokuReader {

    public static SudokuBoard read(String path) throws Exception {

    InputStream input = SudokuReader.class
            .getClassLoader()
            .getResourceAsStream(path);

    if (input == null) {
        throw new IllegalArgumentException(
                "Arquivo de Sudoku não encontrado nos recursos: " + path);
    }

    try (BufferedReader reader =
                 new BufferedReader(new InputStreamReader(input))) {

        String primeiraLinha = reader.readLine();

        if (primeiraLinha == null || primeiraLinha.trim().isEmpty()) {
            throw new IllegalArgumentException("O arquivo de Sudoku está vazio.");
        }

        String[] primeirosValores = primeiraLinha.trim().split("\\s+");

        int size = primeirosValores.length;
        int[][] board = new int[size][size];

        for (int j = 0; j < size; j++) {
            board[0][j] = Integer.parseInt(primeirosValores[j]);
        }

        for (int i = 1; i < size; i++) {

            String linha = reader.readLine();

            if (linha == null || linha.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "O arquivo não possui linhas suficientes.");
            }

            String[] valores = linha.trim().split("\\s+");

            if (valores.length != size) {
                throw new IllegalArgumentException(
                        "Todas as linhas devem possuir " + size + " valores.");
            }

            for (int j = 0; j < size; j++) {
                board[i][j] = Integer.parseInt(valores[j]);
            }
        }

        return new SudokuBoard(board);
    }
}}