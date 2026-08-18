/**
 * Classe responsável por exportar os resultados de execução dos solvers para arquivo CSV.
 */

package br.edu.sudoku.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ResultsExporter {

    private static final String RESULTS_DIR = "src/main/resources/results";
    private static final String CSV_FILE = RESULTS_DIR + "/experiment_results.csv";
    private static final String CSV_HEADER = "algoritmo;dificuldade;execucao;tempo_ms;memoria_bytes;nos_visitados;chamadas_recursivas;backtracks;profundidade_maxima";

    /**
     * Exporta um resultado individual para o arquivo CSV.
     * Se o arquivo não existir, cria com cabeçalho. Se existir, adiciona a linha.
     *
     * @param nomeAlgoritmo Nome do algoritmo (ex: "Backtracking")
     * @param dificuldade Nível de dificuldade (ex: "facil", "medio", "dificil")
     * @param numExecucao Número da execução (1-10)
     * @param tempoMs Tempo de execução em milissegundos
     * @param memBytes Memória utilizada em bytes
     * @param nosVisitados Número de nós visitados
     * @param chamadosRecursivos Número total de chamadas recursivas
     * @param backtracks Número de backtracking realizados
     * @param maxDepth Profundidade máxima da árvore de busca
     */
    public static void exportarResultado(String nomeAlgoritmo, String dificuldade, int numExecucao,
                                        double tempoMs, long memBytes, long nosVisitados,
                                        long chamadosRecursivos, long backtracks, long maxDepth) {
        try {
            // Cria diretório se não existir
            File resultsDir = new File(RESULTS_DIR);
            if (!resultsDir.exists()) {
                resultsDir.mkdirs();
            }

            File csvFile = new File(CSV_FILE);
            boolean arquivoNovo = !csvFile.exists();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE, true))) {
                // Escreve cabeçalho se arquivo é novo
                if (arquivoNovo) {
                    writer.write(CSV_HEADER);
                    writer.newLine();
                }

                // Escreve linha de dados usando ponto e vírgula como separador
                String linha = String.format("%s;%s;%d;%.3f;%d;%d;%d;%d;%d",
                        nomeAlgoritmo,
                        dificuldade,
                        numExecucao,
                        tempoMs,
                        memBytes,
                        nosVisitados,
                        chamadosRecursivos,
                        backtracks,
                        maxDepth);

                writer.write(linha);
                writer.newLine();
            }

        } catch (IOException e) {
            System.err.println("Erro ao exportar resultados para CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Limpa o arquivo CSV existente (útil para reiniciar testes).
     */
    public static void limparResultados() {
        File csvFile = new File(CSV_FILE);
        if (csvFile.exists()) {
            csvFile.delete();
        }
    }

    /**
     * Retorna o caminho completo do arquivo CSV.
     */
    public static String obterCaminhoCSV() {
        return new File(CSV_FILE).getAbsolutePath();
    }
}
