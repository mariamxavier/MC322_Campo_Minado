package mc322_campo_minado.patterns;

public class DefaultHintFeeStrategy implements HintFeeStrategy {
    @Override
    public double calculate(double currentPayout) {
        return currentPayout * 0.25;
    }
}
