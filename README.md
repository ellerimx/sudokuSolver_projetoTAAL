# Sudoku Solver — Estudo Comparativo de Algoritmos de Busca

## Sobre o projeto

Repositório destinado a três implementações distintas para a resolução automática de tabuleiros de Sudoku, cada uma baseada em um paradigma clássico de projeto de algoritmos:

- Backtracking
- Branch and Bound
- Programação Dinâmica

Mais do que apenas resolver o sudoku, o projeto foi construído para permitir observar, na prática, como cada paradigma se comporta diante do mesmo problema,  quantos estados cada um precisa explorar, quanto tempo e memória consome, e em que ponto cada estratégia deixa de ser viável conforme a dificuldade do tabuleiro aumenta.

Os testes, comparações e gráficos usados na análise formal estão documentados nos relatório abaixo (necessário e-mail institucional para acesso):

- **Relatório Pt. 1:** 


## O que este projeto se propõe a responder

- Como implementar o mesmo problema sob paradigmas algorítmicos diferentes;
- Qual estratégia entrega melhor relação entre tempo/memória gastos e qualidade da solução;
- Até que ponto a teoria (complexidade assintótica) se confirma na prática (medições reais);
- O quanto podas e critérios de escolha de célula/valor reduzem o espaço de busca explorado.

## Antes de rodar

É necessário ter instalado:

- JDK 23 ou superior
- Apache Maven 3.6 ou superior
- Uma IDE de sua preferência (IntelliJ IDEA, VS Code, Eclipse etc.) — opcional, mas recomendado

## Preparando o ambiente

```bash
git clone https://github.com/ellerimx/sudokuSolver_projetoTAAL.git
cd sudokuSolver_projetoTAAL
mvn clean compile
```

## Como rodar

A aplicação roda por um menu interativo no terminal, disponibilizado pela classe `ExperimentRunner`. Pela IDE, basta executar essa classe; pela linha de comando:

```bash
mvn clean package
java -cp target/classes br.edu.sudoku.experiment.ExperimentRunner
```

### Interface gráfica

Para visualizar o tabuleiro e a resolução fora do terminal, existe uma interface gráfica simples (Swing) na classe `SudokuGUI`. Pela IDE, basta executá-la; pela linha de comando:

```bash
mvn clean package
java -cp target/classes br.edu.sudoku.gui.SudokuGUI
```

Nela é possível escolher o tamanho do tabuleiro, o algoritmo e a dificuldade, clicar em "Resolver" e ver o tabuleiro preenchido junto das métricas da execução. Para Backtracking e Branch and Bound em tabuleiros até 9x9, o passo a passo da busca (célula tentada, valor testado e backtracks) aparece direto no painel "Passo a passo" da interface, em vez de ser impresso no terminal.

**Capturas de tela:**

![Página inicial](src/sudoku%20pagina%20inicial.png)

| 4x4 | 9x9 |
|---|---|
| ![Sudoku 4x4](src/sudoku%204x4.png) | ![Sudoku 9x9](src/sudoku%209x9.png) |

| 16x16 | 25x25 |
|---|---|
| ![Sudoku 16x16](src/sudoku%2016x16.png) | ![Sudoku 25x25](src/sudoku%2025x25.png) |

O menu pede, em sequência, o algoritmo desejado e o nível de dificuldade do Sudoku. Terminada a execução, o console mostra o tabuleiro resolvido junto das métricas coletadas (tempo, memória, nós visitados, chamadas recursivas, backtracks, podas e profundidade máxima), e a solução é gravada em um novo arquivo dentro de `src/main/resources/sudokus/`.

### Chamando um solver diretamente pelo código

Todos os solvers implementam a interface `SudokuSolver`, então dá para usá-los fora do menu também:

```java
SudokuBoard board = SudokuReader.read("sudokus/sudoku_facil.txt");
Metrics metrics = new Metrics();

SudokuSolver solver = new BacktrackingSolver();
boolean resolvido = solver.solve(board, metrics);

if (resolvido) {
    SudokuWriter.printBoard(board);
}
```

### Registro dos resultados em CSV

A classe `ResultsExporter` grava cada execução em `src/main/resources/results/experiment_results.csv`. É esse arquivo que alimenta as tabelas e gráficos usados nos relatórios.

Quem efetivamente gera esse CSV é `PerformanceComparisonTest` (em `src/test/.../experiment/`): roda os três algoritmos por dificuldade (9x9) e por tamanho de tabuleiro (4x4, 9x9, 16x16, 25x25), 10 execuções cada, exportando tempo, memória, nós visitados, chamadas recursivas, backtracks, podas e profundidade máxima. É um programa com `main()`, não um `@Test`, então não roda com `mvn test` — precisa ser chamado direto:

```bash
mvn clean test-compile
java -cp "target/classes;target/test-classes" br.edu.sudoku.experiment.PerformanceComparisonTest
```

(no Linux/Mac, troque o `;` do classpath por `:`)

Para tabuleiros 16x16 e 25x25, o experimento aplica um limite de nós visitados e de tempo por execução — sem isso, o Branch and Bound (que recalcula o bound varrendo o tabuleiro inteiro a cada tentativa) pode não terminar em tempo hábil em instâncias médias/difíceis de 25x25. Quando o limite é atingido, a execução é registrada no CSV com `status_execucao = nao_concluido`, o que também é um resultado válido para a análise de escalabilidade (mostra o ponto em que cada algoritmo deixa de ser viável).

## Instâncias de Sudoku disponíveis

Ficam em `src/main/resources/sudokus/`:

| Arquivo | Dificuldade |
|---|---|
| `sudoku_facil.txt` | Fácil |
| `sudoku_medio.txt` | Média |
| `sudoku_dificil.txt` | Difícil |

Formato: matriz 9x9, valores separados por espaço, `0` representando célula vazia.

```
5 3 0 0 7 0 0 0 0
6 0 0 1 9 5 0 0 0
0 9 8 0 0 0 0 6 0
8 0 0 0 6 0 0 0 3
4 0 0 8 0 3 0 0 1
7 0 0 0 2 0 0 0 6
0 6 0 0 0 0 2 8 0
0 0 0 4 1 9 0 0 5
0 0 0 0 8 0 0 7 9
```

## Organização do código

```
src
├── main
│   ├── java/br/edu/sudoku
│   │   ├── experiment/          → ExperimentRunner (menu principal)
│   │   ├── io/                  → SudokuReader, SudokuWriter, ResultsExporter
│   │   ├── metrics/             → Metrics
│   │   ├── model/                → SudokuBoard
│   │   ├── solver/
│   │   │   ├── SudokuSolver.java (interface comum)
│   │   │   ├── backtracking/
│   │   │   ├── branchandbound/
│   │   │   └── dynamicprogramming/
│   │   └── utils/                → SudokuValidator
│   └── resources/
│       ├── results/              → experiment_results.csv
│       └── sudokus/              → instâncias de entrada e saídas geradas
└── test
    ├── java/br/edu/sudoku/       → testes unitários e de comparação
    └── resources/sudokus/        → instância usada nos testes
```

## O que cada parte faz

**Modelo** — `SudokuBoard` guarda o tabuleiro como matriz 9x9, controla quais células vieram fixas na instância original e cuida da impressão colorida no terminal.

**Solvers** — cada pacote dentro de `solver/` implementa `SudokuSolver` (`solve(board, metrics)`) com uma estratégia diferente:
- `BacktrackingSolver`: busca recursiva com retrocesso ao encontrar um estado inválido;
- `BranchAndBoundSolver`: busca com poda de ramos que não podem levar a uma solução viável;
- `DynamicProgrammingSolver`: resolução reaproveitando estados/subsoluções já computados.

**E/S** — `SudokuReader` lê as instâncias de `resources/sudokus`; `SudokuWriter` imprime no console e grava a solução em arquivo; `ResultsExporter` acumula as métricas de cada rodada no CSV de resultados.

**Métricas** — `Metrics` contabiliza tempo de execução, memória usada, nós visitados, chamadas recursivas, backtracks, podas (ramos descartados por inviabilidade, específico do Branch and Bound) e profundidade máxima atingida na busca.

**Validação** — `SudokuValidator` confere se um valor pode ocupar determinada célula sem ferir as regras de linha, coluna e bloco 3x3.

**Testes** — cobrem cada solver individualmente (`BacktrackingSolverTest`, `BranchAndBoundSolverTest`, `DynamicProgrammingSolverTest`), o validador (`SudokuValidatorTest`), comparações entre algoritmos (`SolverComparisonTest`, `PerformanceComparisonTest`) e checagens rápidas de desempenho (`QuickPerformanceTest`).

## Critérios usados na comparação

- Complexidade assintótica teórica (tempo e espaço, pior caso);
- Tempo de execução medido empiricamente;
- Memória consumida durante a resolução;
- Quantidade de nós explorados, de backtracks e de podas realizadas;
- Profundidade máxima atingida na árvore de busca;
- Comportamento de cada algoritmo ao passar de instâncias fáceis para difíceis (escalabilidade).

## Rodando os testes

```bash
mvn test                              # suíte completa
mvn test -Dtest=BacktrackingSolverTest  # um teste específico
```

Pelo IntelliJ: rode `clean` e depois `install` no painel Maven uma vez para preparar o projeto; depois disso, qualquer classe (principal ou de teste) pode ser executada direto pelo ícone ▶ ao lado dela.

## Licença

Distribuído sob a Licença MIT — detalhes em [`LICENSE`](./LICENSE).

Copyright (c) 2026 Adrielly Carla Ferreira de Melo, Mirelle Casimiro Silvino, Pedro Henrique da Silva Sales e Rui Fernando do Nascimento Filho.
