package br.edu.sudoku.experiment;

import java.lang.management.ManagementFactory;
import com.sun.management.ThreadMXBean;

import br.edu.sudoku.TestUtils;
import br.edu.sudoku.io.ResultsExporter;
import br.edu.sudoku.io.SudokuReader;
import br.edu.sudoku.metrics.Metrics;
import br.edu.sudoku.model.SudokuBoard;
import br.edu.sudoku.solver.SudokuSolver;
import br.edu.sudoku.solver.backtracking.BacktrackingSolver;
import br.edu.sudoku.solver.branchandbound.BranchAndBoundSolver;
import br.edu.sudoku.solver.dynamicprogramming.DynamicProgrammingSolver;


public class PerformanceComparisonTest {

    private static final int EXECUCOES = 10;
    private static final long LIMITE_NOS_DIMENSOES_GRANDES = 100_000;
    private static final long LIMITE_TEMPO_DIMENSOES_GRANDES_MS = 10_000;

    private static final ThreadMXBean THREAD_BEAN =
    (ThreadMXBean) ManagementFactory.getThreadMXBean();

    static {
        if (THREAD_BEAN.isThreadAllocatedMemorySupported()
                && !THREAD_BEAN.isThreadAllocatedMemoryEnabled()) {
            THREAD_BEAN.setThreadAllocatedMemoryEnabled(true);
        }
    }

private static long memoriaAlocada() {
    if (THREAD_BEAN.isThreadAllocatedMemorySupported()
            && THREAD_BEAN.isThreadAllocatedMemoryEnabled()) {

        return THREAD_BEAN.getThreadAllocatedBytes(
                Thread.currentThread().threadId()
        );
    }

    return 0;
}

    public static void main(String[] args) throws Exception {

        System.out.println("==================================");
        System.out.println("      COMPARACAO DE DESEMPENHO");
        System.out.println("==================================");

        // Limpar CSV anterior para garantir que apenas dados desta execução sejam salvos
        ResultsExporter.limparResultados();

        executarExperimentoPorDificuldade();
        executarExperimentoPorTamanho();

        System.out.println("\n==================================");
        System.out.println("RESULTADOS EXPORTADOS PARA CSV");
        System.out.println("Localização: " + ResultsExporter.obterCaminhoCSV());
        System.out.println("==================================");
    }

    private static void executarExperimentoPorDificuldade() throws Exception {
        System.out.println("\n==================================");
        System.out.println("EIXO 1: DIFICULDADE (TABULEIROS 9x9)");
        System.out.println("==================================");

        String[] dificuldades = {"facil", "medio", "dificil"};

        for (String dificuldade : dificuldades) {
            System.setProperty("difficulty", dificuldade);
            System.setProperty("sudoku.difficulty", dificuldade);

            System.out.println("\n==================================");
            System.out.println("Sudoku: " + dificuldade.toUpperCase());
            System.out.println("==================================");

            executarExperimentoPorDificuldade(new BacktrackingSolver(false), "Backtracking", dificuldade);
            executarExperimentoPorDificuldade(new BranchAndBoundSolver(false), "Branch and Bound", dificuldade);
            executarExperimentoPorDificuldade(new DynamicProgrammingSolver(), "Dynamic Programming", dificuldade);
        }
    }

    private static void executarExperimentoPorTamanho() throws Exception {
        System.out.println("\n==================================");
        System.out.println("EIXO 2: ESCALABILIDADE DO TAMANHO");
        System.out.println("==================================");

        int[] tamanhos = {4, 9, 16, 25};
        String[] dificuldades = {"facil", "medio", "dificil"};
        String[][] caminhos = {
            {
                "sudokus/sudoku_4x4_facil.txt",
                "sudokus/sudoku_4x4_medio.txt",
                "sudokus/sudoku_4x4_dificil.txt"
            },
            {
                "sudokus/sudoku_9x9_facil.txt",
                "sudokus/sudoku_9x9_medio.txt",
                "sudokus/sudoku_9x9_dificil.txt"
            },
            {
                "sudokus/sudoku_16x16_facil.txt",
                "sudokus/sudoku_16x16_medio.txt",
                "sudokus/sudoku_16x16_dificil.txt"
            },
            {
                "sudokus/sudoku_25x25_facil.txt",
                "sudokus/sudoku_25x25_medio.txt",
                "sudokus/sudoku_25x25_dificil.txt"
            }
        };

        for (int i = 0; i < tamanhos.length; i++) {
            int tamanho = tamanhos[i];
            String rotulo = tamanho + "x" + tamanho;

            for (int j = 0; j < dificuldades.length; j++) {
                String dificuldade = dificuldades[j];
                String caminho = caminhos[i][j];

                System.out.println("\n==================================");
                System.out.println("Sudoku: " + rotulo + " - " + dificuldade.toUpperCase());
                System.out.println("==================================");

                executarExperimentoPorTamanho(new BacktrackingSolver(false), "Backtracking",
                    caminho, tamanho, dificuldade);
                executarExperimentoPorTamanho(new BranchAndBoundSolver(false), "Branch and Bound",
                    caminho, tamanho, dificuldade);
                executarExperimentoPorTamanho(new DynamicProgrammingSolver(), "Dynamic Programming",
                    caminho, tamanho, dificuldade);
            }
        }
    }

    private static void executarExperimentoPorDificuldade(SudokuSolver solver, String nome,
                                                           String dificuldade) throws Exception {

        String caminho = TestUtils.caminhoParaDificuldade(dificuldade);
        executarExperimento(solver, nome, dificuldade, caminho, 9);
    }

    private static void executarExperimentoPorTamanho(SudokuSolver solver, String nome,
                                                       String caminho, int tamanho,
                                                       String dificuldade) throws Exception {
        executarExperimento(solver, nome, dificuldade, caminho, tamanho);
    }

    private static void executarExperimento(SudokuSolver solver, String nome, String grupo,
                                             String caminho, int tamanho) throws Exception {

        double[] tempos              = new double[EXECUCOES];
        long[]   nosVisitados        = new long[EXECUCOES];
        long[]   backtracks          = new long[EXECUCOES];
        long[]   podasArray          = new long[EXECUCOES];
        long[]   memoriaBytesArray   = new long[EXECUCOES];
        long[]   recursiveCallsArray = new long[EXECUCOES];
        long[]   maxDepthArray       = new long[EXECUCOES];
        int solucoesEncontradas = 0;

        for (int i = 0; i < EXECUCOES; i++) {
            SudokuBoard tabuleiro = SudokuReader.read(caminho);
            if (tabuleiro.getSize() != tamanho) {
                throw new IllegalArgumentException(
                        "A instância " + caminho + " não possui tamanho " + tamanho + "x" + tamanho);
            }
            Metrics metricas = new Metrics();
            if (tamanho >= 16) {
                metricas.setMaxVisitedNodes(LIMITE_NOS_DIMENSOES_GRANDES);
                // O Branch and Bound recalcula o bound varrendo o tabuleiro inteiro a cada
                // candidato tentado; em 25x25 isso torna o custo por nó caro o bastante para
                // não terminar em tempo hábil antes de atingir o limite de nós. O limite de
                // tempo garante que o experimento sempre finalize.
                metricas.setTimeLimitMillis(LIMITE_TEMPO_DIMENSOES_GRANDES_MS);
            }

            // Medição do tempo e da memória alocada pela thread do algoritmo
        long memAntes = memoriaAlocada();
        long inicio = System.nanoTime();

        boolean solucaoEncontrada;
        boolean limiteAtingido = false;

        try {
            solucaoEncontrada = solver.solve(tabuleiro, metricas);
        } catch (Metrics.VisitLimitReachedException e) {
            limiteAtingido = true;
            solucaoEncontrada = false;
        }

        long fim = System.nanoTime();
        long memDepois = memoriaAlocada();

        // Memória alocada durante a execução do algoritmo
        long memUsada = Math.max(0, memDepois - memAntes);

        metricas.setMemoryUsedBytes(memUsada);

            String statusExecucao = limiteAtingido || metricas.isVisitLimitReached()
                    ? "nao_concluido"
                    : solucaoEncontrada ? "concluido" : "falhou";

            if (solucaoEncontrada) {
                solucoesEncontradas++;
            }

            tempos[i]              = (fim - inicio) / 1_000_000.0;
            nosVisitados[i]        = metricas.getVisitedNodes();
            backtracks[i]          = metricas.getBacktracks();
            podasArray[i]          = metricas.getPrunes();
            memoriaBytesArray[i]   = metricas.getMemoryUsedBytes();
            recursiveCallsArray[i] = metricas.getRecursiveCalls();
            maxDepthArray[i]       = metricas.getMaxDepth();

            // Exportar cada execução para CSV
                ResultsExporter.exportarResultado(nome, grupo, tamanho, i + 1, tempos[i],
                    memoriaBytesArray[i], nosVisitados[i], recursiveCallsArray[i],
                    backtracks[i], podasArray[i], maxDepthArray[i], solucaoEncontrada, statusExecucao);
        }

        double tempoMedio   = media(tempos);
        double desvioPadrao = desvio(tempos, tempoMedio);
        double mediaNos     = mediaLong(nosVisitados);
        double mediaBacks   = mediaLong(backtracks);
        double mediaPodas   = mediaLong(podasArray);
        double mediaMemoria = mediaLong(memoriaBytesArray);
        double mediaRecursive = mediaLong(recursiveCallsArray);
        double mediaMaxDepth = mediaLong(maxDepthArray);

        System.out.println("\nAlgoritmo: " + nome);
        System.out.println("Execucoes: " + EXECUCOES);
        System.out.printf("Tempo medio:             %.3f ms%n", tempoMedio);
        System.out.printf("Desvio padrao:           %.3f ms%n", desvioPadrao);
        System.out.printf("Nos visitados (media):   %.0f%n",    mediaNos);
        System.out.printf("Backtracks (media):      %.0f%n",    mediaBacks);
        System.out.printf("Podas (media):           %.0f%n",    mediaPodas);
        System.out.printf("Memoria usada (media):   %.0f bytes%n", mediaMemoria);
        System.out.printf("Chamadas recursivas (media): %.0f%n", mediaRecursive);
        System.out.printf("Profundidade maxima (media): %.0f%n", mediaMaxDepth);
        System.out.printf("Taxa de sucesso:         %d/%d%n", solucoesEncontradas, EXECUCOES);
        if (tamanho >= 16) {
            long naoConcluidas = 0;
            for (int i = 0; i < EXECUCOES; i++) {
                if (nosVisitados[i] >= LIMITE_NOS_DIMENSOES_GRANDES) {
                    naoConcluidas++;
                }
            }
            System.out.printf("Nao concluidas:          %d/%d%n", naoConcluidas, EXECUCOES);
        }
    }

    private static double media(double[] array) {
        double soma = 0;
        for (double v : array) soma += v;
        return soma / array.length;
    }

    private static double desvio(double[] array, double media) {
        double soma = 0;
        for (double v : array) soma += Math.pow(v - media, 2);
        return Math.sqrt(soma / array.length);
    }

    private static double mediaLong(long[] array) {
        long soma = 0;
        for (long v : array) soma += v;
        return (double) soma / array.length;
    }
}