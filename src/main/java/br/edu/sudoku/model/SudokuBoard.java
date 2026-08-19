/**
 * Classe que representa o modelo do tabuleiro de "Sudoku".
 */

package br.edu.sudoku.model;

public class SudokuBoard {

    private int[][] board;
    private boolean[][] fixed;
    private int size;

    // cor ANSI azul para números fixos
    private static final String CIANO = "\u001B[36m";
    private static final String RESET = "\u001B[0m";

    public SudokuBoard() {
        this(9);
    }

    public SudokuBoard(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("O tamanho do tabuleiro deve ser positivo.");
        }

        this.size = size;
        this.board = new int[size][size];
        this.fixed = new boolean[size][size];
    }

    public SudokuBoard(int[][] board) {
        if (board == null || board.length == 0) {
            throw new IllegalArgumentException("O tabuleiro não pode ser nulo ou vazio.");
        }

        this.size = board.length;

        for (int[] linha : board) {
            if (linha == null || linha.length != size) {
                throw new IllegalArgumentException("O tabuleiro deve ser uma matriz quadrada.");
            }
        }

        this.board = board;
        this.fixed = new boolean[size][size];

        // marcar posições fixas
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                fixed[i][j] = board[i][j] != 0;
            }
        }
    }

    public int getSize() {
        return size;
    }

    public int get(int linha, int col) {
        return board[linha][col];
    }

    public void set(int linha, int col, int value) {
        board[linha][col] = value;
    }

    public void printBoard() {
        int boxSize = (int) Math.sqrt(size);
        String separador = criarSeparador(boxSize);

        System.out.println(separador);

        for (int i = 0; i < size; i++) {

            System.out.print("| ");

            for (int j = 0; j < size; j++) {

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

                if ((j + 1) % boxSize == 0) {
                    System.out.print("| ");
                }
            }

            System.out.println();

            if ((i + 1) % boxSize == 0) {
                System.out.println(separador);
            }
        }
    }

    private String criarSeparador(int boxSize) {
        StringBuilder separador = new StringBuilder("+");

        for (int i = 0; i < size; i++) {
            separador.append("---");

            if ((i + 1) % boxSize == 0) {
                separador.append("+");
            }
        }

        return separador.toString();
    }
}