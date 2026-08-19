/**
 * Classe utilitária responsável por validar estados do tabuleiro Sudoku.
 *
 * Contém métodos que verificam se um determinado valor pode ser inserido
 * em uma célula específica sem violar as regras do Sudoku:
 *
 * - não repetir números na mesma linha
 * - não repetir números na mesma coluna
 * - não repetir números no mesmo sub-bloco
 */

package br.edu.sudoku.utils;

import br.edu.sudoku.model.SudokuBoard;

public class SudokuValidator {

    public static boolean isValid(SudokuBoard board, int linha, int col, int num) {

        int size = board.getSize();

        if (num < 1 || num > size) {
            return false;
        }

        for (int i = 0; i < size; i++) {
            if (board.get(linha, i) == num) {
                return false;
            }
            if (board.get(i, col) == num) {
                return false;
            }
        }

        int boxSize = (int) Math.sqrt(size);

        if (boxSize * boxSize != size) {
            throw new IllegalArgumentException(
                    "O tamanho do tabuleiro deve ser um quadrado perfeito.");
        }

        int boxRow = (linha / boxSize) * boxSize;
        int boxCol = (col / boxSize) * boxSize;

        for (int i = boxRow; i < boxRow + boxSize; i++) {
            for (int j = boxCol; j < boxCol + boxSize; j++) {
                if (board.get(i, j) == num) {
                    return false;
                }
            }
        }

        return true;
    }
}