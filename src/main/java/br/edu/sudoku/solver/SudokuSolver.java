/**
 * Interface que define o comportamento comum para todos os
 * algoritmos de resolução do Sudoku.
 */

package br.edu.sudoku.solver;

import br.edu.sudoku.model.SudokuBoard;
import br.edu.sudoku.metrics.Metrics;

public interface SudokuSolver {

    boolean solve(SudokuBoard board, Metrics metrics);
}