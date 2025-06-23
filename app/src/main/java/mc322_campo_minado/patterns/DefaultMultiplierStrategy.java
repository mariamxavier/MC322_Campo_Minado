package mc322_campo_minado.patterns;

public class DefaultMultiplierStrategy implements MultiplierStrategy {
    @Override
    public double calculate(double currentMultiplier, int safeCells, int remainingMines) {
        if (safeCells <= 0) return currentMultiplier;
        int totalUnknown = safeCells + remainingMines;
        double factor = (double) totalUnknown / safeCells;
        return currentMultiplier * factor;
    }
}
