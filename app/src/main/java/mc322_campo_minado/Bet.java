package mc322_campo_minado;

import mc322_campo_minado.patterns.HintFeeStrategy;
import mc322_campo_minado.patterns.MultiplierStrategy;
import mc322_campo_minado.patterns.DefaultHintFeeStrategy;
import mc322_campo_minado.patterns.DefaultMultiplierStrategy;

/**
 * Classe que representa uma aposta no jogo Campo Minado.
 * Gerencia o valor inicial da aposta, o multiplicador atual,
 * estratégias para cálculo do multiplicador e taxa de dica.
 */
public class Bet {
    /** Valor inicial apostado pelo jogador */
    private final double initialBet;
    /** Multiplicador atual da aposta */
    private double currentMultiplier;

    /** Estratégia para cálculo do multiplicador */
    private MultiplierStrategy multiplierStrategy;
    /** Estratégia para cálculo da taxa de dica */
    private HintFeeStrategy hintFeeStrategy;

    /**
     * Construtor da aposta.
     * 
     * @param initialBet Valor inicial da aposta
     * @throws IllegalArgumentException se o valor da aposta for menor ou igual a zero
     */
    public Bet(double initialBet) {
        if (initialBet <= 0) {
            throw new IllegalArgumentException("Valor de aposta deve ser maior que zero");
        }
        this.initialBet = initialBet;
        this.currentMultiplier = 0.4;
        this.multiplierStrategy = new DefaultMultiplierStrategy();
        this.hintFeeStrategy = new DefaultHintFeeStrategy();
    }

    /**
     * Atualiza o multiplicador da aposta com base nas células seguras reveladas e minas restantes.
     * 
     * @param safeCells       Número de células seguras reveladas
     * @param remainingMines  Número de minas restantes no tabuleiro
     */
    public void increaseMultiplier(int safeCells, int remainingMines) {
        this.currentMultiplier = multiplierStrategy.calculate(currentMultiplier, safeCells, remainingMines);
    }

    /**
     * Retorna o valor atual do possível pagamento (payout) da aposta.
     * 
     * @return Valor do payout atual
     */
    public double getCurrentPayout() {
        return initialBet * currentMultiplier;
    }

    /**
     * Retorna o valor da taxa para uso da dica, calculada sobre o payout atual.
     * 
     * @return Valor da taxa de dica
     */
    public double getHintFee() {
        return hintFeeStrategy.calculate(getCurrentPayout());
    }

    /**
     * Retorna o valor inicial da aposta.
     * 
     * @return Valor inicial apostado
     */
    public double getInitialBet() {
        return initialBet;
    }

    /**
     * Retorna o multiplicador atual da aposta.
     * 
     * @return Multiplicador atual
     */
    public double getCurrentMultiplier() {
        return currentMultiplier;
    }

    /**
     * Reseta o multiplicador para o valor inicial padrão (0.4).
     */
    public void resetMultiplier() {
        this.currentMultiplier = 0.4;
    }

    /**
     * Define uma nova estratégia para cálculo do multiplicador.
     * 
     * @param strategy Nova estratégia de multiplicador
     */
    public void setMultiplierStrategy(MultiplierStrategy strategy) {
        this.multiplierStrategy = strategy;
    }

    /**
     * Define uma nova estratégia para cálculo da taxa de dica.
     * 
     * @param strategy Nova estratégia de taxa de dica
     */
    public void setHintFeeStrategy(HintFeeStrategy strategy) {
        this.hintFeeStrategy = strategy;
    }

    /**
     * Aplica a taxa de dica (hint fee) ao payout atual.
     * Deduz 25% do payout atual (initialBet * currentMultiplier) como taxa de dica.
     */
    public void applyHintFee() {
        double fee = getHintFee();
        double payout = getCurrentPayout();

        double newPayout = payout - fee;
        this.currentMultiplier = newPayout / initialBet;
    }
}