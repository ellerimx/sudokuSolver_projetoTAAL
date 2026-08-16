package br.edu.sudoku;

import br.edu.sudoku.model.SudokuBoard;
import br.edu.sudoku.utils.SudokuValidator;

public class TestUtils {

    private TestUtils() {}

    public static String caminhoParaDificuldade(String dificuldade) {
        return switch (dificuldade.trim().toLowerCase()) {
            case "medio"   -> "sudokus/sudoku_medio.txt";
            case "dificil" -> "sudokus/sudoku_dificil.txt";
            default        -> "sudokus/sudoku_facil.txt";
        };
    }

    public static String formatarRotulo(String dificuldade) {
        String valor = dificuldade == null ? "facil" : dificuldade.trim().toLowerCase();
        return switch (valor) {
            case "medio"   -> "medio";
            case "dificil" -> "dificil";
            default        -> "facil";
        };
    }

    public static boolean tabuleiroCompletoEValido(SudokuBoard tabuleiro) {
        for (int linha = 0; linha < 9; linha++) {
            for (int coluna = 0; coluna < 9; coluna++) {
                int valor = tabuleiro.get(linha, coluna);
                if (valor < 1 || valor > 9) {
                    return false;
                }
                tabuleiro.set(linha, coluna, 0);
                boolean valido = SudokuValidator.isValid(tabuleiro, linha, coluna, valor);
                tabuleiro.set(linha, coluna, valor);
                if (!valido) {
                    return false;
                }
            }
        }
        return true;
    }
}