package mc322_campo_minado;

import mc322_campo_minado.patterns.StatusObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por gerenciar o fluxo do jogo Campo Minado.
 * Orquestra as interações entre o tabuleiro (Board), o jogador (Player) e a aposta (Bet).
 * Também controla o estado do jogo, uso de dicas e notificação de observadores.
 */
public class Game {
    /** Tabuleiro do jogo */
    private final Board board;
    /** Jogador da partida */
    private final Player player;
    /** Aposta atual */
    private Bet bet;
    /** Indica se o jogo terminou */
    private boolean isGameOver;
    /** Indica se a dica já foi usada nesta rodada */
    private boolean hintUsed;
    /** Quantidade de células seguras reveladas */
    private int safeCellsRevealed;

    /** Lista de observadores para atualização de status */
    private final List<StatusObserver> observers = new ArrayList<>();

    /**
     * Construtor do jogo.
     * Inicializa o tabuleiro, jogador e estado inicial.
     *
     * @param rows      Número de linhas do tabuleiro
     * @param cols      Número de colunas do tabuleiro
     * @param mineCount Número de minas no tabuleiro
     */
    public Game(int rows, int cols, int mineCount) {
        this.board = new Board(rows, cols, mineCount);
        this.player = new Player(1000.0);
        this.isGameOver = false;
        this.hintUsed = false;
        this.safeCellsRevealed = 0;
    }

    /**
     * Adiciona um observador para ser notificado sobre mudanças de status do jogo.
     *
     * @param obs Observador a ser adicionado
     */
    public void addObserver(StatusObserver obs) {
        observers.add(obs);
    }

    /**
     * Notifica todos os observadores registrados sobre uma mudança de status.
     */
    private void notifyObservers() {
        for (StatusObserver obs : observers) {
            obs.onStatusChanged();
        }
    }

    /**
     * Inicia uma nova rodada, realizando a aposta e gerando o tabuleiro.
     *
     * @param betAmount Valor da aposta
     */
    public void startGame(double betAmount) {
        player.placeBet(betAmount);
        player.loseBet(betAmount);

        board.generateBoard();
        this.bet = new Bet(betAmount);
        this.isGameOver = false;
        this.hintUsed = false;
        this.safeCellsRevealed = 0;

        notifyObservers();
    }

    /**
     * Revela uma célula do tabuleiro.
     * Atualiza o estado do jogo conforme o resultado (mina ou célula segura).
     *
     * @param r Linha da célula
     * @param c Coluna da célula
     * @return true se a célula era segura, false se era uma mina
     */
    public boolean revealCell(int r, int c) {
        Cell cell = board.getCell(r, c);

        // Ignora se a célula já foi revelada ou se o jogo acabou
        if (cell.isRevealed() || isGameOver) {
            return true;
        }

        cell.reveal();

        // Se for mina, encerra o jogo
        if (cell.hasMine()) {
            isGameOver = true;
            notifyObservers();
            return false;
        }

        // Se for célula segura, atualiza contagem e multiplica aposta
        safeCellsRevealed++;
        int safeCells = board.getRemainingSafeCells();
        bet.increaseMultiplier(safeCells, board.getRemainingMines());

        // Se não restam células seguras, o jogador venceu
        if (safeCells == 0) {
            isGameOver = true;
        }

        notifyObservers();
        return true;
    }

    /**
     * Realiza o cash out, encerrando a rodada e creditando o prêmio ao jogador.
     *
     * @return Valor do prêmio recebido
     */
    public double cashOut() {
        double payout = bet.getCurrentPayout();
        player.addWinnings(payout);
        isGameOver = true;
        notifyObservers();
        return payout;
    }

    /**
     * Verifica se o jogo já terminou.
     *
     * @return true se o jogo acabou, false caso contrário
     */
    public boolean checkGameOver() {
        return isGameOver;
    }

    /**
     * Retorna o jogador da partida.
     *
     * @return Instância de Player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Retorna a aposta atual.
     *
     * @return Instância de Bet
     */
    public Bet getBet() {
        return bet;
    }

    /**
     * Retorna o tabuleiro do jogo.
     *
     * @return Instância de Board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Indica se a dica já foi utilizada nesta rodada.
     *
     * @return true se a dica foi usada, false caso contrário
     */
    public boolean isHintUsed() {
        return hintUsed;
    }

    /**
     * Define o uso da dica nesta rodada.
     *
     * @param used true para marcar como usada, false caso contrário
     */
    public void setHintUsed(boolean used) {
        this.hintUsed = used;
        notifyObservers();
    }

    /**
     * Retorna a quantidade de células seguras já reveladas.
     *
     * @return Número de células seguras reveladas
     */
    public int getSafeCellsRevealed() {
        return safeCellsRevealed;
    }
}