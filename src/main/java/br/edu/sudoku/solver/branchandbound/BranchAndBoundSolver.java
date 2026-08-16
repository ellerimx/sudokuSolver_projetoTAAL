// Implementação da técnica Branch and Bound

package br.edu.sudoku.solver.branchandbound;

import br.edu.sudoku.heuristics.MRVHeuristic;
import br.edu.sudoku.heuristics.VariableOrderingHeuristic;
import br.edu.sudoku.metrics.Metrics;
import br.edu.sudoku.model.SudokuBoard;
import br.edu.sudoku.utils.SudokuValidator;

public class BranchAndBoundSolver implements BranchAndBoundAlgorithm {

    private final boolean visualizar;
    private final VariableOrderingHeuristic heuristica;

    private int passos;
    private int melhorBound;

    public BranchAndBoundSolver() {
        this(true);
    }

    public BranchAndBoundSolver(boolean visualizar) {
        this.visualizar = visualizar;
        this.heuristica = new MRVHeuristic();
    }

    private String resolverRotuloDificuldade() {
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
        return branchAndBound(tabuleiro, metricas);
    }

    @Override
    public boolean branchAndBound(SudokuBoard tabuleiro, Metrics metricas) {
        passos = 0;
        melhorBound = Integer.MAX_VALUE;
        return resolver(tabuleiro, metricas);
    }

    private boolean resolver(SudokuBoard tabuleiro, Metrics metricas) {
        metricas.incrementVisitedNodes();

        int[] celula = heuristica.selecionarCelula(tabuleiro);
        if (celula == null) {
            return true;
        }

        int linha  = celula[0];
        int coluna = celula[1];

        int[] candidatos  = obterCandidatos(tabuleiro, linha, coluna);
        int numCandidatos = candidatos[0];

        if (numCandidatos == 0) {
            metricas.incrementBacktracks();
            return false;
        }

        for (int i = 1; i <= numCandidatos; i++) {
            int numero = candidatos[i];

            tabuleiro.set(linha, coluna, numero);
            passos++;
            exibirPasso(tabuleiro, linha, coluna, numero, false);

            int bound = calcularBound(tabuleiro);

            if (bound > 0) {
                tabuleiro.set(linha, coluna, 0);
                metricas.incrementBacktracks();
                passos++;
                exibirPasso(tabuleiro, linha, coluna, numero, true);
                continue;
            }

            melhorBound = bound;

            if (resolver(tabuleiro, metricas)) {
                return true;
            }

            tabuleiro.set(linha, coluna, 0);
            passos++;
            exibirPasso(tabuleiro, linha, coluna, numero, true);
        }

        metricas.incrementBacktracks();
        return false;
    }

    private int calcularBound(SudokuBoard tabuleiro) {
        int celulasSemCandidatos = 0;
        for (int l = 0; l < 9; l++) {
            for (int c = 0; c < 9; c++) {
                if (tabuleiro.get(l, c) == 0 && contarCandidatos(tabuleiro, l, c) == 0) {
                    celulasSemCandidatos++;
                }
            }
        }
        return celulasSemCandidatos;
    }

    private int contarCandidatos(SudokuBoard tabuleiro, int linha, int coluna) {
        int cont = 0;
        for (int numero = 1; numero <= 9; numero++) {
            if (SudokuValidator.isValid(tabuleiro, linha, coluna, numero)) {
                cont++;
            }
        }
        return cont;
    }

    private int[] obterCandidatos(SudokuBoard tabuleiro, int linha, int coluna) {
        int[] candidatos = new int[10];
        int cont = 0;

        for (int numero = 1; numero <= 9; numero++) {
            if (SudokuValidator.isValid(tabuleiro, linha, coluna, numero)) {
                cont++;
                candidatos[cont] = numero;
            }
        }

        candidatos[0] = cont;
        return candidatos;
    }

    private void exibirPasso(SudokuBoard tabuleiro, int linha, int coluna, int numero, boolean backtrack) {
        if (!visualizar) {
            return;
        }

        limparConsole();
        System.out.println("=== Sudoku Solver (Branch and Bound) ===");
        System.out.println("Dificuldade: " + resolverRotuloDificuldade());
        System.out.println("Passo: " + passos);

        if (backtrack) {
            System.out.println("Backtracking removendo " + numero + " de (" + linha + "," + coluna + ")\n");
        } else {
            System.out.println("Tentando colocar " + numero + " em (" + linha + "," + coluna + ")\n");
        }

        tabuleiro.printBoard();
        pausar();
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