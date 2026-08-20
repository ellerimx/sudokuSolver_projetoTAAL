// Implementação da técnica Backtracking

package br.edu.sudoku.solver.backtracking;

import br.edu.sudoku.metrics.Metrics;
import br.edu.sudoku.model.SudokuBoard;
import br.edu.sudoku.solver.PassoObservador;
import br.edu.sudoku.utils.SudokuValidator;

public class BacktrackingSolver implements BacktrackingAlgorithm {

    private int passos = 0;
    private boolean visualizar = true;
    private PassoObservador observador;

    public BacktrackingSolver() {
        this(true);
    }

    public BacktrackingSolver(boolean visualizar) {
        this.visualizar = visualizar;
    }

    public BacktrackingSolver(PassoObservador observador) {
        this.visualizar = false;
        this.observador = observador;
    }

    private String obterRotuloDificuldade() {
        String bruto = System.getProperty("difficulty", System.getProperty("sudoku.difficulty", "facil"));
        String valor = bruto == null ? "facil" : bruto.trim().toLowerCase();

        if (valor.equals("medio")) {
            return "MEDIO";
        } else if (valor.equals("dificil")) {
            return "DIFICIL";
        } else {
            return "FACIL";
        }
    }

    @Override
    public boolean solve(SudokuBoard tabuleiro, Metrics metricas) {
        return backtrack(tabuleiro, metricas, 0);
    }

    @Override
    public boolean backtrack(SudokuBoard tabuleiro, Metrics metricas) {
        return backtrack(tabuleiro, metricas, 0);
    }

    private boolean backtrack(SudokuBoard tabuleiro, Metrics metricas, long currentDepth) {

        metricas.incrementVisitedNodes();
        metricas.incrementRecursiveCalls();
        metricas.updateMaxDepth(currentDepth);

        if (metricas.isVisitLimitReached()) {
            return false;
        }

        int size = tabuleiro.getSize();

        for (int linha = 0; linha < size; linha++) {
            for (int coluna = 0; coluna < size; coluna++) {

                if (tabuleiro.get(linha, coluna) == 0) {

                    for (int numero = 1; numero <= size; numero++) {

                        if (SudokuValidator.isValid(tabuleiro, linha, coluna, numero)) {

                            tabuleiro.set(linha, coluna, numero);
                            passos++;
                            notificarPasso(tabuleiro, "Tentando colocar " + numero + " em (" + linha + "," + coluna + ")", linha, coluna, false);

                            if (backtrack(tabuleiro, metricas, currentDepth + 1)) {
                                return true;
                            }

                            tabuleiro.set(linha, coluna, 0);
                            metricas.incrementBacktracks();
                            passos++;
                            notificarPasso(tabuleiro, "Backtracking removendo " + numero + " de (" + linha + "," + coluna + ")", linha, coluna, true);
                        }
                    }

                    return false;
                }
            }
        }

        return true;
    }

    private void notificarPasso(SudokuBoard tabuleiro, String descricao, int linha, int coluna, boolean backtrack) {
        if (observador != null) {
            observador.aoPasso(tabuleiro, passos, descricao, linha, coluna, backtrack);
            pausar();
        } else if (visualizar) {
            limparConsole();

            System.out.println("");
            System.out.println("=== Sudoku Solver (Backtracking) ===");
            System.out.println("Dificuldade: " + obterRotuloDificuldade());
            System.out.println("Passo: " + passos);
            System.out.println(descricao + "\n");

            tabuleiro.printBoard();
            pausar();
        }
    }

    private void pausar() {
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void limparConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}