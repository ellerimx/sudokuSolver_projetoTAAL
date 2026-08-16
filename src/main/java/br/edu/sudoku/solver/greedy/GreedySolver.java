// Implementação de uma estratégia gulosa

package br.edu.sudoku.solver.greedy;

import br.edu.sudoku.metrics.Metrics;
import br.edu.sudoku.model.SudokuBoard;
import br.edu.sudoku.utils.SudokuValidator;

public class GreedySolver implements GreedyAlgorithm {

    @Override
    public boolean solve(SudokuBoard tabuleiro, Metrics metricas) {
        return greedySolve(tabuleiro, metricas);
    }

    @Override
    public boolean greedySolve(SudokuBoard tabuleiro, Metrics metricas) {
        return buscaGulosa(tabuleiro, metricas);
    }

    private boolean buscaGulosa(SudokuBoard tabuleiro, Metrics metricas) {
        metricas.incrementVisitedNodes();

        int[] celula = selecionarCelulaMaisRestrita(tabuleiro);
        if (celula == null) {
            return true;
        }

        int linha  = celula[0];
        int coluna = celula[1];
        int[] candidatosOrdenados = ordenarCandidatosPorRestricao(tabuleiro, linha, coluna);
        int numCandidatos = candidatosOrdenados[0];

        if (numCandidatos == 0) {
            metricas.incrementBacktracks();
            return false;
        }

        for (int i = 1; i <= numCandidatos; i++) {
            int candidato = candidatosOrdenados[i];
            tabuleiro.set(linha, coluna, candidato);

            if (buscaGulosa(tabuleiro, metricas)) {
                return true;
            }

            tabuleiro.set(linha, coluna, 0);
        }

        metricas.incrementBacktracks();
        return false;
    }

    /**
     * Seleciona a celula vazia com menor dominio (MRV — Minimum Remaining Values).
     * Se encontrar dominio 0, retorna imediatamente para falhar rapido.
     *
     * @return coordenadas [linha, coluna] da celula mais restrita, ou null se nao houver celulas vazias
     */
    private int[] selecionarCelulaMaisRestrita(SudokuBoard tabuleiro) {
        int melhorLinha   = -1;
        int melhorColuna  = -1;
        int melhorDominio = Integer.MAX_VALUE;

        for (int linha = 0; linha < 9; linha++) {
            for (int coluna = 0; coluna < 9; coluna++) {
                if (tabuleiro.get(linha, coluna) != 0) {
                    continue;
                }

                int dominio = contarCandidatos(tabuleiro, linha, coluna);

                // Dominio vazio: celula sem candidatos, falha rapida
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
     * Ordena os candidatos validos para (linha, coluna) pelo menor custo de restricao
     * (LCV — Least Constraining Value): prefere valores que menos restringem os vizinhos.
     *
     * @return array onde resultado[0] = quantidade de candidatos,
     *         resultado[1..n] = candidatos ordenados do menos ao mais restritivo
     */
    private int[] ordenarCandidatosPorRestricao(SudokuBoard tabuleiro, int linha, int coluna) {
        int[] valores = new int[9];
        int[] custos  = new int[9];
        int tamanho   = 0;

        for (int valor = 1; valor <= 9; valor++) {
            if (!SudokuValidator.isValid(tabuleiro, linha, coluna, valor)) {
                continue;
            }

            tabuleiro.set(linha, coluna, valor);
            valores[tamanho] = valor;
            custos[tamanho]  = estimarCustoRestricao(tabuleiro, linha, coluna);
            tabuleiro.set(linha, coluna, 0);
            tamanho++;
        }

        ordenarPorCusto(valores, custos, tamanho);

        int[] resultado = new int[10];
        resultado[0] = tamanho;
        for (int i = 0; i < tamanho; i++) {
            resultado[i + 1] = valores[i];
        }
        return resultado;
    }

    private int estimarCustoRestricao(SudokuBoard tabuleiro, int linha, int coluna) {
        int custo = 0;

        for (int c = 0; c < 9; c++) {
            if (c != coluna && tabuleiro.get(linha, c) == 0) {
                custo += contarCandidatos(tabuleiro, linha, c);
            }
        }

        for (int l = 0; l < 9; l++) {
            if (l != linha && tabuleiro.get(l, coluna) == 0) {
                custo += contarCandidatos(tabuleiro, l, coluna);
            }
        }

        int inicioLinha  = (linha  / 3) * 3;
        int inicioColuna = (coluna / 3) * 3;
        for (int l = inicioLinha; l < inicioLinha + 3; l++) {
            for (int c = inicioColuna; c < inicioColuna + 3; c++) {
                if (l != linha && c != coluna && tabuleiro.get(l, c) == 0) {
                    custo += contarCandidatos(tabuleiro, l, c);
                }
            }
        }

        return custo;
    }

    /**
     * Ordena {@code valores} pelo respectivo {@code custos} em ordem crescente (selection sort).
     * O tamanho real dos arrays e {@code tamanho}.
     */
    private void ordenarPorCusto(int[] valores, int[] custos, int tamanho) {
        for (int i = 0; i < tamanho - 1; i++) {
            int indiceMelhor = i;
            for (int j = i + 1; j < tamanho; j++) {
                if (custos[j] < custos[indiceMelhor]) {
                    indiceMelhor = j;
                }
            }

            if (indiceMelhor != i) {
                int tempCusto = custos[i];
                custos[i] = custos[indiceMelhor];
                custos[indiceMelhor] = tempCusto;

                int tempValor = valores[i];
                valores[i] = valores[indiceMelhor];
                valores[indiceMelhor] = tempValor;
            }
        }
    }
}