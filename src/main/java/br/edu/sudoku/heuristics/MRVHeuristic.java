/**
 * Implementação da heurística MRV (Minimum Remaining Values).
 * Escolhe a célula vazia com o menor número de valores possíveis.
 * Isso reduz o espaço de busca do algoritmo.
 */

package br.edu.sudoku.heuristics;

import br.edu.sudoku.model.SudokuBoard;
import br.edu.sudoku.utils.SudokuValidator;

public class MRVHeuristic implements VariableOrderingHeuristic {

    @Override
    public int[] selecionarCelula(SudokuBoard board) {

        int minimoOpcoes = Integer.MAX_VALUE;
        int[] melhorCelula = null;

        for (int linha = 0; linha < 9; linha++) {

            for (int coluna = 0; coluna < 9; coluna++) {

                if (board.get(linha, coluna) == 0) {

                    int[] candidatos = obterCandidatos(board, linha, coluna);
                    int numCandidatos = candidatos[0]; // posição 0 guarda a quantidade de candidatos

                    if (numCandidatos < minimoOpcoes) {
                        minimoOpcoes = numCandidatos;
                        melhorCelula = new int[]{linha, coluna};

                        if (minimoOpcoes == 1) {
                            return melhorCelula;
                        }
                    }
                }
            }
        }

        return melhorCelula;
    }

    private int[] obterCandidatos(SudokuBoard board, int linha, int coluna) {

        int[] candidatos = new int[10]; // índice 0 = contagem, 1..9 = valores possíveis
        int cont = 0;

        for (int numero = 1; numero <= 9; numero++) {

            if (SudokuValidator.isValid(board, linha, coluna, numero)) {
                cont++;
                candidatos[cont] = numero;
            }
        }

        candidatos[0] = cont;
        return candidatos;
    }
}