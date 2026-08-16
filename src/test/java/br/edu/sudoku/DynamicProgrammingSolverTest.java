package br.edu.sudoku;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import br.edu.sudoku.io.SudokuReader;
import br.edu.sudoku.metrics.Metrics;
import br.edu.sudoku.model.SudokuBoard;
import br.edu.sudoku.solver.dynamicprogramming.DynamicProgrammingSolver;

public class DynamicProgrammingSolverTest {

    @Test
    public void testDynamicProgrammingSolvesSudokus() throws Exception {

        String[] dificuldades = {"facil", "medio", "dificil"};

        for (String dificuldade : dificuldades) {
            String rotulo = TestUtils.formatarRotulo(dificuldade);
            String caminho = TestUtils.caminhoParaDificuldade(dificuldade);

            System.out.println("Dificuldade de teste: (" + rotulo + ")");

            SudokuBoard tabuleiro = SudokuReader.read(caminho);
            Metrics metricas = new Metrics();

            DynamicProgrammingSolver solver = new DynamicProgrammingSolver();
            boolean resolvido = solver.solve(tabuleiro, metricas);

            assertTrue(resolvido, "DynamicProgrammingSolver deve resolver o sudoku (" + rotulo + ")");
            assertTrue(TestUtils.tabuleiroCompletoEValido(tabuleiro),
                    "Solução produzida deve ser completa e válida (" + rotulo + ")");

            System.out.printf("Sudoku resolvido (%s) | Nós: %d | Backtracks: %d%n%n",
                    rotulo, metricas.getVisitedNodes(), metricas.getBacktracks());
        }
    }
}