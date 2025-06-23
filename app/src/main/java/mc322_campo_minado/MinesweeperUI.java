package mc322_campo_minado;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface principal da aplicação Campo Minado com apostas.
 * Orquestra os painéis de configuração, tabuleiro, status e dica paga.
 * Controla a lógica do jogo usando Game e exibe tudo com CellButton.
 */
public class MinesweeperUI extends JFrame {
    private SetupPanel setupPanel;     // painel de configurações iniciais
    private BoardPanel boardPanel;     // painel do tabuleiro (com CellButton)
    private StatusPanel statusPanel;   // painel de informações (saldo, mult, status)
    private JButton cashOutButton;     // botão de saque manual
    private JButton hintButton;        // botão de dica paga
    private Game game;                 // lógica do jogo

    private static final Color HINT_SAFE_BG = Color.decode("#2E7D32"); // cor de fundo para dica segura

    // Guarda a última célula clicada para usar na dica
    private int lastClickedRow = -1, lastClickedCol = -1;
    private CellButton lastClickedButton = null;

    /**
     * Construtor da interface principal.
     * Inicializa todos os componentes gráficos.
     */
    public MinesweeperUI() {
        super("Minesweeper Bet Game");
        initComponents();
    }

    /**
     * Inicializa e organiza os componentes da interface gráfica.
     */
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

    /**
     * Inicia uma nova rodada ao clicar em "Start".
     * Valida os parâmetros, inicializa o jogo e atualiza a interface.
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

            // Mantém o saldo do jogador entre rodadas
            double prevBalance = (game != null)
                ? game.getPlayer().getBalance()
                : 1000.0;

            game = new Game(size, size, mines);
            game.getPlayer().setBalance(prevBalance);
            game.startGame(bet);

            statusPanel.setPlayer(game.getPlayer());
            statusPanel.updateMultiplier(game.getBet().getCurrentMultiplier());
            statusPanel.updateStatus("Playing");
            statusPanel.updateStatus("Bet placed: " + String.format("%.2f", bet));
            statusPanel.updatePayout(game.getBet().getCurrentPayout());
            setupPanel.setStartEnabled(false);
            cashOutButton.setEnabled(false);
            hintButton.setEnabled(false);

            boardPanel.buildBoard(game.getBoard(), this::handleCellClick);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    /**
     * Manipula o clique em uma célula do tabuleiro.
     * Atualiza o estado do jogo, interface e botões conforme o resultado.
     *
     * @param r   linha da célula clicada
     * @param c   coluna da célula clicada
     * @param btn botão correspondente à célula
     */
    private void handleCellClick(int r, int c, CellButton btn) {
        if (game.checkGameOver()) return;

        lastClickedRow = r;
        lastClickedCol = c;
        lastClickedButton = btn;

        boolean safe = game.revealCell(r, c);
        if (game.getBoard().getCell(r, c).hasMine()) {
            btn.showMine();
        } else {
            btn.showGem();
            hintButton.setEnabled(true);
            if (game.getSafeCellsRevealed() >= 3) {
                cashOutButton.setEnabled(true);
            }
        }
        btn.setEnabled(false);
        hintButton.setEnabled(true);
        statusPanel.updateMultiplier(game.getBet().getCurrentMultiplier());
        statusPanel.updatePayout(game.getBet().getCurrentPayout());

        if (!safe) {
            // Se clicou em mina, revela todas as minas e encerra a rodada
            boardPanel.revealAllMines(game.getBoard());
            btn.showExplosion();
            endRound("Game Over!");
        } else if (game.checkGameOver()) {
            // Se todas as casas seguras foram abertas, vitória
            double payout = game.cashOut();
            endRound("You Win! Payout: " + String.format("%.2f", payout));
        }
    }

    /**
     * Executa a lógica da dica paga ("Hint").
     * Revela temporariamente as células adjacentes à última célula clicada.
     */
    private void doHint() {
        if (lastClickedRow < 0) {
            JOptionPane.showMessageDialog(this, "No cell selected");
            return;
        }

        if (game.isHintUsed()) {
            JOptionPane.showMessageDialog(this, "Você já usou uma dica nesta rodada.");
            return;
        }

        double fee = game.getBet().getHintFee();
        double payout = game.getBet().getCurrentPayout();

        if (fee >= payout) {
            JOptionPane.showMessageDialog(this, "Insufficient payout for hint");
            return;
        }

        // Aplica a taxa da dica e marca como usada
        game.getBet().applyHintFee();
        game.setHintUsed(true);
        statusPanel.updatePayout(game.getBet().getCurrentPayout());
        statusPanel.updateStatus(String.format("Hint used: -%.2f", fee));

        Board board = game.getBoard();
        CellButton[][] buttons = boardPanel.getCellButtons();
        List<CellButton> affectedButtons = new ArrayList<>();

        // Direções: cima, baixo, esquerda, direita
        int[][] directions = {
            {-1, 0}, // cima
            {1, 0},  // baixo
            {0, -1}, // esquerda
            {0, 1}   // direita
        };

        // Revela temporariamente as casas adjacentes
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

    /**
     * Executa a ação de "Cash Out", encerrando a rodada e pagando o jogador.
     */
    private void doCashOut() {
        double payout = game.cashOut();
        statusPanel.updateStatus("Cashed out: " + String.format("%.2f", payout));
        setupPanel.setStartEnabled(true);
        cashOutButton.setEnabled(false);
        hintButton.setEnabled(false);
    }

    /**
     * Finaliza a rodada, desabilitando botões e exibindo mensagem.
     *
     * @param msg mensagem de status a ser exibida
     */
    private void endRound(String msg) {
        statusPanel.updateStatus(msg);
        setupPanel.setStartEnabled(true);
        cashOutButton.setEnabled(false);
        hintButton.setEnabled(false);
    }

    /**
     * Método principal. Inicia a interface Swing do jogo.
     *
     * @param args argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MinesweeperUI::new);
    }
}