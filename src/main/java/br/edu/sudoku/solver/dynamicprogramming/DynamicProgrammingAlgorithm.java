package br.edu.sudoku.solver.dynamicprogramming;

import br.edu.sudoku.solver.SudokuSolver;
import br.edu.sudoku.model.SudokuBoard;
import br.edu.sudoku.metrics.Metrics;

public interface DynamicProgrammingAlgorithm extends SudokuSolver {

    boolean dynamicSolve(SudokuBoard board, Metrics metrics);

}