/**
 * Classe utilitária responsável por validar estados do tabuleiro Sudoku.
 *
 * Contém métodos que verificam se um determinado valor pode ser inserido
 * em uma célula específica sem violar as regras do Sudoku:
 *
 * - não repetir números na mesma linha
 * - não repetir números na mesma coluna
 * - não repetir números no mesmo sub-bloco 3x3
 */

package br.edu.sudoku.utils;

import br.edu.sudoku.model.SudokuBoard;

public class SudokuValidator {

    public static boolean isValid(SudokuBoard board, int linha, int col, int num) {

        if (num < 1 || num > 9) {
            return false;
        }

        for (int i = 0; i < 9; i++) {
            if (board.get(linha, i) == num) {
                return false;
            }
            if (board.get(i, col) == num) {
                return false;
            }
        }

        int boxRow = (linha / 3) * 3;
        int boxCol = (col / 3) * 3;

        for (int i = boxRow; i < boxRow + 3; i++) {
            for (int j = boxCol; j < boxCol + 3; j++) {
                if (board.get(i, j) == num) {
                    return false;
                }
            }
        }

        return true;
    }
}