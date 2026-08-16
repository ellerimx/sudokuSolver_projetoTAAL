/**
 * Classe responsável por executar os "experimentos" do projeto
 * e coordenar o fluxo principal do sistema.
 */

package br.edu.sudoku.experiment;

import br.edu.sudoku.io.SudokuReader;
import br.edu.sudoku.io.SudokuWriter;
import br.edu.sudoku.metrics.Metrics;
import br.edu.sudoku.model.SudokuBoard;
import br.edu.sudoku.solver.backtracking.BacktrackingSolver;
import br.edu.sudoku.solver.branchandbound.BranchAndBoundSolver;
import br.edu.sudoku.solver.dynamicprogramming.DynamicProgrammingSolver;
import br.edu.sudoku.solver.greedy.GreedySolver;
import br.edu.sudoku.solver.SudokuSolver;

import java.util.Scanner;

public class ExperimentRunner {

    private static Integer lerOpcaoInteira(Scanner scanner) {
        if (!scanner.hasNextInt()) {
            String entradaInvalida = scanner.next();
            System.out.println("Entrada inválida: '" + entradaInvalida + "'. Digite um número do menu.");
            return null;
        }
        return scanner.nextInt();
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        while (true) {

            System.out.println("-------------------------------------------------------");
            System.out.println("███████╗██╗   ██╗██████╗  ██████╗ ██╗  ██╗██╗   ██╗");
            System.out.println("██╔════╝██║   ██║██╔══██╗██╔═══██╗██║ ██╔╝██║   ██║");
            System.out.println("███████╗██║   ██║██║  ██║██║   ██║█████╔╝ ██║   ██║");
            System.out.println("╚════██║██║   ██║██║  ██║██║   ██║██╔═██╗ ██║   ██║");
            System.out.println("███████║╚██████╔╝██████╔╝╚██████╔╝██║  ██╗╚██████╔╝");
            System.out.println("╚══════╝ ╚═════╝ ╚═════╝  ╚═════╝ ╚═╝  ╚═╝ ╚═════╝ ");
            System.out.println("                     SOLVER");
            System.out.println("-------------------------------------------------------");

            System.out.println("1 - Backtracking");
            System.out.println("2 - Branch and Bound");
            System.out.println("3 - Guloso (Greedy)");
            System.out.println("4 - Programação Dinâmica");
            System.out.println("0 - Sair");

            System.out.print("\nEscolha o algoritmo: ");
            Integer opcaoAlgoritmoLida = lerOpcaoInteira(scanner);
            if (opcaoAlgoritmoLida == null) {
                continue;
            }
            int opcaoAlgoritmo = opcaoAlgoritmoLida;

            if (opcaoAlgoritmo == 0) {
                System.out.println("Encerrando...");
                break;
            }

            if (opcaoAlgoritmo < 0 || opcaoAlgoritmo > 4) {
                System.out.println("Algoritmo inexistente. Escolha uma opção válida do menu.");
                continue;
            }

            System.out.println("-------------------------------------------------------");
            System.out.println("\nEscolha a dificuldade:");
            System.out.println("1 - Fácil");
            System.out.println("2 - Médio");
            System.out.println("3 - Difícil");
            System.out.println("0 - Voltar");

            System.out.print("Opção: ");
            Integer opcaoDificuldadeLida = lerOpcaoInteira(scanner);
            if (opcaoDificuldadeLida == null) {
                continue;
            }
            int opcaoDificuldade = opcaoDificuldadeLida;

            if (opcaoDificuldade == 0) {
                continue;
            }

            if (opcaoDificuldade < 0 || opcaoDificuldade > 3) {
                System.out.println("Dificuldade inválida. Escolha uma opção válida do menu.");
                continue;
            }

            String caminhoArquivo = "";
            String nomeDificuldade = "";
            String nomeDificuldadeArquivo = "";

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
                    System.out.println("Dificuldade inválida.");
                    continue;
            }

            try {

                SudokuBoard tabuleiro = SudokuReader.read(caminhoArquivo);

                SudokuSolver solver = null;
                String nomeAlgoritmoArquivo = "";
                String nomeAlgoritmo = "";

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
                        solver = new GreedySolver();
                        nomeAlgoritmoArquivo = "greedy";
                        nomeAlgoritmo = "GREEDY";
                        break;

                    case 4:
                        solver = new DynamicProgrammingSolver();
                        nomeAlgoritmoArquivo = "dynamicprogramming";
                        nomeAlgoritmo = "DYNAMIC PROGRAMMING";
                        break;

                    default:
                        System.out.println("Algoritmo inválido.");
                        continue;
                }

                System.out.println("\nAlgoritmo escolhido:");
                System.out.println("╔══════════════════════════════╗");
                System.out.println("        " + nomeAlgoritmo);
                System.out.println("╚══════════════════════════════╝");

                System.out.println("Dificuldade: " + nomeDificuldade);

                System.out.println("\nSudoku inicial:\n");
                SudokuWriter.printBoard(tabuleiro);

                Metrics metricas = new Metrics();

                System.setProperty("difficulty", nomeDificuldadeArquivo);
                System.setProperty("sudoku.difficulty", nomeDificuldadeArquivo);

                long inicio = System.currentTimeMillis();

                boolean resolvido = solver.solve(tabuleiro, metricas);

                long fim = System.currentTimeMillis();

                if (resolvido) {

                    System.out.println("\nSudoku resolvido:\n");
                    SudokuWriter.printBoard(tabuleiro);

                    String arquivoSaida =
                            "src/main/resources/sudokus/sudoku_" +
                                    nomeDificuldadeArquivo + "_" +
                                    nomeAlgoritmoArquivo + ".txt";

                    SudokuWriter.writeToFile(tabuleiro, arquivoSaida);

                    System.out.println("\n================ RESULTADOS ================");
                    System.out.println("Tempo de execução: " + (fim - inicio) + " ms");
                    System.out.println("Nós visitados: " + metricas.getVisitedNodes());
                    System.out.println("Backtracks: " + metricas.getBacktracks());
                    System.out.println("============================================");

                } else {

                    System.out.println("Não foi possível resolver o Sudoku.");
                }

            } catch (Exception e) {

                System.out.println("Erro ao carregar o arquivo.");
                e.printStackTrace();
            }

            System.out.println("\n");
        }

        scanner.close();
    }
}