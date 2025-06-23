package mc322_campo_minado;

import java.util.Random;

public class RandomMineGenerationStrategy implements MineGenerationStrategy {
    private final Random random = new Random();

    @Override
    public void generateMines(Board board) {
        board.getMineList().clear(); // Limpa a lista de minas antes de gerar novas
        int minesLeft = board.getRemainingMines();
        int rows = board.getRows();
        int cols = board.getCols();

        while (minesLeft > 0) {
            int r = random.nextInt(rows);
            int c = random.nextInt(cols);
            Cell cell = board.getCell(r, c);

            if (!cell.hasMine()) {
                cell.setMine(true);
                board.getMineList().add(cell);
                minesLeft--;
            }
        }
    }
    
}
