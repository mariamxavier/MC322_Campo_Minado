package mc322_campo_minado;

/**
 * Interface Strategy para geração de minas no tabuleiro.
 * Implementações concretas definem diferentes algoritmos de distribuição.
 */
public interface MineGenerationStrategy {
    /**
     * Posiciona exatamente totalMines minas na matriz de células.
     *
     * @param cells      matriz de Cell a receber minas
     * @param totalMines quantidade exata de minas a posicionar
     */
    void generate(Cell[][] cells, int totalMines);
}
