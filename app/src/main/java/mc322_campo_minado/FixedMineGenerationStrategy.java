package mc322_campo_minado;

public class FixedMineGenerationStrategy implements MineGenerationStrategy {
    private final int[][] minePositions;

    /**
     * Construtor: recebe uma matriz de posições fixas para as minas.
     *
     * @param minePositions array de pares [linha, coluna] onde cada mina será colocada
     */
    public FixedMineGenerationStrategy(int[][] minePositions) {
        this.minePositions = minePositions;
    }

    @Override
    public void generateMines(Board board) {
        board.getMineList().clear(); // Limpa a lista de minas antes de gerar novas
        for (int[] pos : minePositions) {
            int r = pos[0];
            int c = pos[1];
            Cell cell = board.getCell(r, c);
            cell.setMine(true);
            board.getMineList().add(cell);
        }
    }
}
