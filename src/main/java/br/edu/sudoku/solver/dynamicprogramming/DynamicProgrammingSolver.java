// Implementação da técnica de Programação Dinâmica

package br.edu.sudoku.solver.dynamicprogramming;

import java.util.HashSet;
import java.util.Set;

import br.edu.sudoku.metrics.Metrics;
import br.edu.sudoku.model.SudokuBoard;
import br.edu.sudoku.utils.SudokuValidator;

public class DynamicProgrammingSolver implements DynamicProgrammingAlgorithm {

    @Override
    public boolean solve(SudokuBoard tabuleiro, Metrics metricas) {
        return dynamicSolve(tabuleiro, metricas);
    }

    @Override
    public boolean dynamicSolve(SudokuBoard tabuleiro, Metrics metricas) {
        Set<String> estadosMortos = new HashSet<>();
        return resolverComMemo(tabuleiro, metricas, estadosMortos);
    }

    private boolean resolverComMemo(SudokuBoard tabuleiro, Metrics metricas, Set<String> estadosMortos) {
        metricas.incrementVisitedNodes();

        int[] celula = selecionarCelulaMaisRestrita(tabuleiro);
        if (celula == null) {
            return true;
        }

        String chave = serializar(tabuleiro);
        if (estadosMortos.contains(chave)) {
            metricas.incrementBacktracks();
            return false;
        }

        int linha  = celula[0];
        int coluna = celula[1];

        for (int valor = 1; valor <= 9; valor++) {
            if (!SudokuValidator.isValid(tabuleiro, linha, coluna, valor)) {
                continue;
            }

            tabuleiro.set(linha, coluna, valor);

            if (resolverComMemo(tabuleiro, metricas, estadosMortos)) {
                return true;
            }

            tabuleiro.set(linha, coluna, 0);
        }

        estadosMortos.add(chave);
        metricas.incrementBacktracks();
        return false;
    }

    /**
     * Seleciona a celula vazia com menor dominio (MRV — Minimum Remaining Values).
     * Reduz o espaco de busca ao atacar primeiro as celulas mais restritas.
     *
     * @return coordenadas [linha, coluna] da celula mais restrita, ou null se nao houver celulas vazias
     */
    private int[] selecionarCelulaMaisRestrita(SudokuBoard tabuleiro) {
        int melhorLinha  = -1;
        int melhorColuna = -1;
        int melhorDominio = Integer.MAX_VALUE;

        for (int linha = 0; linha < 9; linha++) {
            for (int coluna = 0; coluna < 9; coluna++) {
                if (tabuleiro.get(linha, coluna) != 0) {
                    continue;
                }

                int dominio = contarCandidatos(tabuleiro, linha, coluna);

                if (dominio == 0) {
                    return new int[]{linha, coluna};
                }

                if (dominio < melhorDominio) {
                    melhorDominio = dominio;
                    melhorLinha   = linha;
                    melhorColuna  = coluna;

                    if (melhorDominio == 1) {
                        return new int[]{melhorLinha, melhorColuna};
                    }
                }
            }
        }

        if (melhorLinha == -1) {
            return null;
        }

        return new int[]{melhorLinha, melhorColuna};
    }

    private int contarCandidatos(SudokuBoard tabuleiro, int linha, int coluna) {
        int cont = 0;
        for (int valor = 1; valor <= 9; valor++) {
            if (SudokuValidator.isValid(tabuleiro, linha, coluna, valor)) {
                cont++;
            }
        }
        return cont;
    }

    /**
     * Serializa o tabuleiro em uma string de 81 caracteres.
     * Usada como chave de memorizacao para identificar estados ja explorados.
     */
    private String serializar(SudokuBoard tabuleiro) {
        StringBuilder sb = new StringBuilder(81);
        for (int linha = 0; linha < 9; linha++) {
            for (int coluna = 0; coluna < 9; coluna++) {
                sb.append((char) ('0' + tabuleiro.get(linha, coluna)));
            }
        }
        return sb.toString();
    }
}