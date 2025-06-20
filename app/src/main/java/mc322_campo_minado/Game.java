package mc322_campo_minado;

/**
 * Gerencia o fluxo do jogo, orquestrando Board, Player e Bet.
 */
public class Game {
    private Board board;        // tabuleiro do jogo
    private Player player;      // jogador com saldo para apostas
    private Bet bet;            // objeto de aposta atual
    private boolean isGameOver; // flag indicando fim de rodada

    /**
     * Construtor: inicializa jogo com dimensões e número de minas.
     * @param rows número de linhas
     * @param cols número de colunas
     * @param mineCount quantidade de minas
     */
    public Game(int rows, int cols, int mineCount) {
        this.board = new Board(rows, cols, mineCount);
        this.player = new Player(1000.0);  // saldo inicial padrão
        this.isGameOver = false;
    }

    /**
     * Inicia uma nova rodada de jogo com o valor de aposta informado.
     * @param betAmount valor apostado pelo jogador
     */
    public void startGame(double betAmount) {
        // Deduz o valor da aposta do saldo do jogador
        player.placeBet(betAmount);
        // Gera novo tabuleiro com minas
        board.generateBoard();
        // Cria um novo objeto Bet para essa rodada
        this.bet = new Bet(betAmount);
        // Reseta estado de fim de jogo
        this.isGameOver = false;
    }

    /**
     * Revela uma célula no tabuleiro e atualiza estado do jogo.
     * @param r índice da linha da célula
     * @param c índice da coluna da célula
     * @return true se a célula não for mina, false se for mina
     */
    public boolean revealCell(int r, int c) {
        Cell cell = board.getCell(r, c);

        // Se já revelada ou jogo acabou, ignora clique
        if (cell.isRevealed() || isGameOver) {
            return true;
        }

        // Marca a célula como revelada
        cell.reveal();

        // Se havia mina, fim de jogo e aposta perdida
        if (cell.hasMine()) {
            isGameOver = true;
            player.loseBet(bet.getInitialBet());
            return false;
        }

        // Célula segura: recalcula multiplicador
        int safeCells = board.getRemainingSafeCells();
        bet.increaseMultiplier(safeCells, board.getRemainingMines());

        // Se não restam células seguras, o jogador venceu
        if (safeCells == 0) {
            isGameOver = true;
        }

        return true;
    }

    /**
     * Permite ao jogador sacar os ganhos atuais e encerra a rodada.
     * @return valor sacado (aposta × multiplicador)
     */
    public double cashOut() {
        double payout = bet.getCurrentPayout();
        // Adiciona o valor ganho ao saldo do jogador
        player.addWinnings(payout);
        // Encerra a rodada
        isGameOver = true;
        return payout;
    }

    /**
     * Verifica se a rodada/jogo terminou.
     * @return true se terminou, false caso contrário
     */
    public boolean checkGameOver() {
        return isGameOver;
    }

    /**
     * Retorna o objeto Player para consulta de saldo.
     * @return jogador atual
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Retorna o objeto Bet para consulta de multiplicador e payout.
     * @return aposta atual
     */
    public Bet getBet() {
        return bet;
    }

    /**
     * Retorna o objeto Board para consulta do tabuleiro.
     * @return tabuleiro atual
     */
    public Board getBoard() {
        return board;
    }
}
