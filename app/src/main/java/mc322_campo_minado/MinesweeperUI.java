package mc322_campo_minado;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Interface gráfica Swing para o Minesweeper Bet Game.
 * Agora permite configurar tamanho do tabuleiro (quadrado),
 * número de minas e aposta antes de iniciar.
 */
public class MinesweeperUI extends JFrame {
    private Game game;                 // lógica do jogo
    private JPanel boardPanel;         // painel do tabuleiro
    private JTextField sizeField;      // campo para tamanho (rows=cols)
    private JTextField mineField;      // campo para quantidade de minas
    private JTextField betField;       // campo para valor da aposta
    private JButton startButton;       // inicia rodada
    private JButton cashOutButton;     // faz cash out
    private JLabel balanceLabel;       // exibe saldo do jogador
    private JLabel multiplierLabel;    // exibe multiplicador atual
    private JLabel statusLabel;        // exibe status da rodada
    private int rows, cols;

    /**
     * Construtor: monta UI sem iniciar o jogo.
     */
    public MinesweeperUI() {
        super("Minesweeper Bet Game");
        initComponents();
    }

    /**
     * Configura todos os componentes da interface.
     */
    private void initComponents() {
        setLayout(new BorderLayout());

        // Painel de controles
        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Size:"));
        sizeField = new JTextField("8", 3);
        controlPanel.add(sizeField);

        controlPanel.add(new JLabel("Mines:"));
        mineField = new JTextField("10", 3);
        controlPanel.add(mineField);

        controlPanel.add(new JLabel("Bet:"));
        betField = new JTextField("100", 5);
        controlPanel.add(betField);

        startButton = new JButton("Start");
        controlPanel.add(startButton);

        cashOutButton = new JButton("Cash Out");
        cashOutButton.setEnabled(false);
        controlPanel.add(cashOutButton);

        balanceLabel = new JLabel("Balance: --");
        controlPanel.add(balanceLabel);

        multiplierLabel = new JLabel("Multiplier: --");
        controlPanel.add(multiplierLabel);

        statusLabel = new JLabel("Status: Ready");
        controlPanel.add(statusLabel);

        add(controlPanel, BorderLayout.NORTH);

        // Painel do tabuleiro
        boardPanel = new JPanel();
        add(boardPanel, BorderLayout.CENTER);

        // Listeners dos botões
        startButton.addActionListener(e -> startGame());
        cashOutButton.addActionListener(e -> doCashOut());

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 650);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Inicia uma nova rodada: lê tamanho, mina e aposta,
     * inicializa Game e monta o tabuleiro.
     */
    private void startGame() {
        try {
            rows = Integer.parseInt(sizeField.getText());
            cols = rows;
            int mineCount = Integer.parseInt(mineField.getText());
            double bet = Double.parseDouble(betField.getText());

            if (rows < 2 || mineCount < 1 || mineCount >= rows * cols) {
                JOptionPane.showMessageDialog(this, "Invalid size or mines");
                return;
            }

            game = new Game(rows, cols, mineCount);
            game.startGame(bet);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers");
            return;
        }

        startButton.setEnabled(false);
        cashOutButton.setEnabled(true);
        balanceLabel.setText("Balance: " + game.getPlayer().getBalance());
        multiplierLabel.setText("Multiplier: " + String.format("%.2f", game.getBet().getCurrentMultiplier()));
        statusLabel.setText("Status: Playing");
        buildBoard();
    }

    /**
     * Monta os botões do tabuleiro para interação.
     */
    private void buildBoard() {
        boardPanel.removeAll();
        boardPanel.setLayout(new GridLayout(rows, cols));
        int cellSize = Math.max(400 / rows, 30);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(cellSize, cellSize));
                final int rr = r, cc = c;
                btn.addActionListener(e -> handleClick(rr, cc, btn));
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
    private void handleClick(int r, int c, JButton btn) {
        if (game.checkGameOver()) return;

        boolean safe = game.revealCell(r, c);
        Cell cell = game.getBoard().getCell(r, c);

        if (cell.hasMine()) {
            btn.setText("💣");
        } else {
            int adj = countAdjacent(r, c);
            btn.setText(adj > 0 ? String.valueOf(adj) : "");
        }
        btn.setEnabled(false);

        multiplierLabel.setText("Multiplier: " + String.format("%.2f", game.getBet().getCurrentMultiplier()));

        if (!safe) {
            revealMines();
            endRound("Game Over!");
        } else if (game.checkGameOver()) {
            endRound("You Win!");
        }
    }

    /**
     * Conta minas ao redor de uma célula.
     */
    private int countAdjacent(int r, int c) {
        int cnt = 0;
        Board b = game.getBoard();
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int rr = r + dr, cc = c + dc;
                if (rr >= 0 && rr < rows && cc >= 0 && cc < cols && b.getCell(rr, cc).hasMine()) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    /**
     * Revela todas as minas no tabuleiro ao fim de jogo.
     */
    private void revealMines() {
        Board b = game.getBoard();
        Component[] comps = boardPanel.getComponents();
        for (int i = 0; i < comps.length; i++) {
            JButton btn = (JButton) comps[i];
            int r = i / cols, c = i % cols;
            if (b.getCell(r, c).hasMine()) {
                btn.setText("💣");
            }
            btn.setEnabled(false);
        }
    }

    /**
     * Executa o cash out, atualiza saldo e estado da UI.
     */
    private void doCashOut() {
        double payout = game.cashOut();
        balanceLabel.setText("Balance: " + game.getPlayer().getBalance());
        statusLabel.setText("Status: Cashed out: " + String.format("%.2f", payout));
        cashOutButton.setEnabled(false);
        startButton.setEnabled(true);
    }

    /**
     * Finaliza a rodada, exibe mensagem e atualiza UI.
     */
    private void endRound(String msg) {
        statusLabel.setText("Status: " + msg);
        cashOutButton.setEnabled(false);
        startButton.setEnabled(true);
    }

    /**
     * Ponto de entrada da aplicação.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MinesweeperUI::new);
    }
}
