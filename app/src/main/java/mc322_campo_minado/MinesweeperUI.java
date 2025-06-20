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

        // Painel de configuração (topo)
        setupPanel = new SetupPanel(e -> onStartClicked());
        add(setupPanel, BorderLayout.NORTH);

        // Painel do tabuleiro (centro)
        boardPanel = new BoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        // Painel de status (rodapé)
        statusPanel = new StatusPanel();
        add(statusPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Ação quando o usuário clica em "Start": lê parâmetros, inicializa Game e monta o tabuleiro.
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

            // Inicializa lógica
            game = new Game(size, size, mines);
            game.startGame(bet);

            // Atualiza status
            statusPanel.updateBalance(game.getPlayer().getBalance());
            statusPanel.updateMultiplier(game.getBet().getCurrentMultiplier());
            statusPanel.updateStatus("Playing");

            // Desativa Start até próxima rodada
            setupPanel.setStartEnabled(false);

            // Monta tabuleiro
            boardPanel.buildBoard(size, size, (r, c, btn) -> handleCellClick(r, c, btn));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    /**
     * Tratamento de clique em célula: revela, atualiza UI e encerra rodada se necessário.
     */
    private void handleCellClick(int r, int c, JButton btn) {
        if (game.checkGameOver()) return;

        boolean safe = game.revealCell(r, c);
        Cell cell = game.getBoard().getCell(r, c);

        if (cell.hasMine()) {
            btn.setText("💣");
        } else {
            // exibe número de minas adjacentes
            int adj = 0;
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    int rr = r + dr, cc = c + dc;
                    if (rr >= 0 && rr < game.getBoard().getRows() && cc >= 0 && cc < game.getBoard().getCols()
                        && game.getBoard().getCell(rr, cc).hasMine()) {
                        adj++;
                    }
                }
            }
            btn.setText(adj > 0 ? String.valueOf(adj) : "");
        }
        btn.setEnabled(false);

        // Atualiza multiplicador no status
        statusPanel.updateMultiplier(game.getBet().getCurrentMultiplier());

        if (!safe) {
            // se perdeu, revela todas as minas e encerra
            boardPanel.revealAllMines(game.getBoard());
            endRound("Game Over!");
        } else if (game.checkGameOver()) {
            // se ganhou (todas células seguras reveladas)
            endRound("You Win!");
        }
    }

    /**
     * Realiza cash out: atualiza saldo, status e prepara próxima rodada.
     */
    private void doCashOut() {
        double payout = game.cashOut();
        statusPanel.updateBalance(game.getPlayer().getBalance());
        statusPanel.updateStatus("Cashed out: " + String.format("%.2f", payout));
        setupPanel.setStartEnabled(true);
    }

    /**
     * Encerramento de rodada: atualiza status e habilita Start.
     */
    private void endRound(String message) {
        statusPanel.updateStatus(message);
        setupPanel.setStartEnabled(true);
    }

    /**
     * Ponto de entrada da aplicação.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MinesweeperUI::new);
    }
}
