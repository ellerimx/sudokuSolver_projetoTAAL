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

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        int[][] board = new int[9][9];

        for (int i = 0; i < 9; i++) {

            String linha = reader.readLine();
            String[] valores = linha.split(" ");

            for (int j = 0; j < 9; j++) {
                board[i][j] = Integer.parseInt(valores[j]);
            }
        }

        reader.close();

        return new SudokuBoard(board);
    }
}