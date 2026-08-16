package br.edu.sudoku;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.edu.sudoku.model.SudokuBoard;
import br.edu.sudoku.utils.SudokuValidator;

public class SudokuValidatorTest {

	@BeforeEach
	public void imprimirDificuldade() {
		String dificuldade = System.getProperty("difficulty", "medio");
		System.out.println("Dificuldade de teste: (" + TestUtils.formatarRotulo(dificuldade) + ")");
	}

	@Test
	public void testeInsercaoValida() {
		int[][] tabuleiroArray = new int[9][9];
		tabuleiroArray[0][0] = 5;
		SudokuBoard tabuleiro = new SudokuBoard(tabuleiroArray);

		assertTrue(SudokuValidator.isValid(tabuleiro, 0, 1, 3));
	}

	@Test
	public void testeConflitoLinha() {
		int[][] tabuleiroArray = new int[9][9];
		tabuleiroArray[0][0] = 5;
		SudokuBoard tabuleiro = new SudokuBoard(tabuleiroArray);

		assertFalse(SudokuValidator.isValid(tabuleiro, 0, 2, 5));
	}

	@Test
	public void testeConflitoColuna() {
		int[][] tabuleiroArray = new int[9][9];
		tabuleiroArray[0][0] = 7;
		SudokuBoard tabuleiro = new SudokuBoard(tabuleiroArray);

		assertFalse(SudokuValidator.isValid(tabuleiro, 2, 0, 7));
	}

	@Test
	public void testeConflitoSubGrade() {
		int[][] tabuleiroArray = new int[9][9];
		tabuleiroArray[1][1] = 9;
		SudokuBoard tabuleiro = new SudokuBoard(tabuleiroArray);

		assertFalse(SudokuValidator.isValid(tabuleiro, 0, 2, 9));
	}

	@Test
	public void testeNumeroZeroInvalido() {
		int[][] tabuleiroArray = new int[9][9];
		SudokuBoard tabuleiro = new SudokuBoard(tabuleiroArray);

		assertFalse(SudokuValidator.isValid(tabuleiro, 0, 0, 0));
	}

	@Test
	public void testeNumeroMaiorQueNoveInvalido() {
		int[][] tabuleiroArray = new int[9][9];
		SudokuBoard tabuleiro = new SudokuBoard(tabuleiroArray);

		assertFalse(SudokuValidator.isValid(tabuleiro, 0, 0, 10));
	}
}