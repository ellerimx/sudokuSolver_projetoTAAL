/**
 * Classe responsável por armazenar métricas de desempenho dos algoritmos.
 */

package br.edu.sudoku.metrics;

public class Metrics {

    private long visitedNodes = 0;
    private long backtracks = 0;
    private long maxDepth = 0;
    private long memoryUsedBytes = 0;
    private long recursiveCalls = 0;
    private long maxVisitedNodes = Long.MAX_VALUE;

    public void incrementVisitedNodes() {
        if (visitedNodes >= maxVisitedNodes) {
            throw new VisitLimitReachedException();
        }

        visitedNodes++;
        atualizarMemoria();
    }

    public static class VisitLimitReachedException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    public void setMaxVisitedNodes(long maxVisitedNodes) {
        if (maxVisitedNodes <= 0) {
            throw new IllegalArgumentException("O limite de nós deve ser positivo.");
        }

        this.maxVisitedNodes = maxVisitedNodes;
    }

    public boolean isVisitLimitReached() {
        return visitedNodes >= maxVisitedNodes;
    }

    public void incrementBacktracks() {
        backtracks++;
        atualizarMemoria();
    }

    public void incrementRecursiveCalls() {
        recursiveCalls++;
        atualizarMemoria();
    }

    public void updateMaxDepth(long currentDepth) {
        if (currentDepth > maxDepth) {
            maxDepth = currentDepth;
        }

        atualizarMemoria();
    }

    /**
     * Atualiza o pico de memória utilizada pelo processo Java.
     *
     * A memória registrada corresponde ao maior uso de heap observado
     * durante a execução do algoritmo.
     */
    private void atualizarMemoria() {
        Runtime runtime = Runtime.getRuntime();

        long memoriaUsada =
                runtime.totalMemory() - runtime.freeMemory();

        if (memoriaUsada > memoryUsedBytes) {
            memoryUsedBytes = memoriaUsada;
        }
    }

    public void setMemoryUsedBytes(long bytes) {
        memoryUsedBytes = bytes;
    }

    public long getVisitedNodes() {
        return visitedNodes;
    }

    public long getBacktracks() {
        return backtracks;
    }

    public long getMaxDepth() {
        return maxDepth;
    }

    public long getMemoryUsedBytes() {
        return memoryUsedBytes;
    }

    public long getRecursiveCalls() {
        return recursiveCalls;
    }

    public void reset() {
        visitedNodes = 0;
        backtracks = 0;
        maxDepth = 0;
        memoryUsedBytes = 0;
        recursiveCalls = 0;
    }
}