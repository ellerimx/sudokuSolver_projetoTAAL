//  Aqui testamos as técnicas de projeto de algoritmo de acordo com o nível de dificuldade escolhido.

package br.edu.sudoku;

import org.junit.jupiter.api.Test;

import br.edu.sudoku.io.SudokuReader;
import br.edu.sudoku.metrics.Metrics;
import br.edu.sudoku.model.SudokuBoard;
import br.edu.sudoku.solver.backtracking.BacktrackingSolver;
import br.edu.sudoku.solver.branchandbound.BranchAndBoundSolver;
import br.edu.sudoku.solver.dynamicprogramming.DynamicProgrammingSolver;
import br.edu.sudoku.solver.greedy.GreedySolver;

public class QuickPerformanceTest {

    @Test
    public void compararDesempenhoSolvers() throws Exception {

        String dificuldade = "medio"; // Altere aqui para testar outra dificuldade
        String caminho = TestUtils.caminhoParaDificuldade(dificuldade);
        String rotulo = TestUtils.formatarRotulo(dificuldade);

        System.setProperty("difficulty", dificuldade);
        System.setProperty("sudoku.difficulty", dificuldade);

        System.out.println("\n==============================");
        System.out.println(" TESTE RAPIDO DE DESEMPENHO ");
        System.out.println("==============================");
        System.out.println("Sudoku: " + rotulo);
        System.out.println();

        // -------- Backtracking --------
        SudokuBoard tabuleiroBt = SudokuReader.read(caminho);
        Metrics metricasBt = new Metrics();

        long inicioBt = System.nanoTime();
        new BacktrackingSolver(false).solve(tabuleiroBt, metricasBt);
        long tempoBt = (System.nanoTime() - inicioBt) / 1_000_000;

        System.out.println("Backtracking:");
        System.out.println("  Tempo (ms):     " + tempoBt);
        System.out.println("  Nos visitados:  " + metricasBt.getVisitedNodes());
        System.out.println("  Backtracks:     " + metricasBt.getBacktracks());
        System.out.println();

        // -------- Branch and Bound --------
        SudokuBoard tabuleiroBb = SudokuReader.read(caminho);
        Metrics metricasBb = new Metrics();

        long inicioBb = System.nanoTime();
        new BranchAndBoundSolver(false).solve(tabuleiroBb, metricasBb);
        long tempoBb = (System.nanoTime() - inicioBb) / 1_000_000;

        System.out.println("Branch and Bound:");
        System.out.println("  Tempo (ms):     " + tempoBb);
        System.out.println("  Nos visitados:  " + metricasBb.getVisitedNodes());
        System.out.println("  Backtracks:     " + metricasBb.getBacktracks());
        System.out.println();

        // -------- Greedy --------
        SudokuBoard tabuleiroG = SudokuReader.read(caminho);
        Metrics metricasG = new Metrics();

        long inicioG = System.nanoTime();
        new GreedySolver().solve(tabuleiroG, metricasG);
        long tempoG = (System.nanoTime() - inicioG) / 1_000_000;

        System.out.println("Greedy:");
        System.out.println("  Tempo (ms):     " + tempoG);
        System.out.println("  Nos visitados:  " + metricasG.getVisitedNodes());
        System.out.println("  Backtracks:     " + metricasG.getBacktracks());
        System.out.println();

        // -------- Dynamic Programming --------
        SudokuBoard tabuleiroDp = SudokuReader.read(caminho);
        Metrics metricasDp = new Metrics();

        long inicioDp = System.nanoTime();
        new DynamicProgrammingSolver().solve(tabuleiroDp, metricasDp);
        long tempoDp = (System.nanoTime() - inicioDp) / 1_000_000;

        System.out.println("Dynamic Programming:");
        System.out.println("  Tempo (ms):     " + tempoDp);
        System.out.println("  Nos visitados:  " + metricasDp.getVisitedNodes());
        System.out.println("  Backtracks:     " + metricasDp.getBacktracks());
        System.out.println();

        System.out.println("==============================\n");
    }
}