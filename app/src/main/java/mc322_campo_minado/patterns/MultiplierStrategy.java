package mc322_campo_minado.patterns;

public interface MultiplierStrategy {
    double calculate(double currentMultiplier, int safeCells, int remainingMines);
}