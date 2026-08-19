// Implementação da técnica de Programação Dinâmica

package br.edu.sudoku.solver.dynamicprogramming;

import java.util.HashSet;
import java.util.Set;

import br.edu.sudoku.metrics.Metrics;
import br.edu.sudoku.model.SudokuBoard;
import br.edu.sudoku.utils.SudokuValidator;

public class DynamicProgrammingSolver implements DynamicProgrammingAlgorithm {

    @Override
    public boolean solve(SudokuBoard tabuleiro, Metrics metricas) {
        return dynamicSolve(tabuleiro, metricas);
    }

    @Override
    public boolean dynamicSolve(SudokuBoard tabuleiro, Metrics metricas) {
        Set<String> estadosMortos = new HashSet<>();
        return resolverComMemo(tabuleiro, metricas, estadosMortos, 0);
    }

    private boolean resolverComMemo(SudokuBoard tabuleiro, Metrics metricas, Set<String> estadosMortos, long currentDepth) {
        metricas.incrementVisitedNodes();
        metricas.incrementRecursiveCalls();
        metricas.updateMaxDepth(currentDepth);

        if (metricas.isVisitLimitReached()) {
            return false;
        }

        int[] celula = selecionarCelulaLinear(tabuleiro);
        if (celula == null) {
            return true;
        }

        String chave = serializar(tabuleiro);
        if (estadosMortos.contains(chave)) {
            metricas.incrementBacktracks();
            return false;
        }

        int linha  = celula[0];
        int coluna = celula[1];

        int size = tabuleiro.getSize();

        for (int valor = 1; valor <= size; valor++) {
            if (!SudokuValidator.isValid(tabuleiro, linha, coluna, valor)) {
                continue;
            }

            tabuleiro.set(linha, coluna, valor);

            if (resolverComMemo(tabuleiro, metricas, estadosMortos, currentDepth + 1)) {
                return true;
            }

            tabuleiro.set(linha, coluna, 0);
        }

        estadosMortos.add(chave);
        metricas.incrementBacktracks();
        return false;
    }

    /**
     * Seleciona a primeira célula vazia encontrada, percorrendo linha por linha.
     * Ordem linear, sem heurística.
     *
     * @return coordenadas [linha, coluna] da primeira célula vazia, ou null se não houver
     */
    private int[] selecionarCelulaLinear(SudokuBoard tabuleiro) {
        int size = tabuleiro.getSize();

        for (int linha = 0; linha < size; linha++) {
            for (int coluna = 0; coluna < size; coluna++) {
                if (tabuleiro.get(linha, coluna) == 0) {
                    return new int[]{linha, coluna};
                }
            }
        }
        return null;
    }

    /**
    * Serializa o tabuleiro em uma string de tamanho N x N.
     * Usada como chave de memorizacao para identificar estados ja explorados.
     */
    private String serializar(SudokuBoard tabuleiro) {
        int size = tabuleiro.getSize();
        StringBuilder sb = new StringBuilder(size * size);

        for (int linha = 0; linha < size; linha++) {
            for (int coluna = 0; coluna < size; coluna++) {
                sb.append((char) ('0' + tabuleiro.get(linha, coluna)));
            }
        }
        return sb.toString();
    }
}