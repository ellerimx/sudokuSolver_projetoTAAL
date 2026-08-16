package br.edu.sudoku.solver.greedy;

import br.edu.sudoku.solver.SudokuSolver;
import br.edu.sudoku.model.SudokuBoard;
import br.edu.sudoku.metrics.Metrics;

public interface GreedyAlgorithm extends SudokuSolver {

    boolean greedySolve(SudokuBoard board, Metrics metrics);

}