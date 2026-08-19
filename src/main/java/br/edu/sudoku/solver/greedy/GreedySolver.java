// Implementação de uma estratégia gulosa

package br.edu.sudoku.solver.greedy;

import br.edu.sudoku.metrics.Metrics;
import br.edu.sudoku.model.SudokuBoard;
import br.edu.sudoku.utils.SudokuValidator;

public class GreedySolver implements GreedyAlgorithm {

    @Override
    public boolean solve(SudokuBoard tabuleiro, Metrics metricas) {
        return greedySolve(tabuleiro, metricas);
    }

    @Override
    public boolean greedySolve(SudokuBoard tabuleiro, Metrics metricas) {
        return buscaGulosa(tabuleiro, metricas, 0);
    }

    private boolean buscaGulosa(SudokuBoard tabuleiro, Metrics metricas, long currentDepth) {
        metricas.incrementVisitedNodes();
        metricas.incrementRecursiveCalls();
        metricas.updateMaxDepth(currentDepth);

        if (metricas.isVisitLimitReached()) {
            return false;
        }

        int size = tabuleiro.getSize();

        for (int linha = 0; linha < size; linha++) {
            for (int coluna = 0; coluna < size; coluna++) {
                if (tabuleiro.get(linha, coluna) != 0) {
                    continue;
                }

                for (int valor = 1; valor <= size; valor++) {
                    if (SudokuValidator.isValid(tabuleiro, linha, coluna, valor)) {
                        tabuleiro.set(linha, coluna, valor);
                        return buscaGulosa(tabuleiro, metricas, currentDepth + 1);
                    }
                }

                metricas.incrementBacktracks();
                return false;
            }
        }

        return true;
    }
}