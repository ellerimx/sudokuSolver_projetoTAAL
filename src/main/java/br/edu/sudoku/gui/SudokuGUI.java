/**
 * Interface gráfica simples para visualizar a resolução do Sudoku fora do terminal.
 */

package br.edu.sudoku.gui;

import br.edu.sudoku.io.SudokuReader;
import br.edu.sudoku.metrics.Metrics;
import br.edu.sudoku.model.SudokuBoard;
import br.edu.sudoku.solver.PassoObservador;
import br.edu.sudoku.solver.SudokuSolver;
import br.edu.sudoku.solver.backtracking.BacktrackingSolver;
import br.edu.sudoku.solver.branchandbound.BranchAndBoundSolver;
import br.edu.sudoku.solver.dynamicprogramming.DynamicProgrammingSolver;

import javax.swing.*;
import java.awt.*;

public class SudokuGUI extends JFrame {

    private int size = 9;

    private static final Color FUNDO = Color.BLACK;
    private static final Color FUNDO_CELULA = new Color(20, 20, 20);
    private static final Color TEXTO = Color.WHITE;
    private static final Color TEXTO_FIXO = new Color(80, 160, 255);
    private static final Color TEXTO_RESOLVIDO = new Color(90, 220, 120);
    private static final Color BORDA = new Color(90, 90, 90);
    private static final Color DESTAQUE = new Color(0, 120, 215);
    private static final Color DESTAQUE_HOVER = new Color(30, 150, 245);
    private static final Color DESTAQUE_PASSO = new Color(255, 200, 0);
    private static final Color DOURADO_LETREIRO = new Color(255, 199, 44);

    private static final int MAX_LINHAS_PASSO = 300;
    private static final long LIMITE_TEMPO_RESOLUCAO_MS = 8000;
    private static final int LIMITE_TAMANHO_PASSO_A_PASSO = 9;

    private JTextField[][] celulas = new JTextField[size][size];
    private boolean[][] fixasIniciais = new boolean[size][size];

    private final JComboBox<String> comboTamanho = new JComboBox<>(
            new String[]{"4x4", "9x9", "16x16", "25x25"});

    private final JComboBox<String> comboAlgoritmo = new JComboBox<>(
            new String[]{"Backtracking", "Branch and Bound", "Programação Dinâmica"});

    private final JComboBox<String> comboDificuldade = new JComboBox<>(
            new String[]{"Fácil", "Médio", "Difícil"});

    private final JPanel gradeContainer = new JPanel(new BorderLayout());

    private final JLabel status = new JLabel("Selecione o algoritmo e a dificuldade, depois clique em Resolver.");
    private final JTextArea metricasArea = new JTextArea(4, 20);
    private final JTextArea passoArea = new JTextArea(10, 22);

    private final JButton botaoResolver = criarBotao("Resolver", 13);
    private final JButton botaoReiniciar = criarBotao("Reiniciar", 13);

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cartas = new JPanel(cardLayout);

    private static final String CARTA_INICIO = "inicio";
    private static final String CARTA_SOLVER = "solver";

    public SudokuGUI() {
        super("Sudoku Solver");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cartas.setBackground(FUNDO);
        cartas.add(criarTelaInicial(), CARTA_INICIO);
        cartas.add(criarTelaSolver(), CARTA_SOLVER);
        cardLayout.show(cartas, CARTA_INICIO);

        add(cartas);

        carregarTabuleiro();

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private JButton criarBotao(String texto, int tamanhoFonte) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font(Font.SANS_SERIF, Font.BOLD, tamanhoFonte));
        botao.setForeground(Color.WHITE);
        botao.setBackground(DESTAQUE);
        botao.setOpaque(true);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));

        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                botao.setBackground(DESTAQUE_HOVER);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                botao.setBackground(DESTAQUE);
            }
        });

        return botao;
    }

    private void estilizarCombo(JComboBox<String> combo) {
        combo.setBackground(FUNDO_CELULA);
        combo.setForeground(TEXTO);
        combo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        combo.setFocusable(false);
        combo.setBorder(BorderFactory.createLineBorder(BORDA));
    }

    /**
     * Monta um "letreiro" no estilo cartaz de cinema: fonte serifada, letras
     * douradas com contorno escuro e sombra projetada, espaçamento entre
     * caracteres para dar aquele efeito de título de filme antigo.
     */
    private JComponent criarLetreiroCinema(String texto, int tamanhoFonte) {
        Font fonte = new Font(Font.SERIF, Font.BOLD, tamanhoFonte);
        int espacamento = tamanhoFonte / 8;

        JComponent letreiro = new JComponent() {
            @Override
            public Dimension getPreferredSize() {
                FontMetrics fm = getFontMetrics(fonte);
                return new Dimension(larguraComEspacamento(fm, texto, espacamento) + 16, fm.getHeight() + 16);
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setFont(fonte);

                FontMetrics fm = g2.getFontMetrics();
                int largura = larguraComEspacamento(fm, texto, espacamento);
                int baseline = (getHeight() + fm.getAscent()) / 2 - fm.getDescent() / 2;
                int cursorX = (getWidth() - largura) / 2;

                for (char c : texto.toCharArray()) {
                    String s = String.valueOf(c);

                    g2.setColor(new Color(0, 0, 0, 150));
                    g2.drawString(s, cursorX + 4, baseline + 4);

                    g2.setColor(Color.BLACK);
                    g2.drawString(s, cursorX - 1, baseline);
                    g2.drawString(s, cursorX + 1, baseline);
                    g2.drawString(s, cursorX, baseline - 1);
                    g2.drawString(s, cursorX, baseline + 1);

                    g2.setColor(DOURADO_LETREIRO);
                    g2.drawString(s, cursorX, baseline);

                    cursorX += fm.charWidth(c) + espacamento;
                }

                g2.dispose();
            }
        };

        letreiro.setOpaque(false);
        return letreiro;
    }

    private int larguraComEspacamento(FontMetrics fm, String texto, int espacamento) {
        int largura = 0;
        for (char c : texto.toCharArray()) {
            largura += fm.charWidth(c) + espacamento;
        }
        return largura - espacamento;
    }

    private JPanel criarTelaInicial() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(FUNDO);
        painel.setPreferredSize(new Dimension(560, 480));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        JComponent letreiro = criarLetreiroCinema("SUDOKU", 64);

        JLabel subtitulo = new JLabel("SOLVER", SwingConstants.CENTER);
        subtitulo.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        subtitulo.setForeground(TEXTO_FIXO);

        JButton botaoComecar = criarBotao("Começar", 16);
        botaoComecar.addActionListener(e -> cardLayout.show(cartas, CARTA_SOLVER));

        gbc.gridy = 0;
        painel.add(letreiro, gbc);

        gbc.gridy = 1;
        painel.add(subtitulo, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(40, 0, 0, 0);
        painel.add(botaoComecar, gbc);

        return painel;
    }

    private JPanel criarTelaSolver() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBackground(FUNDO);
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painel.add(criarPainelTopo(), BorderLayout.NORTH);
        gradeContainer.setBackground(FUNDO);
        reconstruirGrade();
        painel.add(gradeContainer, BorderLayout.CENTER);
        painel.add(criarPainelLateral(), BorderLayout.EAST);
        painel.add(status, BorderLayout.SOUTH);

        status.setForeground(TEXTO);

        return painel;
    }

    private JPanel criarPainelTopo() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painel.setBackground(FUNDO);

        JLabel labelTamanho = new JLabel("Tamanho:");
        labelTamanho.setForeground(TEXTO);
        painel.add(labelTamanho);
        estilizarCombo(comboTamanho);
        comboTamanho.setSelectedIndex(1);
        painel.add(comboTamanho);
        comboTamanho.addActionListener(e -> aoMudarTamanho());

        JLabel labelAlgoritmo = new JLabel("Algoritmo:");
        labelAlgoritmo.setForeground(TEXTO);
        painel.add(labelAlgoritmo);
        estilizarCombo(comboAlgoritmo);
        painel.add(comboAlgoritmo);

        JLabel labelDificuldade = new JLabel("Dificuldade:");
        labelDificuldade.setForeground(TEXTO);
        painel.add(labelDificuldade);
        estilizarCombo(comboDificuldade);
        painel.add(comboDificuldade);
        comboDificuldade.addActionListener(e -> carregarTabuleiro());

        botaoResolver.addActionListener(e -> resolver());
        painel.add(botaoResolver);

        botaoReiniciar.addActionListener(e -> carregarTabuleiro());
        painel.add(botaoReiniciar);

        return painel;
    }

    private int tamanhoCelulaPx() {
        if (size <= 4) return 60;
        if (size <= 9) return 40;
        if (size <= 16) return 30;
        return 24;
    }

    private int tamanhoFontePx() {
        if (size <= 4) return 22;
        if (size <= 9) return 18;
        if (size <= 16) return 14;
        return 11;
    }

    private void reconstruirGrade() {
        int boxSize = (int) Math.round(Math.sqrt(size));

        celulas = new JTextField[size][size];
        fixasIniciais = new boolean[size][size];

        JPanel grade = new JPanel(new GridLayout(size, size));
        grade.setBackground(FUNDO);
        grade.setBorder(BorderFactory.createLineBorder(BORDA, 2));

        Font fonte = new Font(Font.SANS_SERIF, Font.BOLD, tamanhoFontePx());
        int celulaPx = tamanhoCelulaPx();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                JTextField campo = new JTextField();
                campo.setHorizontalAlignment(JTextField.CENTER);
                campo.setFont(fonte);
                campo.setPreferredSize(new Dimension(celulaPx, celulaPx));
                campo.setEditable(false);
                campo.setBackground(FUNDO_CELULA);
                campo.setForeground(TEXTO);
                campo.setDisabledTextColor(TEXTO);
                campo.setCaretColor(TEXTO);

                int top = 1;
                int left = 1;
                int bottom = (i % boxSize == boxSize - 1) ? 3 : 1;
                int right = (j % boxSize == boxSize - 1) ? 3 : 1;
                campo.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, BORDA));

                celulas[i][j] = campo;
                grade.add(campo);
            }
        }

        gradeContainer.removeAll();
        gradeContainer.add(grade, BorderLayout.CENTER);
        gradeContainer.revalidate();
        gradeContainer.repaint();
    }

    private JPanel criarPainelLateral() {
        JPanel painel = new JPanel(new BorderLayout(0, 10));
        painel.setBackground(FUNDO);
        painel.setPreferredSize(new Dimension(260, 0));

        painel.add(criarPainelPassoAPasso(), BorderLayout.CENTER);
        painel.add(criarPainelMetricas(), BorderLayout.SOUTH);

        return painel;
    }

    private JPanel criarPainelPassoAPasso() {
        JPanel painel = new JPanel(new BorderLayout(0, 4));
        painel.setBackground(FUNDO);

        JLabel labelPasso = new JLabel("Passo a passo");
        labelPasso.setForeground(TEXTO);
        painel.add(labelPasso, BorderLayout.NORTH);

        passoArea.setEditable(false);
        passoArea.setLineWrap(true);
        passoArea.setWrapStyleWord(true);
        passoArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        passoArea.setBackground(FUNDO_CELULA);
        passoArea.setForeground(TEXTO);
        passoArea.setCaretColor(TEXTO);

        JScrollPane scroll = new JScrollPane(passoArea);
        scroll.getViewport().setBackground(FUNDO_CELULA);
        painel.add(scroll, BorderLayout.CENTER);

        return painel;
    }

    private JPanel criarPainelMetricas() {
        JPanel painel = new JPanel(new BorderLayout(0, 4));
        painel.setBackground(FUNDO);

        JLabel labelMetricas = new JLabel("Métricas");
        labelMetricas.setForeground(TEXTO);
        painel.add(labelMetricas, BorderLayout.NORTH);

        metricasArea.setEditable(false);
        metricasArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        metricasArea.setBackground(FUNDO_CELULA);
        metricasArea.setForeground(TEXTO);
        metricasArea.setCaretColor(TEXTO);

        JScrollPane scroll = new JScrollPane(metricasArea);
        scroll.getViewport().setBackground(FUNDO_CELULA);
        painel.add(scroll, BorderLayout.CENTER);

        return painel;
    }

    private void registrarPasso(String texto) {
        passoArea.append(texto + "\n");

        int excesso = passoArea.getLineCount() - MAX_LINHAS_PASSO;
        if (excesso > 0) {
            try {
                int fim = passoArea.getLineEndOffset(excesso - 1);
                passoArea.replaceRange("", 0, fim);
            } catch (Exception ignorada) {
                // mantém o texto como está caso os offsets fiquem inválidos
            }
        }

        passoArea.setCaretPosition(passoArea.getDocument().getLength());
    }

    private void atualizarGradeComSnapshot(int[][] snapshot, int linhaAtual, int colunaAtual) {
        int boxSize = (int) Math.round(Math.sqrt(size));

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                JTextField campo = celulas[i][j];
                int valor = snapshot[i][j];

                campo.setText(valor == 0 ? "" : String.valueOf(valor));
                campo.setForeground(fixasIniciais[i][j] ? TEXTO_FIXO : TEXTO);

                boolean atual = i == linhaAtual && j == colunaAtual;
                campo.setBackground(atual ? new Color(60, 50, 0) : FUNDO_CELULA);

                int top = 1;
                int left = 1;
                int bottom = (i % boxSize == boxSize - 1) ? 3 : 1;
                int right = (j % boxSize == boxSize - 1) ? 3 : 1;
                Color corBorda = atual ? DESTAQUE_PASSO : BORDA;
                campo.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, corBorda));
            }
        }
    }

    private String caminhoArquivoSelecionado() {
        int dificuldadeIdx = comboDificuldade.getSelectedIndex();

        String prefixo;
        switch (comboTamanho.getSelectedIndex()) {
            case 0: prefixo = "sudoku_4x4"; break;
            case 1: prefixo = "sudoku"; break;
            case 2: prefixo = "sudoku_16x16"; break;
            case 3: prefixo = "sudoku_25x25"; break;
            default: throw new IllegalStateException("Tamanho inválido.");
        }

        if (comboTamanho.getSelectedIndex() == 1) {
            switch (dificuldadeIdx) {
                case 0: return "sudokus/sudoku_facil.txt";
                case 1: return "sudokus/sudoku_medio.txt";
                case 2: return "sudokus/sudoku_dificil.txt";
                default: throw new IllegalStateException("Dificuldade inválida.");
            }
        }

        switch (dificuldadeIdx) {
            case 0: return "sudokus/" + prefixo + ".txt";
            case 1: return "sudokus/" + prefixo + "_medio.txt";
            case 2: return "sudokus/" + prefixo + "_dificil.txt";
            default: throw new IllegalStateException("Dificuldade inválida.");
        }
    }

    private void aoMudarTamanho() {
        switch (comboTamanho.getSelectedIndex()) {
            case 0: size = 4; break;
            case 1: size = 9; break;
            case 2: size = 16; break;
            case 3: size = 25; break;
            default: throw new IllegalStateException("Tamanho inválido.");
        }

        reconstruirGrade();
        carregarTabuleiro();
        pack();
        setLocationRelativeTo(null);
    }

    private void carregarTabuleiro() {
        try {
            SudokuBoard tabuleiro = SudokuReader.read(caminhoArquivoSelecionado());
            metricasArea.setText("");
            passoArea.setText("");
            status.setText("Tabuleiro carregado. Clique em Resolver.");

            int boxSize = (int) Math.round(Math.sqrt(size));

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    int valor = tabuleiro.get(i, j);
                    boolean fixa = valor != 0;
                    fixasIniciais[i][j] = fixa;

                    JTextField campo = celulas[i][j];
                    campo.setText(fixa ? String.valueOf(valor) : "");
                    campo.setForeground(fixa ? TEXTO_FIXO : TEXTO);
                    campo.setBackground(FUNDO_CELULA);

                    int top = 1;
                    int left = 1;
                    int bottom = (i % boxSize == boxSize - 1) ? 3 : 1;
                    int right = (j % boxSize == boxSize - 1) ? 3 : 1;
                    campo.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, BORDA));
                }
            }
        } catch (Exception e) {
            status.setText("Erro ao carregar o Sudoku: " + e.getMessage());
        }
    }

    private SudokuSolver criarSolverSelecionado(PassoObservador observador) {
        switch (comboAlgoritmo.getSelectedIndex()) {
            case 0: return new BacktrackingSolver(observador);
            case 1: return new BranchAndBoundSolver(observador);
            case 2: return new DynamicProgrammingSolver();
            default: throw new IllegalStateException("Algoritmo inválido.");
        }
    }

    private void resolver() {
        status.setText("Resolvendo...");
        passoArea.setText("");
        botaoResolver.setEnabled(false);
        botaoReiniciar.setEnabled(false);

        boolean mostraPassoAPasso = comboAlgoritmo.getSelectedIndex() != 2 && size <= LIMITE_TAMANHO_PASSO_A_PASSO;

        if (!mostraPassoAPasso) {
            registrarPasso(comboAlgoritmo.getSelectedIndex() == 2
                    ? "Programação Dinâmica resolve diretamente, sem passo a passo visual."
                    : "Tabuleiro " + size + "x" + size + " resolve diretamente, sem passo a passo visual (animação passo a passo é impraticável nesse tamanho).");
        }

        PassoObservador observador = !mostraPassoAPasso ? null : (tabuleiroAtual, passo, descricao, linha, coluna, backtrack) -> {
            int[][] snapshot = new int[size][size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    snapshot[i][j] = tabuleiroAtual.get(i, j);
                }
            }

            SwingUtilities.invokeLater(() -> {
                atualizarGradeComSnapshot(snapshot, linha, coluna);
                registrarPasso("[" + passo + "] " + descricao);
            });
        };

        SwingWorker<Boolean, Void> tarefa = new SwingWorker<>() {
            SudokuBoard tabuleiro;
            Metrics metricas;
            long tempoMs;
            boolean tempoEsgotado;

            @Override
            protected Boolean doInBackground() throws Exception {
                tabuleiro = SudokuReader.read(caminhoArquivoSelecionado());
                metricas = new Metrics();
                if (!mostraPassoAPasso) {
                    metricas.setTimeLimitMillis(LIMITE_TEMPO_RESOLUCAO_MS);
                }
                SudokuSolver solver = criarSolverSelecionado(observador);

                long inicio = System.currentTimeMillis();
                boolean resolvido;
                try {
                    resolvido = solver.solve(tabuleiro, metricas);
                } catch (Metrics.VisitLimitReachedException e) {
                    resolvido = false;
                    tempoEsgotado = true;
                }
                tempoMs = System.currentTimeMillis() - inicio;

                return resolvido;
            }

            @Override
            protected void done() {
                try {
                    boolean resolvido = get();

                    int boxSize = (int) Math.round(Math.sqrt(size));

                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < size; j++) {
                            JTextField campo = celulas[i][j];
                            int valor = tabuleiro.get(i, j);
                            campo.setText(valor == 0 ? "" : String.valueOf(valor));
                            campo.setBackground(FUNDO_CELULA);

                            if (fixasIniciais[i][j]) {
                                campo.setForeground(TEXTO_FIXO);
                            } else {
                                campo.setForeground(TEXTO_RESOLVIDO);
                            }

                            int top = 1;
                            int left = 1;
                            int bottom = (i % boxSize == boxSize - 1) ? 3 : 1;
                            int right = (j % boxSize == boxSize - 1) ? 3 : 1;
                            campo.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, BORDA));
                        }
                    }

                    if (resolvido) {
                        status.setText("Sudoku resolvido com sucesso.");
                        registrarPasso("Sudoku resolvido em " + tempoMs + " ms.");
                        metricasArea.setText(
                                "Tempo: " + tempoMs + " ms\n" +
                                "Memória: " + metricas.getMemoryUsedBytes() + " bytes\n" +
                                "Nós visitados: " + metricas.getVisitedNodes() + "\n" +
                                "Chamadas recursivas: " + metricas.getRecursiveCalls() + "\n" +
                                "Backtracks: " + metricas.getBacktracks() + "\n" +
                                "Podas: " + metricas.getPrunes() + "\n" +
                                "Profundidade máxima: " + metricas.getMaxDepth());
                    } else if (tempoEsgotado) {
                        status.setText("Tempo limite (" + (LIMITE_TEMPO_RESOLUCAO_MS / 1000) + "s) esgotado antes de resolver.");
                        registrarPasso("Tempo limite esgotado. Este tabuleiro é grande demais para este algoritmo resolver a tempo.");
                        metricasArea.setText("");
                    } else {
                        status.setText("Não foi possível resolver o Sudoku.");
                        registrarPasso("Não foi possível resolver o Sudoku.");
                        metricasArea.setText("");
                    }
                } catch (Exception e) {
                    status.setText("Erro ao resolver: " + e.getMessage());
                } finally {
                    botaoResolver.setEnabled(true);
                    botaoReiniciar.setEnabled(true);
                }
            }
        };

        tarefa.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SudokuGUI().setVisible(true));
    }
}
