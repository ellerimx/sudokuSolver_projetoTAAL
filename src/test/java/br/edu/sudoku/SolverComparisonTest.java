package br.edu.sudoku;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import br.edu.sudoku.io.SudokuReader;
import br.edu.sudoku.metrics.Metrics;
import br.edu.sudoku.model.SudokuBoard;
import br.edu.sudoku.solver.backtracking.BacktrackingSolver;
import br.edu.sudoku.solver.branchandbound.BranchAndBoundSolver;
import br.edu.sudoku.solver.dynamicprogramming.DynamicProgrammingSolver;
import br.edu.sudoku.solver.greedy.GreedySolver;

public class SolverComparisonTest {

	@Test
	public void testarSolversProduzemSolucaoValida() throws Exception {

		String solverProp = System.getProperty("solver", "all").toLowerCase();
		String dificuldade = System.getProperty("difficulty", "medio").toLowerCase();
		String rotulo = TestUtils.formatarRotulo(dificuldade);
		String caminho = TestUtils.caminhoParaDificuldade(dificuldade);

		System.out.println("Dificuldade de teste: (" + rotulo + ")");
		System.out.println("Solver(s): " + solverProp);
		System.out.println();

		boolean all = solverProp.equals("all");

		// -------- Backtracking --------
		if (all || solverProp.equals("backtracking")) {
			SudokuBoard tabuleiro = SudokuReader.read(caminho);
			Metrics metricas = new Metrics();

			BacktrackingSolver solver = new BacktrackingSolver(false);
			boolean resolvido = solver.solve(tabuleiro, metricas);

			assertTrue(resolvido, "BacktrackingSolver deve resolver o Sudoku (" + rotulo + ")");
			assertTrue(TestUtils.tabuleiroCompletoEValido(tabuleiro),
					"Solução do Backtracking deve ser válida (" + rotulo + ")");

			System.out.printf("Backtracking (%s) | Nós: %d | Backtracks: %d%n",
					rotulo, metricas.getVisitedNodes(), metricas.getBacktracks());
		}

		// -------- Branch and Bound --------
		if (all || solverProp.equals("branchandbound") || solverProp.equals("branch_and_bound")) {
			SudokuBoard tabuleiro = SudokuReader.read(caminho);
			Metrics metricas = new Metrics();

			BranchAndBoundSolver solver = new BranchAndBoundSolver(false);
			boolean resolvido = solver.solve(tabuleiro, metricas);

			assertTrue(resolvido, "BranchAndBoundSolver deve resolver o Sudoku (" + rotulo + ")");
			assertTrue(TestUtils.tabuleiroCompletoEValido(tabuleiro),
					"Solução do Branch and Bound deve ser válida (" + rotulo + ")");

			System.out.printf("Branch and Bound (%s) | Nós: %d | Backtracks: %d%n",
					rotulo, metricas.getVisitedNodes(), metricas.getBacktracks());
		}

		// -------- Greedy --------
		if (all || solverProp.equals("greedy")) {
			SudokuBoard tabuleiro = SudokuReader.read(caminho);
			Metrics metricas = new Metrics();

			GreedySolver solver = new GreedySolver();
			boolean resolvido = solver.solve(tabuleiro, metricas);

			assertTrue(resolvido, "GreedySolver deve resolver o Sudoku (" + rotulo + ")");
			assertTrue(TestUtils.tabuleiroCompletoEValido(tabuleiro),
					"Solução do Greedy deve ser válida (" + rotulo + ")");

			System.out.printf("Greedy (%s) | Nós: %d | Backtracks: %d%n",
					rotulo, metricas.getVisitedNodes(), metricas.getBacktracks());
		}

		// -------- Dynamic Programming --------
		if (all || solverProp.equals("dynamic") || solverProp.equals("dp")) {
			SudokuBoard tabuleiro = SudokuReader.read(caminho);
			Metrics metricas = new Metrics();

			DynamicProgrammingSolver solver = new DynamicProgrammingSolver();
			boolean resolvido = solver.solve(tabuleiro, metricas);

			assertTrue(resolvido, "DynamicProgrammingSolver deve resolver o Sudoku (" + rotulo + ")");
			assertTrue(TestUtils.tabuleiroCompletoEValido(tabuleiro),
					"Solução do Dynamic Programming deve ser válida (" + rotulo + ")");

			System.out.printf("Dynamic Programming (%s) | Nós: %d | Backtracks: %d%n",
					rotulo, metricas.getVisitedNodes(), metricas.getBacktracks());
		}
	}
}