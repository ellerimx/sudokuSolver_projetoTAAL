/**
 * Classe responsável pela escrita da solução do "Sudoku" em arquivo.
 */

package br.edu.sudoku.io;

import br.edu.sudoku.model.SudokuBoard;

import java.io.FileWriter;
import java.io.PrintWriter;

public class SudokuWriter {

    public static void printBoard(SudokuBoard board) {
        board.printBoard();
    }

    public static void writeToFile(SudokuBoard board, String path) throws Exception {

        PrintWriter writer = new PrintWriter(new FileWriter(path));

        int tamanho = board.getSize();

        for (int i = 0; i < tamanho; i++) {

            for (int j = 0; j < tamanho; j++) {

                writer.print(board.get(i, j));

                if (j < tamanho - 1) {
                    writer.print(" ");
                }
            }

            writer.println();
        }

        writer.close();
    }
}