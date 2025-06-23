package mc322_campo_minado;

import java.util.Random;

/**
 * Estratégia de geração aleatória de minas.
 * Sorteia posições únicas até atingir totalMines.
 */
public class RandomMineGenerationStrategy implements MineGenerationStrategy {
    private final Random random = new Random();

    @Override
    public void generate(Cell[][] cells, int totalMines) {
        int rows = cells.length;
        int cols = cells[0].length;
        int placed = 0;

        while (placed < totalMines) {
            int r = random.nextInt(rows);
            int c = random.nextInt(cols);
            Cell cell = cells[r][c];
            if (!cell.hasMine()) {
                cell.setMine(true);
                placed++;
            }
        }
    }
}
