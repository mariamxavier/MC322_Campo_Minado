package mc322_campo_minado;

/**
 * Representa uma aposta genérica, armazenando o valor inicial
 * e um multiplicador que cresce conforme o jogador revela células seguras.
 */
public class Bet {
    private double initialBet;        // valor apostado inicialmente
    private double currentMultiplier;  // multiplicador atual de payout

    /**
     * Construtor: define o valor inicial da aposta e inicializa multiplicador em 1x.
     *
     * @param initialBet valor apostado
     */
    public Bet(double initialBet) {
        if (initialBet <= 0) {
            throw new IllegalArgumentException("Valor de aposta deve ser maior que zero");
        }
        this.initialBet = initialBet;
        this.currentMultiplier = 0.5;
    }

    /**
     * Aumenta o multiplicador com base no número de casas seguras restantes
     * e no número de minas não reveladas.
     * Aplica a fórmula:
     *   multiplicadorNovo = multiplicadorAtual * (casasRestantes + minasRestantes) / casasRestantes
     * onde (casasRestantes + minasRestantes) = total de células não reveladas.
     * Assim, reflete a relação de probabilidade de acertar uma casa segura.
     *
     * @param safeCells       número de casas sem mina ainda não reveladas
     * @param remainingMines  número de minas ainda não reveladas
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
     * Redefine o multiplicador para 1x (útil se quiser reiniciar entre rodadas).
     */
    public void resetMultiplier() {
        this.currentMultiplier = 0.5;
    }
}
