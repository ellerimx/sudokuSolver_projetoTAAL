package br.edu.sudoku;

import br.edu.sudoku.model.SudokuBoard;
import br.edu.sudoku.utils.SudokuValidator;

public class TestUtils {

    private TestUtils() {}

    /**
     * Retorna o caminho do arquivo de Sudoku de acordo
     * com o tamanho e a dificuldade.
     *
     * Exemplos:
     * 4  -> sudokus/sudoku_4x4_facil.txt
     * 9  -> sudokus/sudoku_9x9_medio.txt
     * 16 -> sudokus/sudoku_16x16_dificil.txt
     * 25 -> sudokus/sudoku_25x25_facil.txt
     */
    public static String caminhoParaDificuldade(
            int tamanho,
            String dificuldade) {

        String nivel = dificuldade == null
                ? "facil"
                : dificuldade.trim().toLowerCase();

        if (!nivel.equals("facil")
                && !nivel.equals("medio")
                && !nivel.equals("dificil")) {

            throw new IllegalArgumentException(
                    "Dificuldade inválida: " + dificuldade
            );
        }

        if (tamanho != 4
                && tamanho != 9
                && tamanho != 16
                && tamanho != 25) {

            throw new IllegalArgumentException(
                    "Tamanho inválido: " + tamanho
                    + ". Use 4, 9, 16 ou 25."
            );
        }

        return "sudokus/sudoku_"
                + tamanho
                + "x"
                + tamanho
                + "_"
                + nivel
                + ".txt";
    }

    /**
     * Mantém compatibilidade com códigos antigos
     * que ainda chamam o método apenas com a dificuldade.
     *
     * Nesse caso, utiliza 9x9.
     */
    public static String caminhoParaDificuldade(String dificuldade) {
        return caminhoParaDificuldade(9, dificuldade);
    }

    /**
     * Formata o nome da dificuldade para exibição.
     */
    public static String formatarRotulo(String dificuldade) {

        String valor = dificuldade == null
                ? "facil"
                : dificuldade.trim().toLowerCase();

        return switch (valor) {
            case "medio" -> "medio";
            case "dificil" -> "dificil";
            default -> "facil";
        };
    }

    /**
     * Verifica se o tabuleiro está completo e válido.
     *
     * Funciona para 4x4, 9x9, 16x16 e 25x25.
     */
    public static boolean tabuleiroCompletoEValido(
            SudokuBoard tabuleiro) {

        int tamanho = tabuleiro.getSize();

        for (int linha = 0; linha < tamanho; linha++) {

            for (int coluna = 0; coluna < tamanho; coluna++) {

                int valor = tabuleiro.get(linha, coluna);

                // Não pode existir célula vazia.
                if (valor < 1 || valor > tamanho) {
                    return false;
                }

                // Remove temporariamente o valor.
                tabuleiro.set(linha, coluna, 0);

                boolean valido = SudokuValidator.isValid(
                        tabuleiro,
                        linha,
                        coluna,
                        valor
                );

                // Coloca o valor novamente.
                tabuleiro.set(linha, coluna, valor);

                if (!valido) {
                    return false;
                }
            }
        }

        return true;
    }
}