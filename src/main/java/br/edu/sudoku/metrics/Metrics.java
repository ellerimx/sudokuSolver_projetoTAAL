/**
 * Classe responsável por armazenar métricas de desempenho dos algoritmos.
 */

package br.edu.sudoku.metrics;

public class Metrics {

    private long visitedNodes = 0;
    private long backtracks = 0;
    private long prunes = 0;
    private long maxDepth = 0;
    private long memoryUsedBytes = 0;
    private long recursiveCalls = 0;
    private long maxVisitedNodes = Long.MAX_VALUE;
    private long deadlineMillis = Long.MAX_VALUE;

    public void incrementVisitedNodes() {
        if (visitedNodes >= maxVisitedNodes || System.currentTimeMillis() >= deadlineMillis) {
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

    /**
     * Define um limite de tempo de execução, encerrando a busca (via
     * VisitLimitReachedException) caso o algoritmo demore demais para
     * escalar em tabuleiros grandes (ex.: 25x25).
     */
    public void setTimeLimitMillis(long timeLimitMillis) {
        if (timeLimitMillis <= 0) {
            throw new IllegalArgumentException("O limite de tempo deve ser positivo.");
        }

        this.deadlineMillis = System.currentTimeMillis() + timeLimitMillis;
    }

    public boolean isVisitLimitReached() {
        return visitedNodes >= maxVisitedNodes || System.currentTimeMillis() >= deadlineMillis;
    }

    public void incrementBacktracks() {
        backtracks++;
        atualizarMemoria();
    }

    /**
     * Registra uma poda: um ramo descartado por inviabilidade (ex.: bound
     * do Branch and Bound), distinto de um backtrack comum por esgotamento
     * de candidatos.
     */
    public void incrementPrunes() {
        prunes++;
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

    public long getPrunes() {
        return prunes;
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
        prunes = 0;
        maxDepth = 0;
        memoryUsedBytes = 0;
        recursiveCalls = 0;
    }
}