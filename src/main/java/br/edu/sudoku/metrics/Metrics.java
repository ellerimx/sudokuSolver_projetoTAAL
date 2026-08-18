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

    public void incrementVisitedNodes() {
        visitedNodes++;
    }

    public void incrementBacktracks() {
        backtracks++;
    }

    public void incrementRecursiveCalls() {
        recursiveCalls++;
    }

    public void updateMaxDepth(long currentDepth) {
        if (currentDepth > maxDepth) {
            maxDepth = currentDepth;
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