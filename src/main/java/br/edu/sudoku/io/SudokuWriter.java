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

        for (int i = 0; i < 9; i++) {

            for (int j = 0; j < 9; j++) {

                writer.print(board.get(i, j));

                if (j < 8) {
                    writer.print(" ");
                }
            }

            writer.println();
        }

        writer.close();
    }
}