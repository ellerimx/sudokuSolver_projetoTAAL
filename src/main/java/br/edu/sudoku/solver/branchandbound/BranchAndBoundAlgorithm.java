package br.edu.sudoku.solver.branchandbound;

import br.edu.sudoku.solver.SudokuSolver;
import br.edu.sudoku.model.SudokuBoard;
import br.edu.sudoku.metrics.Metrics;

public interface BranchAndBoundAlgorithm extends SudokuSolver {

    boolean branchAndBound(SudokuBoard board, Metrics metrics);

}