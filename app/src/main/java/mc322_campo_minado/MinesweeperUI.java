package mc322_campo_minado;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Interface gráfica Swing para o Minesweeper Bet Game.
 * Cria a janela jogável, controla apostas, revela células e faz cash out.
 */
public class MinesweeperUI extends JFrame {
    private Game game;                 // lógica do jogo
    private JPanel boardPanel;         // painel que contém o tabuleiro
    private JTextField betField;       // campo para inserir aposta
    private JButton startButton;       // botão para iniciar rodada
    private JButton cashOutButton;     // botão para sacar ganhos
    private JLabel balanceLabel;       // exibe saldo do jogador
    private JLabel multiplierLabel;    // exibe multiplicador atual
    private JLabel statusLabel;        // exibe status da rodada
    private int rows, cols;

    /**
     * Construtor: monta UI e inicializa Game.
     *
     * @param rows      número de linhas do tabuleiro
     * @param cols      número de colunas do tabuleiro
     * @param mineCount quantidade de minas
     */
    public MinesweeperUI(int rows, int cols, int mineCount) {
        super("Minesweeper Bet Game");
        this.rows = rows;
        this.cols = cols;
        this.game = new Game(rows, cols, mineCount);
        initComponents();
    }

    /**
     * Configura todos os componentes da interface.
     */
    private void initComponents() {
        setLayout(new BorderLayout());

        // Painel de controles (norte)
        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Bet:"));
        betField = new JTextField(5);
        controlPanel.add(betField);
        startButton = new JButton("Start");
        controlPanel.add(startButton);
        cashOutButton = new JButton("Cash Out");
        cashOutButton.setEnabled(false);
        controlPanel.add(cashOutButton);
        balanceLabel = new JLabel("Balance: " + game.getPlayer().getBalance());
        controlPanel.add(balanceLabel);
        multiplierLabel = new JLabel("Multiplier: 1.00");
        controlPanel.add(multiplierLabel);
        statusLabel = new JLabel("Status: Ready");
        controlPanel.add(statusLabel);
        add(controlPanel, BorderLayout.NORTH);

        // Painel do tabuleiro (centro)
        boardPanel = new JPanel(new GridLayout(rows, cols));
        add(boardPanel, BorderLayout.CENTER);

        // Listeners dos botões
        startButton.addActionListener(e -> startGame());
        cashOutButton.addActionListener(e -> cashOut());

        // Configurações finais da janela
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(cols * 50, rows * 50 + 120);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Inicia uma nova rodada: lê aposta, inicializa Game e monta o tabuleiro.
     */
    private void startGame() {
        double betAmount;
        try {
            betAmount = Double.parseDouble(betField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor de aposta inválido");
            return;
        }
        try {
            game.startGame(betAmount);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            return;
        }
        startButton.setEnabled(false);
        cashOutButton.setEnabled(true);
        statusLabel.setText("Status: Playing");
        balanceLabel.setText("Balance: " + game.getPlayer().getBalance());
        multiplierLabel.setText("Multiplier: " + String.format("%.2f", game.getBet().getCurrentMultiplier()));
        buildBoard();
    }

    /**
     * Monta os botões do tabuleiro para interação.
     */
    private void buildBoard() {
        boardPanel.removeAll();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(40, 40));
                final int row = r, col = c;
                btn.addActionListener(e -> handleCellClick(row, col, btn));
                boardPanel.add(btn);
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    /**
     * Trata o clique em uma célula: revela, atualiza multiplicador,
     * exibe minas e finaliza rodada se necessário.
     */
    private void handleCellClick(int r, int c, JButton btn) {
        if (game.checkGameOver()) return;

        boolean safe = game.revealCell(r, c);
        Cell cell = game.getBoard().getCell(r, c);

        // Exibe mina ou número de minas adjacentes
        if (cell.hasMine()) {
            btn.setText("💣");
        } else {
            int count = countAdjacentMines(r, c);
            btn.setText(count > 0 ? String.valueOf(count) : "");
        }
        btn.setEnabled(false);

        // Atualiza multiplicador
        multiplierLabel.setText("Multiplier: " + String.format("%.2f", game.getBet().getCurrentMultiplier()));

        if (!safe) {
            revealAllMines();
            endRound("Game Over!");
        } else if (game.checkGameOver()) {
            endRound("You Win!");
        }
    }

    /**
     * Conta minas ao redor de uma célula.
     */
    private int countAdjacentMines(int r, int c) {
        int cnt = 0;
        Board board = game.getBoard();
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int rr = r + dr, cc = c + dc;
                if (rr >= 0 && rr < rows && cc >= 0 && cc < cols && board.getCell(rr, cc).hasMine()) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    /**
     * Revela todas as minas no tabuleiro ao fim de jogo.
     */
    private void revealAllMines() {
        Board board = game.getBoard();
        Component[] comps = boardPanel.getComponents();
        for (int i = 0; i < comps.length; i++) {
            JButton btn = (JButton) comps[i];
            int r = i / cols, c = i % cols;
            if (board.getCell(r, c).hasMine()) {
                btn.setText("💣");
            }
            btn.setEnabled(false);
        }
    }

    /**
     * Executa o cash out, atualiza saldo e estado da UI.
     */
    private void cashOut() {
        double payout = game.cashOut();
        balanceLabel.setText("Balance: " + game.getPlayer().getBalance());
        statusLabel.setText("Status: Cashed out: " + String.format("%.2f", payout));
        cashOutButton.setEnabled(false);
        startButton.setEnabled(true);
    }

    /**
     * Finaliza a rodada, exibe mensagem e atualiza UI.
     */
    private void endRound(String message) {
        statusLabel.setText("Status: " + message);
        cashOutButton.setEnabled(false);
        startButton.setEnabled(true);
    }

    /**
     * Ponto de entrada da aplicação.
     */
    public static void main(String[] args) {
        new MinesweeperUI(8, 8, 10);
    }
}
