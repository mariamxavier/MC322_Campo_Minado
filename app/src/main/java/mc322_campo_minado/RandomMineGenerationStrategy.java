package mc322_campo_minado;

import java.util.Random;

/**
 * Estratégia de geração de minas que posiciona as minas aleatoriamente no tabuleiro.
 * Garante que cada célula só receba uma mina e não sobrescreve minas já existentes.
 */
public class RandomMineGenerationStrategy implements MineGenerationStrategy {
    private final Random random = new Random(); // gerador de números aleatórios

    /**
     * Gera as minas em posições aleatórias no tabuleiro.
     * Limpa a lista de minas antes de adicionar novas posições.
     *
     * @param board tabuleiro onde as minas serão posicionadas
     */
    @Override
    public void generateMines(Board board) {
        board.getMineList().clear(); // Limpa a lista de minas antes de gerar novas
        int minesLeft = board.getRemainingMines();
        int rows = board.getRows();
        int cols = board.getCols();

        // Continua até posicionar todas as minas necessárias
        while (minesLeft > 0) {
            int r = random.nextInt(rows); // sorteia linha
            int c = random.nextInt(cols); // sorteia coluna
            Cell cell = board.getCell(r, c);

            // Só posiciona mina se a célula ainda não tiver uma
            if (!cell.hasMine()) {
                cell.setMine(true);
                board.getMineList().add(cell);
                minesLeft--; 
            }
        }
    }
}