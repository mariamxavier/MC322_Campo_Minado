package mc322_campo_minado;

/**
 * Gerencia o fluxo do jogo, orquestrando Board, Player e Bet.
 */
public class Game {
    private final Board board;        // tabuleiro do jogo
    private final Player player;      // jogador com saldo para apostas
    private Bet bet;                  // aposta atual da rodada
    private boolean isGameOver;       // flag indicando fim de rodada
    private boolean hintUsed;         // flag indicando se dica paga foi usada
    private int safeCellsRevealed;    // contador de jogadas seguras


    /**
     * Construtor: inicializa jogo com dimensões e número de minas.
     *
     * @param rows      número de linhas
     * @param cols      número de colunas
     * @param mineCount quantidade de minas
     */
    public Game(int rows, int cols, int mineCount) {
        this.board = new Board(rows, cols, mineCount);
        this.player = new Player(1000.0);  // define saldo inicial padrão
        this.isGameOver = false;
        this.hintUsed = false;
        this.safeCellsRevealed = 0; // inicia contador de células seguras reveladas
    }

    /**
     * Inicia uma nova rodada de jogo com o valor de aposta informado.
     * A chamada a placeBet valida se o jogador tem saldo suficiente,
     * mas não deduz nada de imediato. A dedução ocorre em caso de perda.
     *
     * @param betAmount valor apostado pelo jogador
     * @throws IllegalArgumentException se valor de aposta for inválido
     */
    public void startGame(double betAmount) {
        // valida a aposta (confirma saldo suficiente)
        player.placeBet(betAmount);

        // deduz o valor da aposta do saldo do jogador
        player.loseBet(betAmount);

        // gera novo tabuleiro com minas
        board.generateBoard();
        // inicializa o objeto Bet para controlar multiplicador e payout
        this.bet = new Bet(betAmount);
        // reseta a flag de fim de jogo
        this.isGameOver = false;
    }

    /**
     * Revela uma célula no tabuleiro e atualiza estado do jogo.
     *
     * @param r índice da linha da célula
     * @param c índice da coluna da célula
     * @return true se a célula não for mina (continua jogando),
     *         false se for mina (roda terminará)
     */
    public boolean revealCell(int r, int c) {
        Cell cell = board.getCell(r, c);

        // ignora cliques em células já reveladas ou após fim de jogo
        if (cell.isRevealed() || isGameOver) {
            return true;
        }

        // marca a célula como revelada
        cell.reveal();

        if (cell.hasMine()) {
            //player.loseBet(bet.getInitialBet());
            // ao clicar em mina, deduz a aposta do saldo e encerra
            isGameOver = true;
            return false;
        }

         // Incrementa o número de células seguras reveladas
        safeCellsRevealed++;

        // célula segura: atualiza multiplicador
        int safeCells = board.getRemainingSafeCells();
        bet.increaseMultiplier(safeCells, board.getRemainingMines());

        // se nenhuma célula segura restante, o jogador venceu
        if (safeCells == 0) {
            isGameOver = true;
        }

        return true;
    }

    /**
     * Retorna o número de células seguras reveladas nesta rodada.
     * Incrementa a cada revelação de célula segura.
     *
     * @return número de células seguras reveladas
     */
    public int getSafeCellsRevealed() {
        return safeCellsRevealed;
    }


    /**
     * Permite ao jogador sacar os ganhos atuais e encerra a rodada.
     *
     * @return valor sacado (initialBet × currentMultiplier)
     */
    public double cashOut() {
        double payout = bet.getCurrentPayout();
        // adiciona o valor ganho (incluindo stake) ao saldo
        player.addWinnings(payout);
        isGameOver = true;
        return payout;
    }

    /**
     * Verifica se a rodada/jogo terminou (por mina ou vitória).
     *
     * @return true se terminou, false caso contrário
     */
    public boolean checkGameOver() {
        return isGameOver;
    }

    /**
     * @return objeto Player para consulta de saldo
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return objeto Bet para consulta de multiplicador e payout
     */
    public Bet getBet() {
        return bet;
    }

    /**
     * @return objeto Board para consulta do tabuleiro
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Verifica se uma dica paga foi usada nesta rodada.
     *
     * @return true se a dica foi usada, false caso contrário
     */
    public boolean isHintUsed() {
        return hintUsed;
    }

    /**
     * Define se uma dica paga foi usada nesta rodada.
     *
     * @param used true se a dica foi usada, false caso contrário
     */
    public void setHintUsed(boolean used) {
        this.hintUsed = used;
    }

}
