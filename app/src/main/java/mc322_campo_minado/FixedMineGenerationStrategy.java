package mc322_campo_minado;

/**
 * Estratégia de geração de minas que posiciona as minas em locais fixos,
 * definidos previamente por uma matriz de coordenadas.
 * Útil para testes
 */
public class FixedMineGenerationStrategy implements MineGenerationStrategy {
    private final int[][] minePositions; // matriz de pares [linha, coluna] para posicionamento fixo das minas

    /**
     * Construtor: recebe uma matriz de posições fixas para as minas.
     *
     * @param minePositions array de pares [linha, coluna] onde cada mina será colocada
     */
    public FixedMineGenerationStrategy(int[][] minePositions) {
        this.minePositions = minePositions;
    }

    /**
     * Gera as minas no tabuleiro nas posições fixas especificadas.
     * Limpa a lista de minas do tabuleiro antes de adicionar as novas posições.
     *
     * @param board tabuleiro onde as minas serão posicionadas
     */
    @Override
    public void generateMines(Board board) {
        board.getMineList().clear(); // Limpa a lista de minas antes de gerar novas
        for (int[] pos : minePositions) {
            int r = pos[0];
            int c = pos[1];
            Cell cell = board.getCell(r, c);
            cell.setMine(true); // Marca a célula como mina
            board.getMineList().add(cell); // Adiciona à lista de minas do tabuleiro
        }
    }
}