/*
 Classe responsável por executar os "experimentos" do projeto
 e coordenar o fluxo principal do sistema.
 */

package br.edu.sudoku.experiment;

import br.edu.sudoku.io.SudokuReader;
import br.edu.sudoku.io.SudokuWriter;
import br.edu.sudoku.metrics.Metrics;
import br.edu.sudoku.model.SudokuBoard;
import br.edu.sudoku.solver.backtracking.BacktrackingSolver;
import br.edu.sudoku.solver.branchandbound.BranchAndBoundSolver;
import br.edu.sudoku.solver.dynamicprogramming.DynamicProgrammingSolver;
import br.edu.sudoku.solver.SudokuSolver;

import java.util.Scanner;

public class ExperimentRunner {

    private static Integer lerOpcaoInteira(Scanner scanner) {

        if (!scanner.hasNextInt()) {
            String entradaInvalida = scanner.next();
            System.out.println(
                    "Entrada inválida: '" + entradaInvalida +
                    "'. Digite um número do menu."
            );
            return null;
        }

        return scanner.nextInt();
    }

    // cabeçalho principal do sistema
    private static void mostrarCabecalho() {
        System.out.println();
        System.out.println("-------------------------------------------------------");
        System.out.println("███████╗██╗   ██╗██████╗  ██████╗ ██╗  ██╗██╗   ██╗");
        System.out.println("██╔════╝██║   ██║██╔══██╗██╔═══██╗██║ ██╔╝██║   ██║");
        System.out.println("███████╗██║   ██║██║  ██║██║   ██║█████╔╝ ██║   ██║");
        System.out.println("╚════██║██║   ██║██║  ██║██║   ██║██╔═██╗ ██║   ██║");
        System.out.println("███████║╚██████╔╝██████╔╝╚██████╔╝██║  ██╗╚██████╔╝");
        System.out.println("╚══════╝ ╚═════╝ ╚═════╝  ╚═════╝ ╚═╝  ╚═╝ ╚═════╝");
        System.out.println("                     SOLVER");
        System.out.println("-------------------------------------------------------");
    }

    // algoritmos disponíveis para o usuário escolher
    private static void mostrarMenuAlgoritmos() {
        System.out.println();
        System.out.println("ALGORITMOS DISPONÍVEIS");
        System.out.println("-------------------------------------------------------");
        System.out.println("1 - Backtracking");
        System.out.println("2 - Branch and Bound");
        System.out.println("3 - Programação Dinâmica");
        System.out.println("0 - Sair");
        System.out.println("-------------------------------------------------------");
    }

    // exibição do menu de dificuldade do Sudoku
    private static void mostrarMenuDificuldade() {
        System.out.println();
        System.out.println("DIFICULDADE DO SUDOKU");
        System.out.println("-------------------------------------------------------");
        System.out.println("1 - Fácil");
        System.out.println("2 - Médio");
        System.out.println("3 - Difícil");
        System.out.println("0 - Voltar");
        System.out.println("-------------------------------------------------------");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            mostrarCabecalho();
            mostrarMenuAlgoritmos();
            System.out.print("Escolha o algoritmo: ");
            Integer opcaoAlgoritmoLida = lerOpcaoInteira(scanner);
            if (opcaoAlgoritmoLida == null) {
                continue;
            }

            int opcaoAlgoritmo = opcaoAlgoritmoLida;

            // sair
            if (opcaoAlgoritmo == 0) {
                System.out.println("\nEncerrando o Solver...");
                break;
            }

            // validar algoritmo
            if (opcaoAlgoritmo < 1 || opcaoAlgoritmo > 4) {
                System.out.println(
                        "\nAlgoritmo inexistente. " +
                        "Escolha uma opção válida."
                );
                continue;
            }

            mostrarMenuDificuldade();
            System.out.print("Escolha a dificuldade: ");

            Integer opcaoDificuldadeLida = lerOpcaoInteira(scanner);

            if (opcaoDificuldadeLida == null) {
                continue;}

            int opcaoDificuldade = opcaoDificuldadeLida;

            // volta ao menu de algoritmos
            if (opcaoDificuldade == 0) {
                continue;}

            // valida dificuldade escolhida
            if (opcaoDificuldade < 1 || opcaoDificuldade > 3) {
                System.out.println("\nDificuldade inválida. " + "Escolha uma opção válida.");
                continue;
            }

            String caminhoArquivo;
            String nomeDificuldade;
            String nomeDificuldadeArquivo;

            switch (opcaoDificuldade) {
                case 1:
                    caminhoArquivo = "sudokus/sudoku_facil.txt";
                    nomeDificuldade = "Fácil";
                    nomeDificuldadeArquivo = "facil";
                    break;

                case 2:
                    caminhoArquivo = "sudokus/sudoku_medio.txt";
                    nomeDificuldade = "Médio";
                    nomeDificuldadeArquivo = "medio";
                    break;

                case 3:
                    caminhoArquivo = "sudokus/sudoku_dificil.txt";
                    nomeDificuldade = "Difícil";
                    nomeDificuldadeArquivo = "dificil";
                    break;

                default:
                    continue;
            }

            try {
                // carrega o sudoku correspondente a dificuldade escolhida
                SudokuBoard tabuleiro = SudokuReader.read(caminhoArquivo);

                SudokuSolver solver;
                String nomeAlgoritmoArquivo;
                String nomeAlgoritmo;

                // seleção do algoritmo
                switch (opcaoAlgoritmo) {
                    case 1:
                        solver = new BacktrackingSolver();
                        nomeAlgoritmoArquivo = "backtracking";
                        nomeAlgoritmo = "BACKTRACKING";
                        break;
                    case 2:
                        solver = new BranchAndBoundSolver();
                        nomeAlgoritmoArquivo = "branchandbound";
                        nomeAlgoritmo = "BRANCH AND BOUND";
                        break;
                    case 3:
                        solver = new DynamicProgrammingSolver();
                        nomeAlgoritmoArquivo = "dynamicprogramming";
                        nomeAlgoritmo = "DYNAMIC PROGRAMMING";
                        break;

                    default:
                        System.out.println("Algoritmo inválido.");
                        continue;
                }

                // inicialização do experimento
                System.out.println();
                System.out.println("╔══════════════════════════════════════╗");
                System.out.println("║          EXPERIMENTO INICIADO       ║");
                System.out.println("╠══════════════════════════════════════╣");
                System.out.printf("║ Algoritmo: %-25s ║%n",nomeAlgoritmo);
                System.out.printf("║ Dificuldade: %-23s ║%n",nomeDificuldade);
                System.out.println("╚══════════════════════════════════════╝");

                
                System.out.println("\nSudoku inicial:\n");
                SudokuWriter.printBoard(tabuleiro);

               // métricas para o experimento
                Metrics metricas = new Metrics();

                System.setProperty("difficulty",nomeDificuldadeArquivo);

                System.setProperty("sudoku.difficulty",nomeDificuldadeArquivo);

                //execução do algoritmo escolhido
                long memAntes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

                long inicio = System.currentTimeMillis();

                boolean resolvido = solver.solve(tabuleiro, metricas);

                long fim = System.currentTimeMillis();

                long memDepois = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

                long memUsada = memDepois - memAntes;

                metricas.setMemoryUsedBytes(memUsada);

                // resultados do experimento                
                if (resolvido) {

                    System.out.println("\nSudoku resolvido:\n");
                    SudokuWriter.printBoard(tabuleiro);

                    String arquivoSaida =
                            "src/main/resources/sudokus/sudoku_"
                            + nomeDificuldadeArquivo
                            + "_"
                            + nomeAlgoritmoArquivo
                            + ".txt";

                    SudokuWriter.writeToFile(tabuleiro,arquivoSaida);

                    System.out.println();
                    System.out.println(
                            "╔══════════════════════════════════════╗"
                    );
                    System.out.println(
                            "║             RESULTADOS              ║"
                    );
                    System.out.println(
                            "╠══════════════════════════════════════╣"
                    );

                    System.out.println("║ Tempo de execução: "+ (fim - inicio)+ " ms");

                    System.out.println("║ Memória usada: "+ memUsada+ " bytes");

                    System.out.println("║ Nós visitados: "+ metricas.getVisitedNodes());

                    System.out.println("║ Chamadas recursivas: "+ metricas.getRecursiveCalls());

                    System.out.println("║ Backtracks: "+ metricas.getBacktracks());

                    System.out.println("║ Podas: "+ metricas.getPrunes());

                    System.out.println("║ Profundidade máxima: "+ metricas.getMaxDepth());

                    System.out.println("╚══════════════════════════════════════╝");
                    

                    System.out.println("\nResultado salvo em:");

                    System.out.println(arquivoSaida);

                } else {
                    System.out.println("\nNão foi possível resolver o Sudoku.");
                }

            } catch (Exception e) {

                System.out.println("\nErro ao carregar ou executar o experimento.");

                e.printStackTrace();
            }

            System.out.println("\nPressione ENTER para continuar...");
            scanner.nextLine();
            scanner.nextLine();
        }
        scanner.close();
    }
}