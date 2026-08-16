package br.edu.sudoku.experiment;

import br.edu.sudoku.TestUtils;
import br.edu.sudoku.io.SudokuReader;
import br.edu.sudoku.metrics.Metrics;
import br.edu.sudoku.model.SudokuBoard;
import br.edu.sudoku.solver.SudokuSolver;
import br.edu.sudoku.solver.backtracking.BacktrackingSolver;
import br.edu.sudoku.solver.branchandbound.BranchAndBoundSolver;
import br.edu.sudoku.solver.dynamicprogramming.DynamicProgrammingSolver;
import br.edu.sudoku.solver.greedy.GreedySolver;

public class PerformanceComparisonTest {

    private static final int EXECUCOES = 10;

    public static void main(String[] args) throws Exception {

        System.out.println("==================================");
        System.out.println("      COMPARACAO DE DESEMPENHO");
        System.out.println("==================================");

        String[] dificuldades = {"facil", "medio", "dificil"};

        for (String dificuldade : dificuldades) {
            System.setProperty("difficulty", dificuldade);
            System.setProperty("sudoku.difficulty", dificuldade);

            System.out.println("\n==================================");
            System.out.println("Sudoku: " + dificuldade.toUpperCase());
            System.out.println("==================================");

            executarExperimento(new BacktrackingSolver(false), "Backtracking",        dificuldade);
            executarExperimento(new BranchAndBoundSolver(false), "Branch and Bound",  dificuldade);
            executarExperimento(new GreedySolver(),               "Greedy",            dificuldade);
            executarExperimento(new DynamicProgrammingSolver(),   "Dynamic Programming", dificuldade);
        }
    }

    private static void executarExperimento(SudokuSolver solver, String nome, String dificuldade) throws Exception {

        String caminho = TestUtils.caminhoParaDificuldade(dificuldade);

        double[] tempos       = new double[EXECUCOES];
        long[]   nosVisitados = new long[EXECUCOES];
        long[]   backtracks   = new long[EXECUCOES];

        for (int i = 0; i < EXECUCOES; i++) {
            SudokuBoard tabuleiro = SudokuReader.read(caminho);
            Metrics metricas = new Metrics();

            long inicio = System.nanoTime();
            solver.solve(tabuleiro, metricas);
            long fim = System.nanoTime();

            tempos[i]       = (fim - inicio) / 1_000_000.0;
            nosVisitados[i] = metricas.getVisitedNodes();
            backtracks[i]   = metricas.getBacktracks();
        }

        double tempoMedio   = media(tempos);
        double desvioPadrao = desvio(tempos, tempoMedio);
        double mediaNos     = mediaLong(nosVisitados);
        double mediaBacks   = mediaLong(backtracks);

        System.out.println("\nAlgoritmo: " + nome);
        System.out.println("Execucoes: " + EXECUCOES);
        System.out.printf("Tempo medio:             %.3f ms%n", tempoMedio);
        System.out.printf("Desvio padrao:           %.3f ms%n", desvioPadrao);
        System.out.printf("Nos visitados (media):   %.0f%n",    mediaNos);
        System.out.printf("Backtracks (media):      %.0f%n",    mediaBacks);
    }

    private static double media(double[] array) {
        double soma = 0;
        for (double v : array) soma += v;
        return soma / array.length;
    }

    private static double desvio(double[] array, double media) {
        double soma = 0;
        for (double v : array) soma += Math.pow(v - media, 2);
        return Math.sqrt(soma / array.length);
    }

    private static double mediaLong(long[] array) {
        long soma = 0;
        for (long v : array) soma += v;
        return (double) soma / array.length;
    }
}