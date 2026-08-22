# Sudoku Solver — Estudo Comparativo de Algoritmos de Busca

## Sobre o projeto

Repositório destinado a três implementações distintas para a resolução automática de tabuleiros de Sudoku, cada uma baseada em um paradigma clássico de projeto de algoritmos:
- **Backtracking**
- **Branch and Bound**
- **Programação Dinâmica**

O sistema permite executar e visualizar a resolução de tabuleiros de:
- **4x4**
- **9x9**
- **16x16**
- **25x25**

Cada tamanho possui instâncias classificadas em três níveis de dificuldade:
- **Fácil**
- **Médio**

Mais do que apenas resolver o sudoku, o projeto foi construído para permitir observar, na prática, como cada paradigma se comporta diante de instâncias de tamanhos diferentes e níveis de dificuldade,  quantos estados cada um precisa explorar, quanto tempo e memória consome, e em que ponto cada estratégia deixa de ser viável conforme a dificuldade do tabuleiro aumenta.

## Os testes, comparações e gráficos utilizados na análise experimental estão documentados no relatório completo. As versões em arquivo e online estão disponíveis nos links abaixo:

- **Relatório em PDF** (anexado na pasta "relatório" no repositório): [Acessar relatório](./relatório/Sudoku%20Solver%20-%20Relatório%20%28Projeto%20TAAL%29.pdf)
- **Google Docs:** [Acessar relatório online](https://docs.google.com/document/d/1MrSuCMAWIcCiZ21MSblVLjxp6qEWUvmyoUXNizrzokI/edit?usp=sharing)

---

## Objetivos do estudo experimental

A partir das implementações e dos experimentos realizados, o projeto busca analisar questões como:

- Qual algoritmo apresenta menor tempo de execução nas diferentes instâncias?
- Qual algoritmo consome menos memória?
- Como o desempenho dos algoritmos varia conforme o tamanho do tabuleiro aumenta?
- Em que tamanho de entrada determinado algoritmo se torna inviável?
- Como a dificuldade da instância influencia o desempenho?
- Existe diferença significativa entre a análise assintótica e o comportamento observado experimentalmente?
- Como as estratégias de busca e poda influenciam a quantidade de estados explorados?
- Em quais cenários uma estratégia consegue encontrar uma solução de forma mais eficiente?
- O custo computacional adicional de uma estratégia mais sofisticada compensa seus benefícios?

Dessa forma, os experimentos procuram relacionar a **análise teórica dos algoritmos** com seu **comportamento observado na prática**.

---
## Algoritmos implementados

### Backtracking

Realiza uma busca recursiva, preenchendo as células vazias do Sudoku e retornando a estados anteriores quando uma escolha leva a uma configuração inválida.

### Branch and Bound

Utiliza uma estratégia de busca com poda de ramos. Antes de continuar a exploração de determinado estado, é calculado um limite (`bound`) para verificar se aquele ramo ainda pode levar a uma solução viável.

### Programação Dinâmica

Utiliza memorização de estados já explorados para evitar a repetição de determinadas subsoluções durante a busca.

---

## Requisitos para rodar

É necessário ter instalado:

- JDK 23 ou superior
- Apache Maven 3.6 ou superior
- Uma IDE de sua preferência (IntelliJ IDEA, VS Code, Eclipse etc.) — opcional, mas recomendado

Para verificar se Java e Maven estão instalados:

```bash
java -version
mvn -version
````

---

## Preparando o ambiente
1. Clone o repositório no terminal ou CMD:
```bash
git clone https://github.com/ellerimx/sudokuSolver_projetoTAAL.git
```
Depois entre na pasta do projeto
```bash
cd sudokuSolver_projetoTAAL
```
2. Compile o projeto
```bash 
mvn clean compile
```
Com a compilação terminada e sem erros, o projeto está pronto para ser executado.

## Como rodar

A aplicação roda por um menu interativo no terminal, disponibilizado pela classe `ExperimentRunner`. Pela IDE, basta executar essa classe; pela linha de comando:

```bash
mvn clean package
java -cp target/classes br.edu.sudoku.experiment.ExperimentRunner
```

## Interface gráfica

Para visualizar o tabuleiro e a resolução fora do terminal, existe uma interface gráfica simples (Swing) na classe `SudokuGUI`. Pela IDE, basta executá-la; pela linha de comando:

```bash
mvn clean package
java -cp target/classes br.edu.sudoku.gui.SudokuGUI
```

Nela é possível escolher o tamanho do tabuleiro, o algoritmo e a dificuldade, clicar em "Resolver" e ver o tabuleiro preenchido junto das métricas da execução. Para Backtracking e Branch and Bound em tabuleiros de 4x4, 9x9, 16x16 e 25x25, o passo a passo da busca (célula tentada, valor testado e backtracks) aparece direto no painel "Passo a passo" da interface, em vez de ser impresso no terminal.

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

## Rodando os testes

```bash
mvn test                              # suíte completa
mvn test -Dtest=BacktrackingSolverTest  # um teste específico
```

Pelo IntelliJ: rode `clean` e depois `install` no painel Maven uma vez para preparar o projeto; depois disso, qualquer classe (principal ou de teste) pode ser executada direto pelo ícone ▶ ao lado dela.

---

### Registro dos resultados em CSV

A classe `ResultsExporter` grava cada execução em `src/main/resources/results/experiment_results.csv`. É esse arquivo que alimenta as tabelas e gráficos usados nos relatórios.

Quem efetivamente gera esse CSV é `PerformanceComparisonTest` (em `src/test/.../experiment/`): roda os três algoritmos por dificuldade e por tamanho de tabuleiro (4x4, 9x9, 16x16, 25x25), 10 execuções cada, exportando tempo, memória, nós visitados, chamadas recursivas, backtracks, podas e profundidade máxima. É um programa com `main()`, não um `@Test`, então não roda com `mvn test` — precisa ser chamado direto:

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
| `sudoku_9x9_facil.txt` | Fácil |
| `sudoku_9x9_medio.txt` | Média |
| `sudoku_9x9_dificil.txt` | Difícil |

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

---

## O que cada parte faz

**Modelo** — `SudokuBoard` epresenta o tabuleiro e fornece operações para acessar e modificar suas células. A implementação é genérica em relação ao tamanho do tabuleiro, permitindo trabalhar com 4x4, 9x9, 16x16 e 25x25.

**Solvers** — cada pacote dentro de `solver/` implementa `SudokuSolver` (`solve(board, metrics)`) com uma estratégia diferente:
- `BacktrackingSolver`: busca recursiva com retrocesso ao encontrar um estado inválido;
- `BranchAndBoundSolver`: busca com poda de ramos que não podem levar a uma solução viável;
- `DynamicProgrammingSolver`: resolução reaproveitando estados/subsoluções já computados.

**E/S** — `SudokuReader` lê as instâncias de `resources/sudokus`; `SudokuWriter` imprime no console e grava a solução em arquivo; `ResultsExporter` acumula as métricas de cada rodada no CSV de resultados.

**Métricas** — `Metrics` registra informações utilizadas para analisar o comportamento dos algoritmos, incluindo: nós visitados; chamadas recursivas; backtracks; podas; memória utilizada; profundidade máxima. O tempo de execução é medido durante os experimentos e registrado nos resultados.

**Validação** — `SudokuValidator` verifica se determinado valor pode ser inserido em uma célula sem violar as regras de linha, coluna e bloco do Sudoku.

**Interface gráfica** — `SudokuGUI` fornece uma interface Swing para seleção da instância, algoritmo e dificuldade, além da visualização da resolução.

**Testes** — cobrem cada solver individualmente (`BacktrackingSolverTest`, `BranchAndBoundSolverTest`, `DynamicProgrammingSolverTest`), o validador (`SudokuValidatorTest`), comparações entre algoritmos (`SolverComparisonTest`, `PerformanceComparisonTest`) e checagens rápidas de desempenho (`QuickPerformanceTest`).

---

## Critérios usados na comparação
Os algoritmos são analisados considerando:

- Tempo de execução;
- Memória utilizada;
- Quantidade de nós visitados;
- Quantidade de chamadas recursivas;
- Quantidade de backtracks;
- Quantidade de podas;
- Profundidade máxima da busca;
- Taxa de conclusão das instâncias;
- Crescimento do custo computacional conforme o tamanho da entrada aumenta;
- Diferença entre o comportamento teórico e o comportamento observado experimentalmente.

A análise considera tanto a dificuldade das instâncias quanto o tamanho dos tabuleiros, permitindo observar diferentes aspectos do comportamento dos algoritmos.

---

## Licença

Distribuído sob a Licença MIT — detalhes em [`LICENSE`](./LICENSE).

Copyright (c) 2026 Adrielly Carla Ferreira de Melo, Mirelle Casimiro Silvino, Pedro Henrique da Silva Sales e Rui Fernando do Nascimento Filho.
