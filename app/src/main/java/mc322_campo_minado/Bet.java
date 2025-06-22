package mc322_campo_minado;

/**
 * Representa uma aposta genérica, armazenando o valor inicial
 * e um multiplicador que cresce conforme o jogador revela células seguras.
 * Inclui método para cálculo de taxa de dica (hint).
 */
public class Bet {
    private double initialBet;        // valor apostado inicialmente
    private double currentMultiplier; // multiplicador atual de payout

    /**
     * Construtor: define o valor inicial da aposta e inicializa multiplicador em 0.5x.
     *
     * @param initialBet valor apostado
     */
    public Bet(double initialBet) {
        if (initialBet <= 0) {
            throw new IllegalArgumentException("Valor de aposta deve ser maior que zero");
        }
        this.initialBet = initialBet;
        this.currentMultiplier = 0.4;  
    }

    /**
     * Aumenta o multiplicador com base no número de casas seguras restantes
     * e no número de minas não reveladas.
     * Aplica a fórmula:
     *   multiplicadorNovo = multiplicadorAtual * (casasRestantes + minasRestantes) / casasRestantes
     * onde (casasRestantes + minasRestantes) é o total de células não reveladas.
     *
     * @param safeCells      número de casas sem mina ainda não reveladas
     * @param remainingMines número de minas ainda não reveladas
     */
    public void increaseMultiplier(int safeCells, int remainingMines) {
        if (safeCells <= 0) {
            return; // nada a multiplicar se não restam casas seguras
        }
        int totalUnknown = safeCells + remainingMines;
        double factor = (double) totalUnknown / safeCells;
        this.currentMultiplier *= factor;
    }

    /**
     * Retorna o valor total que seria recebido ao sacar:
     * initialBet * currentMultiplier.
     *
     * @return valor de payout atual
     */
    public double getCurrentPayout() {
        return initialBet * currentMultiplier;
    }

    /**
     * Retorna o valor originalmente apostado.
     *
     * @return aposta inicial
     */
    public double getInitialBet() {
        return initialBet;
    }

    /**
     * Retorna o multiplicador atual.
     *
     * @return multiplicador acumulado
     */
    public double getCurrentMultiplier() {
        return currentMultiplier;
    }

    /**
     * Calcula a taxa para uso de dica (hint).
     * A taxa é 10% do payout atual (initialBet * currentMultiplier).
     *
     * @return valor da taxa de hint
     */
    public double getHintFee() {
        return getCurrentPayout() * 0.25; // taxa de 25% do payout atual
    }

    /**
     * Redefine o multiplicador para 0.5x (útil para reiniciar entre rodadas).
     */
    public void resetMultiplier() {
        this.currentMultiplier = 0.4;  
    }
}
