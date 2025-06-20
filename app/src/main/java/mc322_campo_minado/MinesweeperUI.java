package mc322_campo_minado;

import javax.swing.*;
import java.awt.*;

/**
 * Interface principal que orquestra os painéis de configuração, tabuleiro e status.
 * Controla a lógica do jogo usando Game.
 */
public class MinesweeperUI extends JFrame {
    private SetupPanel setupPanel;   // painel de configurações iniciais
    private BoardPanel boardPanel;   // painel do tabuleiro
    private StatusPanel statusPanel; // painel de informações (saldo, mult, status)
    private JButton cashOutButton;   // botão de saque manual
    private Game game;               // lógica do jogo

    /**
     * Construtor: monta a janela e posiciona os três painéis.
     */
    public MinesweeperUI() {
        super("Minesweeper Bet Game");
        initComponents();
    }

    /**
     * Inicializa e posiciona os componentes na janela.
     */
    private void initComponents() {
        setLayout(new BorderLayout());

        // Painel superior: configuração e cash out
        JPanel topPanel = new JPanel(new BorderLayout());
        setupPanel = new SetupPanel(e -> onStartClicked());
        topPanel.add(setupPanel, BorderLayout.CENTER);

        cashOutButton = new JButton("Cash Out");
        cashOutButton.setEnabled(false);
        cashOutButton.addActionListener(e -> doCashOut());
        topPanel.add(cashOutButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Painel do tabuleiro (centro)
        boardPanel = new BoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        // Painel de status (rodapé)
        statusPanel = new StatusPanel();
        add(statusPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Ação quando o usuário clica em "Start":
     * lê parâmetros, inicializa Game e monta o tabuleiro.
     * Preserva o saldo atual entre rodadas.
     */
    private void onStartClicked() {
        try {
            int size = setupPanel.getBoardSize();
            int mines = setupPanel.getMineCount();
            double bet = setupPanel.getBetAmount();
            if (size < 2 || mines < 1 || mines >= size * size) {
                JOptionPane.showMessageDialog(this, "Invalid size or mines");
                return;
            }

            // Preserva saldo antes de recriar o jogo
            double prevBalance = (game != null)
                ? game.getPlayer().getBalance()
                : 1000.0;

            game = new Game(size, size, mines);
            game.getPlayer().setBalance(prevBalance);
            game.startGame(bet);

            // Atualiza UI de status
            statusPanel.updateBalance(game.getPlayer().getBalance());
            statusPanel.updateMultiplier(game.getBet().getCurrentMultiplier());
            statusPanel.updateStatus("Playing");
            setupPanel.setStartEnabled(false);
            cashOutButton.setEnabled(true);

            // Monta o tabuleiro interativo
            boardPanel.buildBoard(size, size, (r, c, btn) -> handleCellClick(r, c, btn));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    /**
     * Trata o clique em uma célula: revela, atualiza UI
     * e encerra rodada em caso de mina ou vitória.
     */
    private void handleCellClick(int r, int c, JButton btn) {
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

        statusPanel.updateMultiplier(game.getBet().getCurrentMultiplier());

        if (!safe) {
            boardPanel.revealAllMines(game.getBoard());
            statusPanel.updateBalance(game.getPlayer().getBalance());
            endRound("Game Over!");
        } else if (game.checkGameOver()) {
            double payout = game.cashOut();
            statusPanel.updateBalance(game.getPlayer().getBalance());
            endRound("You Win! Payout: " + String.format("%.2f", payout));
        }
    }

    /**
     * Conta minas adjacentes a uma célula.
     */
    private int countAdjacent(int r, int c) {
        int cnt = 0;
        Board b = game.getBoard();
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int rr = r + dr, cc = c + dc;
                if (rr >= 0 && rr < b.getRows() && cc >= 0 && cc < b.getCols()
                    && b.getCell(rr, cc).hasMine()) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    /**
     * Realiza cash out manual: atualiza saldo e status,
     * e prepara para próxima rodada.
     */
    private void doCashOut() {
        double payout = game.cashOut();
        statusPanel.updateBalance(game.getPlayer().getBalance());
        statusPanel.updateStatus("Cashed out: " + String.format("%.2f", payout));
        setupPanel.setStartEnabled(true);
        cashOutButton.setEnabled(false);
    }

    /**
     * Encerra a rodada, atualiza status e habilita Start.
     */
    private void endRound(String message) {
        statusPanel.updateStatus(message);
        setupPanel.setStartEnabled(true);
        cashOutButton.setEnabled(false);
    }

    /**
     * Ponto de entrada da aplicação.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MinesweeperUI::new);
    }
}
