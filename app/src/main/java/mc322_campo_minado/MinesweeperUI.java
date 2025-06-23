package mc322_campo_minado;

import mc322_campo_minado.patterns.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface gráfica principal do jogo Campo Minado com apostas.
 * Gerencia a inicialização dos painéis, botões e interação do usuário,
 * além de controlar o fluxo do jogo e comandos como dica e cash out.
 */
public class MinesweeperUI extends JFrame {
    /** Cor de fundo para dica de célula segura */
    private static final Color HINT_SAFE_BG = Color.decode("#2E7D32");

    private SetupPanel setupPanel;
    private BoardPanel boardPanel;
    private StatusPanel statusPanel;
    private JButton cashOutButton;
    private JButton hintButton;
    private Game game;

    // Última célula clicada pelo usuário
    private int lastClickedRow = -1, lastClickedCol = -1;
    private CellButton lastClickedButton = null;

    /**
     * Construtor da interface do jogo.
     * Inicializa os componentes gráficos.
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

        JPanel topPanel = new JPanel(new BorderLayout());
        setupPanel = new SetupPanel(e -> onStartClicked());
        topPanel.add(setupPanel, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        cashOutButton = new JButton("Cash Out");
        cashOutButton.setEnabled(false);
        cashOutButton.addActionListener(e -> new CashOutCommand(this).execute());
        buttons.add(cashOutButton);

        hintButton = new JButton("Hint");
        hintButton.setEnabled(false);
        hintButton.addActionListener(e -> new HintCommand(this).execute());
        buttons.add(hintButton);

        topPanel.add(buttons, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        boardPanel = new BoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        statusPanel = new StatusPanel();
        add(statusPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Manipula o clique no botão de início do jogo.
     * Valida os parâmetros e inicializa uma nova partida.
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

            double prevBalance = (game != null)
                    ? game.getPlayer().getBalance()
                    : 1000.0;

            game = GameFactory.createGame(size, size, mines, prevBalance);
            GameSession.getInstance().setGame(game);
            game.addObserver(statusPanel);
            game.startGame(bet);

            statusPanel.updateStatus("Bet placed " + String.format("%.2f", bet));
            setupPanel.setStartEnabled(false);
            cashOutButton.setEnabled(false);
            hintButton.setEnabled(false);

            boardPanel.buildBoard(size, size,
                    (r, c, btn) -> new RevealCommand(r, c, btn, this).execute());

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers");
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    /**
     * Manipula a revelação de uma célula do tabuleiro.
     * Atualiza o estado do jogo e a interface conforme o resultado.
     *
     * @param r   Linha da célula
     * @param c   Coluna da célula
     * @param btn Botão correspondente à célula
     */
    public void handleReveal(int r, int c, CellButton btn) {
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

        if (!safe) {
            boardPanel.revealAllMines(game.getBoard());
            btn.showExplosion();
            endRound("Game Over!");
        } else if (game.checkGameOver()) {
            double payout = game.cashOut();
            endRound("You Win! Payout: " + String.format("%.2f", payout));
        }
    }

    /**
     * Executa a lógica da dica ("Hint") para o jogador.
     * Revela temporariamente as células adjacentes à última célula clicada.
     */
    public void handleHint() {
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

        game.getBet().applyHintFee();
        game.setHintUsed(true); // já usou a dica
        statusPanel.updatePayout(game.getBet().getCurrentPayout());
        statusPanel.updateBalance(game.getPlayer().getBalance());
        statusPanel.updateStatus(String.format("Hint used: -%.2f", fee));

        Board board = game.getBoard();
        CellButton[][] buttons = boardPanel.getCellButtons();
        List<CellButton> affectedButtons = new ArrayList<>();

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int rr = lastClickedRow + dir[0];
            int cc = lastClickedCol + dir[1];
            if (rr >= 0 && rr < board.getRows() && cc >= 0 && cc < board.getCols()) {
                Cell cell = board.getCell(rr, cc);
                CellButton btn = buttons[rr][cc];
                if (!cell.isRevealed()) {
                    if (cell.hasMine()) {
                        btn.showMine();
                    } else {
                        btn.setIcon(null);
                        btn.setBackground(HINT_SAFE_BG);
                    }
                    btn.setEnabled(false);
                    affectedButtons.add(btn);
                }
            }
        }
        hintButton.setEnabled(false);
        Timer timer = new Timer(1000, e -> affectedButtons.forEach(CellButton::reset));
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Executa a ação de "Cash Out", encerrando a rodada e pagando o jogador.
     */
    public void handleCashOut() {
        double payout = game.cashOut();
        statusPanel.updateStatus("Cashed out: " + String.format("%.2f", payout));
        setupPanel.setStartEnabled(true);
        cashOutButton.setEnabled(false);
        hintButton.setEnabled(false);
    }

    /**
     * Finaliza a rodada, desabilitando botões e exibindo mensagem.
     *
     * @param msg Mensagem de status a ser exibida
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
     * @param args Argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MinesweeperUI::new);
    }
}