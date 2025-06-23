package mc322_campo_minado;

/**
 * Interface para estratégias de geração de minas no tabuleiro 
 * Permite implementar diferentes formas de posicionamento das minas (aleatório, fixo).
 */
public interface MineGenerationStrategy {
    /**
     * Gera as minas no tabuleiro de acordo com a estratégia implementada.
     *
     * @param board tabuleiro onde as minas serão posicionadas
     */
    void generateMines(Board board);
}