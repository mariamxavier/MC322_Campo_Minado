package mc322_campo_minado;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface principal que orquestra os painéis de configuração, tabuleiro, status e dica paga.
 * Controla a lógica do jogo usando Game e exibe tudo com CellButton.
 */
public class MinesweeperUI extends JFrame {
    private static final Color HINT_SAFE_BG = Color.decode("#2E7D32");

    private SetupPanel setupPanel;     // painel de configurações iniciais
    private BoardPanel boardPanel;     // painel do tabuleiro (com CellButton)
    private StatusPanel statusPanel;   // painel de informações (saldo, mult, status)
    private JButton cashOutButton;     // botão de saque manual
    private JButton hintButton;        // botão de dica paga
    private Game game;                 // lógica do jogo

    // Guarda a última célula clicada para usar na dica
    private int lastClickedRow = -1, lastClickedCol = -1;
    private CellButton lastClickedButton = null;
    private double fee = 0.0; // taxa da dica paga

    public MinesweeperUI() {
        super("Minesweeper Bet Game");
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Painel superior: configuração + Cash Out + Hint
        JPanel topPanel = new JPanel(new BorderLayout());
        setupPanel = new SetupPanel(e -> onStartClicked());
        topPanel.add(setupPanel, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        cashOutButton = new JButton("Cash Out");
        cashOutButton.setEnabled(false);
        cashOutButton.addActionListener(e -> doCashOut());
        buttons.add(cashOutButton);

        hintButton = new JButton("Hint");
        hintButton.setEnabled(false);
        hintButton.addActionListener(e -> doHint());
        buttons.add(hintButton);

        topPanel.add(buttons, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Tabuleiro
        boardPanel = new BoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        // Status
        statusPanel = new StatusPanel();
        add(statusPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void onStartClicked() {
        try {
            int size = setupPanel.getBoardSize();
            int mines = setupPanel.getMineCount();
            double bet = setupPanel.getBetAmount();
            if (size < 2 || mines < 1 || mines >= size * size) {
                JOptionPane.showMessageDialog(this, "Invalid size or mines");
                return;
            }

            double prevBalance = (game != null)
                ? game.getPlayer().getBalance()
                : 1000.0;

            game = new Game(size, size, mines);
            game.getPlayer().setBalance(prevBalance);
            game.startGame(bet);

            statusPanel.updateBalance(prevBalance);
            statusPanel.updateMultiplier(game.getBet().getCurrentMultiplier());
            statusPanel.updateStatus("Playing");
            setupPanel.setStartEnabled(false);
            cashOutButton.setEnabled(false);  
            hintButton.setEnabled(false);

            boardPanel.buildBoard(size, size, this::handleCellClick);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void handleCellClick(int r, int c, CellButton btn) {
        if (game.checkGameOver()) return;

        lastClickedRow = r;
        lastClickedCol = c;
        lastClickedButton = btn;

        boolean safe = game.revealCell(r, c);
        if (game.getBoard().getCell(r, c).hasMine()) {
            btn.showMine();
        } else {
            btn.showGem();                           // exibe o ícone único de gema
            hintButton.setEnabled(true);             // ativa o botão de dica quando uma célula segura é revelada
            if (game.getSafeCellsRevealed() >= 3) {  // se 3 células seguras foram reveladas
                cashOutButton.setEnabled(true);      // ativa o botão de saque
            }
        }
        btn.setEnabled(false);
        statusPanel.updateMultiplier(game.getBet().getCurrentMultiplier());

        if (!safe) {
            boardPanel.revealAllMines(game.getBoard());
            btn.showExplosion();
            statusPanel.updateBalance(game.getPlayer().getBalance());
            endRound("Game Over!");
        } else if (game.checkGameOver()) {
            double payout = game.cashOut();
            statusPanel.updateBalance(game.getPlayer().getBalance());
            endRound("You Win! Payout: " + String.format("%.2f", payout));
        }
    }

    private void doHint() {
        if (lastClickedRow < 0) {
            JOptionPane.showMessageDialog(this, "No cell selected");
            return;
        }

        if (game.isHintUsed()) {
            JOptionPane.showMessageDialog(this, "Você já usou uma dica nesta rodada.");
            return;
        }

        fee = fee + game.getBet().getHintFee();
        if (game.getPlayer().getBalance() < fee) {
            JOptionPane.showMessageDialog(this, "Insufficient balance for hint");
            return;
        }

        game.getPlayer().loseBet(fee);
        game.setHintUsed(true); // já usou a dica
        statusPanel.updateBalance(game.getPlayer().getBalance());
        statusPanel.updateStatus(String.format("Hint used: -%.2f", fee));

        Board board = game.getBoard();
        CellButton[][] buttons = boardPanel.getCellButtons();
        List<CellButton> affectedButtons = new ArrayList<>();

        int[][] directions = {
            {-1, 0}, // cima
            {1, 0},  // baixo
            {0, -1}, // esquerda
            {0, 1}   // direita
        };

        for (int[] dir : directions) {
            int rr = lastClickedRow + dir[0];
            int cc = lastClickedCol + dir[1];

            if (rr >= 0 && rr < board.getRows() && cc >= 0 && cc < board.getCols()) {
                Cell cell = board.getCell(rr, cc);
                CellButton btn = buttons[rr][cc];

                if (!cell.isRevealed()) {
                    if (cell.hasMine()) {
                        btn.showMine(); // ícone + fundo vermelho
                    } else {
                        btn.setIcon(null); // não mostra a gema
                        btn.setBackground(HINT_SAFE_BG); // apenas fundo verde
                    }
                    btn.setEnabled(false); // impede clique temporariamente
                    affectedButtons.add(btn);
                }
            }
        }

        hintButton.setEnabled(false);

        // Após 1 segundo, volta ao normal
        Timer timer = new Timer(1000, e -> {
            for (CellButton btn : affectedButtons) {
                btn.reset();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void doCashOut() {
        double payout = game.cashOut();
        statusPanel.updateBalance(game.getPlayer().getBalance());
        statusPanel.updateStatus("Cashed out: " + String.format("%.2f", payout));
        setupPanel.setStartEnabled(true);
        cashOutButton.setEnabled(false);
        hintButton.setEnabled(false);
    }

    private void endRound(String msg) {
        statusPanel.updateStatus(msg);
        setupPanel.setStartEnabled(true);
        cashOutButton.setEnabled(false);
        hintButton.setEnabled(false);
    }

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MinesweeperUI::new);
    }
}
