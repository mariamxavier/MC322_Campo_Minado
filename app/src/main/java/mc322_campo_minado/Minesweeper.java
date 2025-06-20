package mc322_campo_minado;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

/**
 * Implementação do jogo Campo Minado usando Java Swing.
 * Responsável por criar o tabuleiro, posicionar minas,
 * tratar interações do usuário e lógica do jogo.
 */
public class Minesweeper {

    /**
     * Classe interna que representa cada célula do tabuleiro.
     */
    private class MineTile extends JButton {
        int r; // índice da linha
        int c; // índice da coluna

        /**
         * Construtor da célula.
         * @param r índice da linha
         * @param c índice da coluna
         */
        public MineTile(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    // Configurações do tabuleiro
    private final int tileSize = 70;               // tamanho de cada célula em pixels
    private final int numRows = 8;                 // número de linhas
    private final int numCols = numRows;           // número de colunas
    private final int boardWidth = numCols * tileSize;
    private final int boardHeight = numRows * tileSize;
    private final int mineCount = 10;              // quantidade total de minas

    // Componentes da interface gráfica
    private JFrame frame = new JFrame("Campo Minado");
    private JLabel textLabel = new JLabel();
    private JPanel textPanel = new JPanel();
    private JPanel boardPanel = new JPanel();

    // Dados do jogo
    private MineTile[][] board = new MineTile[numRows][numCols];
    private ArrayList<MineTile> mineList;          // lista de células que contêm minas
    private Random random = new Random();
    private int tilesClicked = 0;                  // contador de células reveladas
    private boolean gameOver = false;              // flag de fim de jogo

    /**
     * Construtor: inicializa a janela, monta o tabuleiro e posiciona as minas.
     */
    public Minesweeper() {
        configurarJanela();
        montarTabuleiro();
        frame.setVisible(true);
        posicionarMinas();
    }

    /**
     * Configura a janela principal e o painel de texto.
     */
    private void configurarJanela() {
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);                 // centraliza na tela
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Arial", Font.BOLD, 25));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Campo Minado: " + mineCount);
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(numRows, numCols));
        frame.add(boardPanel);
    }

    /**
     * Cria a grade de botões (células) e adiciona listener de mouse.
     */
    private void montarTabuleiro() {
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                MineTile tile = new MineTile(r, c);
                board[r][c] = tile;

                tile.setFocusable(false);
                tile.setMargin(new Insets(0, 0, 0, 0));
                tile.setFont(new Font("Arial Unicode MS", Font.PLAIN, 45));

                tile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (gameOver) return;

                        MineTile clicked = (MineTile) e.getSource();

                        // Clique esquerdo: revelar célula
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if (clicked.getText().equals("")) {
                                if (mineList.contains(clicked)) {
                                    revelarMinas();
                                } else {
                                    processarClique(clicked.r, clicked.c);
                                }
                            }
                        }
                        // Clique direito: marcar/desmarcar bandeira
                        else if (e.getButton() == MouseEvent.BUTTON3) {
                            if (clicked.getText().equals("") && clicked.isEnabled()) {
                                clicked.setText("🚩");
                            } else if (clicked.getText().equals("🚩")) {
                                clicked.setText("");
                            }
                        }
                    }
                });

                boardPanel.add(tile);
            }
        }
    }

    /**
     * Posiciona aleatoriamente as minas no tabuleiro.
     */
    private void posicionarMinas() {
        mineList = new ArrayList<>();
        int minasRestantes = mineCount;

        while (minasRestantes > 0) {
            int r = random.nextInt(numRows);
            int c = random.nextInt(numCols);
            MineTile tile = board[r][c];

            if (!mineList.contains(tile)) {
                mineList.add(tile);
                minasRestantes--;
            }
        }
    }

    /**
     * Revela todas as minas quando o jogador clica em uma delas.
     */
    private void revelarMinas() {
        for (MineTile tile : mineList) {
            tile.setText("💣");
        }
        gameOver = true;
        textLabel.setText("Game Over!");
    }

    /**
     * Processa o clique em uma célula segura, conta minas adjacentes
     * e expande automaticamente se não houver minas por perto.
     * 
     * @param r índice da linha clicada
     * @param c índice da coluna clicada
     */
    private void processarClique(int r, int c) {
        // verifica limites
        if (r < 0 || r >= numRows || c < 0 || c >= numCols) return;

        MineTile tile = board[r][c];
        if (!tile.isEnabled()) return;      // já foi revelada

        tile.setEnabled(false);             // desativa botão
        tilesClicked++;

        int minasAdjacentes = 0;

        // conta minas nas 8 direções
        minasAdjacentes += contarMina(r-1, c-1);
        minasAdjacentes += contarMina(r-1, c);
        minasAdjacentes += contarMina(r-1, c+1);
        minasAdjacentes += contarMina(r,   c-1);
        minasAdjacentes += contarMina(r,   c+1);
        minasAdjacentes += contarMina(r+1, c-1);
        minasAdjacentes += contarMina(r+1, c);
        minasAdjacentes += contarMina(r+1, c+1);

        if (minasAdjacentes > 0) {
            // mostra número de minas ao redor
            tile.setText(Integer.toString(minasAdjacentes));
        } else {
            // expande automaticamente para células vizinhas
            processarClique(r-1, c-1);
            processarClique(r-1, c);
            processarClique(r-1, c+1);
            processarClique(r,   c-1);
            processarClique(r,   c+1);
            processarClique(r+1, c-1);
            processarClique(r+1, c);
            processarClique(r+1, c+1);
        }

        // se todas as células seguras foram reveladas, jogador vence
        if (tilesClicked == numRows * numCols - mineList.size()) {
            gameOver = true;
            textLabel.setText("Minas Neutralizadas!");
        }
    }

    /**
     * Verifica se há mina na posição informada.
     * 
     * @param r linha a verificar
     * @param c coluna a verificar
     * @return 1 se houver mina, 0 caso contrário
     */
    private int contarMina(int r, int c) {
        if (r < 0 || r >= numRows || c < 0 || c >= numCols) {
            return 0;
        }
        return mineList.contains(board[r][c]) ? 1 : 0;
    }
}
