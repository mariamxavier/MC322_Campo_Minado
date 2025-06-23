package mc322_campo_minado;

/**
 * Estratégia de geração de minas que posiciona as minas em locais fixos,
 * definidos previamente por uma matriz de coordenadas.
 * Útil para testes.
 */
public class FixedMineGenerationStrategy implements MineGenerationStrategy {
    private final int[][] minePositions; // pares [linha, coluna] de onde posicionar as minas

    /**
     * Construtor: recebe uma matriz de posições fixas para as minas.
     *
     * @param minePositions array de pares [linha, coluna] onde cada mina será colocada
     */
    public FixedMineGenerationStrategy(int[][] minePositions) {
        this.minePositions = minePositions;
    }

    /**
     * Posiciona exatamente totalMines minas nas posições fornecidas.
     * Se o número de posições fixas for diferente de totalMines,
     * posiciona apenas até o mínimo entre os dois.
     *
     * @param cells      matriz de Cell a receber minas
     * @param totalMines quantidade exata de minas a posicionar
     */
    @Override
    public void generate(Cell[][] cells, int totalMines) {
        // determina quantas minas efetivamente colocar
        int toPlace = Math.min(totalMines, minePositions.length);

        for (int i = 0; i < toPlace; i++) {
            int r = minePositions[i][0];
            int c = minePositions[i][1];
            // valida índices
            if (r >= 0 && r < cells.length && c >= 0 && c < cells[0].length) {
                cells[r][c].setMine(true);
            } else {
                throw new IllegalArgumentException(
                    String.format("Posição de mina fixa inválida: [%d, %d]", r, c));
            }
        }
    }
}
