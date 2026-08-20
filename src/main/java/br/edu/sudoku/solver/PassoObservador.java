/**
 * Callback usado pelos solvers para notificar cada passo da resolução,
 * permitindo exibir o passo a passo fora do terminal (por exemplo, em uma interface gráfica).
 */

package br.edu.sudoku.solver;

import br.edu.sudoku.model.SudokuBoard;

public interface PassoObservador {

    void aoPasso(SudokuBoard tabuleiroAtual, int passo, String descricao, int linha, int coluna, boolean backtrack);
}
