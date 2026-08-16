/**
 * Classe responsável por armazenar métricas de desempenho dos algoritmos.
 */

package br.edu.sudoku.metrics;

public class Metrics {

    private long visitedNodes = 0;
    private long backtracks = 0;

    public void incrementVisitedNodes() {
        visitedNodes++;
    }

    public void incrementBacktracks() {
        backtracks++;
    }

    public long getVisitedNodes() {
        return visitedNodes;
    }

    public long getBacktracks() {
        return backtracks;
    }
}