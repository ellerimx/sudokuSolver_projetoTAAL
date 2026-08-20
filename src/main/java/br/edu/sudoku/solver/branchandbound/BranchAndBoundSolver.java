// Implementação da técnica Branch and Bound

package br.edu.sudoku.solver.branchandbound;

import br.edu.sudoku.metrics.Metrics;
import br.edu.sudoku.model.SudokuBoard;
import br.edu.sudoku.solver.PassoObservador;
import br.edu.sudoku.utils.SudokuValidator;

public class BranchAndBoundSolver implements BranchAndBoundAlgorithm {

    private final boolean visualizar;
    private final PassoObservador observador;

    private int passos;
    private int melhorBound;

    public BranchAndBoundSolver() {
        this(true);
    }

    public BranchAndBoundSolver(boolean visualizar) {
        this.visualizar = visualizar;
        this.observador = null;
    }

    public BranchAndBoundSolver(PassoObservador observador) {
        this.visualizar = false;
        this.observador = observador;
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
        return resolver(tabuleiro, metricas, 0);
    }

    private boolean resolver(SudokuBoard tabuleiro, Metrics metricas, long currentDepth) {
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
                metricas.incrementPrunes();
                passos++;
                exibirPasso(tabuleiro, linha, coluna, numero, true);
                continue;
            }

            melhorBound = bound;

            if (resolver(tabuleiro, metricas, currentDepth + 1)) {
                return true;
            }

            tabuleiro.set(linha, coluna, 0);
            passos++;
            exibirPasso(tabuleiro, linha, coluna, numero, true);
        }

        metricas.incrementBacktracks();
        return false;
    }

    /**
     * Seleciona a primeira célula vazia encontrada, percorrendo linha por linha.
     * Ordem linear, sem heurística.
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

    private int calcularBound(SudokuBoard tabuleiro) {
        int size = tabuleiro.getSize();
        int celulasSemCandidatos = 0;
        for (int l = 0; l < size; l++) {
            for (int c = 0; c < size; c++) {
                if (tabuleiro.get(l, c) == 0 && contarCandidatos(tabuleiro, l, c) == 0) {
                    celulasSemCandidatos++;
                }
            }
        }
        return celulasSemCandidatos;
    }

    private int contarCandidatos(SudokuBoard tabuleiro, int linha, int coluna) {
        int size = tabuleiro.getSize();
        int cont = 0;
        for (int numero = 1; numero <= size; numero++) {
            if (SudokuValidator.isValid(tabuleiro, linha, coluna, numero)) {
                cont++;
            }
        }
        return cont;
    }

    private int[] obterCandidatos(SudokuBoard tabuleiro, int linha, int coluna) {
        int size = tabuleiro.getSize();
        int[] candidatos = new int[size + 1];
        int cont = 0;

        for (int numero = 1; numero <= size; numero++) {
            if (SudokuValidator.isValid(tabuleiro, linha, coluna, numero)) {
                cont++;
                candidatos[cont] = numero;
            }
        }

        candidatos[0] = cont;
        return candidatos;
    }

    private void exibirPasso(SudokuBoard tabuleiro, int linha, int coluna, int numero, boolean backtrack) {
        String descricao = backtrack
                ? "Backtracking removendo " + numero + " de (" + linha + "," + coluna + ")"
                : "Tentando colocar " + numero + " em (" + linha + "," + coluna + ")";

        if (observador != null) {
            observador.aoPasso(tabuleiro, passos, descricao, linha, coluna, backtrack);
            pausar();
        } else if (visualizar) {
            limparConsole();
            System.out.println("=== Sudoku Solver (Branch and Bound) ===");
            System.out.println("Dificuldade: " + resolverRotuloDificuldade());
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