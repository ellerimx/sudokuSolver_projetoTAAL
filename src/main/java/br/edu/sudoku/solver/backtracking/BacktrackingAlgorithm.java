package br.edu.sudoku.solver.backtracking;

import br.edu.sudoku.solver.SudokuSolver;
import br.edu.sudoku.model.SudokuBoard;
import br.edu.sudoku.metrics.Metrics;

public interface BacktrackingAlgorithm extends SudokuSolver {

    boolean backtrack(SudokuBoard board, Metrics metrics);

}