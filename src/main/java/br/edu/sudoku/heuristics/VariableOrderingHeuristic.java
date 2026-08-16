package br.edu.sudoku.heuristics;

import br.edu.sudoku.model.SudokuBoard;

public interface VariableOrderingHeuristic {

    int[] selecionarCelula(SudokuBoard tabuleiro);

}