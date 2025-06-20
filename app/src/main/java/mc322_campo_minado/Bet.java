package mc322_campo_minado;

/**
 * Representa uma aposta genérica, armazenando o valor inicial
 * e um multiplicador que pode ser ajustado conforme o jogo avança.
 */
public class Bet {
    private double initialBet;       // valor inicialmente apostado
    private double currentMultiplier; // multiplicador atual de ganho

    /**
     * Construtor: define o valor da aposta e inicializa multiplicador em 1x.
     *
     * @param initialBet valor apostado
     */
    public Bet(double initialBet) {
        if (initialBet <= 0) {
            throw new IllegalArgumentException("Valor de aposta deve ser maior que zero");
        }
        this.initialBet = initialBet;
        this.currentMultiplier = 1.0;
    }

    /**
     * Aumenta o multiplicador com base em parâmetros genéricos do jogo.
     * Aqui você pode aplicar qualquer fórmula — por enquanto apenas
     * incrementamos em 0.5 para testes.
     *
     * @param safeCells      número de células seguras restantes (não usado neste stub)
     * @param remainingMines número de minas restantes (não usado neste stub)
     */
    public void increaseMultiplier(int safeCells, int remainingMines) {
        // Exemplo simples de ajuste de multiplicador para testes:
        this.currentMultiplier += 0.5;
    }

    /**
     * Retorna o valor atual que seria sacado pelo jogador:
     * initialBet * currentMultiplier.
     *
     * @return valor de payout
     */
    public double getCurrentPayout() {
        return initialBet * currentMultiplier;
    }

    /**
     * Retorna o valor originalmente apostado.
     *
     * @return initialBet
     */
    public double getInitialBet() {
        return initialBet;
    }

    /**
     * Retorna o multiplicador atual.
     *
     * @return currentMultiplier
     */
    public double getCurrentMultiplier() {
        return currentMultiplier;
    }

    /**
     * Redefine o multiplicador para 1x.
     * Útil para reiniciar entre rodadas de teste.
     */
    public void resetMultiplier() {
        this.currentMultiplier = 1.0;
    }
}
