// Implementação da técnica Backtracking

package br.edu.sudoku.solver.backtracking;

import br.edu.sudoku.metrics.Metrics;
import br.edu.sudoku.model.SudokuBoard;
import br.edu.sudoku.utils.SudokuValidator;

public class BacktrackingSolver implements BacktrackingAlgorithm {

    private int passos = 0;
    private boolean visualizar = true;

    public BacktrackingSolver() {
        this(true);
    }

    public BacktrackingSolver(boolean visualizar) {
        this.visualizar = visualizar;
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
        return backtrack(tabuleiro, metricas);
    }

    @Override
    public boolean backtrack(SudokuBoard tabuleiro, Metrics metricas) {

        metricas.incrementVisitedNodes();

        for (int linha = 0; linha < 9; linha++) {
            for (int coluna = 0; coluna < 9; coluna++) {

                if (tabuleiro.get(linha, coluna) == 0) {

                    for (int numero = 1; numero <= 9; numero++) {

                        if (SudokuValidator.isValid(tabuleiro, linha, coluna, numero)) {

                            tabuleiro.set(linha, coluna, numero);
                            passos++;

                            if (visualizar) {
                                limparConsole();

                                System.out.println("");
                                System.out.println("=== Sudoku Solver (Backtracking) ===");
                                System.out.println("Dificuldade: " + obterRotuloDificuldade());
                                System.out.println("Passo: " + passos);
                                System.out.println("Tentando colocar " + numero + " em (" + linha + "," + coluna + ")\n");

                                tabuleiro.printBoard();
                                pausar();
                            }

                            if (backtrack(tabuleiro, metricas)) {
                                return true;
                            }

                            tabuleiro.set(linha, coluna, 0);
                            metricas.incrementBacktracks();
                            passos++;

                            if (visualizar) {
                                limparConsole();

                                System.out.println("");
                                System.out.println("=== Sudoku Solver (Backtracking) ===");
                                System.out.println("Dificuldade: " + obterRotuloDificuldade());
                                System.out.println("Passo: " + passos);
                                System.out.println("Backtracking removendo " + numero + " de (" + linha + "," + coluna + ")\n");

                                tabuleiro.printBoard();
                                pausar();
                            }
                        }
                    }

                    return false;
                }
            }
        }

        return true;
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