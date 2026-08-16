/**
 * Classe que representa o modelo do tabuleiro de "Sudoku".
 * O tabuleiro é representado como uma matriz 9x9 de inteiros,
 * onde cada posição corresponde a uma célula do "Sudoku".
 */

package br.edu.sudoku.model;

public class SudokuBoard {

    private int[][] board;
    private boolean[][] fixed;

    // cor ANSI azul para números fixos
    private static final String CIANO = "\u001B[36m";
    private static final String RESET = "\u001B[0m";

    public SudokuBoard(int[][] board) {

        this.board = board;
        this.fixed = new boolean[9][9];

        // marcar posições fixas
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                fixed[i][j] = board[i][j] != 0;
            }
        }
    }

    public int get(int linha, int col) {
        return board[linha][col];
    }

    public void set(int linha, int col, int value) {
        board[linha][col] = value;
    }

    public void printBoard() {

        System.out.println("+-------+-------+-------+");

        for (int i = 0; i < 9; i++) {

            System.out.print("| ");

            for (int j = 0; j < 9; j++) {

                int value = board[i][j];

                if (value == 0) {
                    System.out.print(". ");
                } else {

                    if (fixed[i][j]) {
                        System.out.print(CIANO + value + RESET + " ");
                    } else {
                        System.out.print(value + " ");
                    }
                }

                if ((j + 1) % 3 == 0) {
                    System.out.print("| ");
                }
            }

            System.out.println();

            if ((i + 1) % 3 == 0) {
                System.out.println("+-------+-------+-------+");
            }
        }
    }
}