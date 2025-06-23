package mc322_campo_minado;


/**
 * Representa o tabuleiro do jogo, contendo uma matriz de Cells,
 * o número total de minas e a estratégia de posicionamento.
 */
public class Board {
    private final int rows;                 // número de linhas
    private final int cols;                 // número de colunas
    private final int totalMines;           // quantidade de minas
    private final Cell[][] cells;           // matriz de células
    private final MineGenerationStrategy strategy; // estratégia de distribuição de minas

    /**
     * Construtor padrão: usa RandomMineGenerationStrategy para posicionar minas.
     *
     * @param rows       número de linhas
     * @param cols       número de colunas
     * @param totalMines quantidade de minas
     */
    public Board(int rows, int cols, int totalMines) {
        this(rows, cols, totalMines, new RandomMineGenerationStrategy());
    }

    /**
     * Construtor que recebe uma estratégia de geração de minas.
     *
     * @param rows       número de linhas
     * @param cols       número de colunas
     * @param totalMines quantidade de minas
     * @param strategy   implementação de MineGenerationStrategy
     */
    public Board(int rows, int cols, int totalMines, MineGenerationStrategy strategy) {
        if (rows < 1 || cols < 1)
            throw new IllegalArgumentException("Tabuleiro deve ter ao menos 1 linha e 1 coluna");
        if (totalMines < 0 || totalMines >= rows * cols)
            throw new IllegalArgumentException("Quantidade de minas inválida para o tamanho do tabuleiro");

        this.rows = rows;
        this.cols = cols;
        this.totalMines = totalMines;
        this.strategy = strategy;

        // inicializa todas as células
        cells = new Cell[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[r][c] = new Cell();
            }
        }
    }

    /**
     * Gera o tabuleiro posicionando minas conforme a estratégia escolhida
     * e reseta todas as células não reveladas.
     */
    public void generateBoard() {
        // limpa todas as células
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[r][c] = new Cell();
            }
        }
        // distribui as minas
        strategy.generate(cells, totalMines);
    }

    /**
     * Retorna a célula na posição (row, col).
     *
     * @param row índice da linha
     * @param col índice da coluna
     * @return instância de Cell
     */
    public Cell getCell(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols)
            throw new IndexOutOfBoundsException("Coordenada fora do tabuleiro");
        return cells[row][col];
    }

    /**
     * Conta quantas células seguras (sem mina) ainda não foram reveladas.
     *
     * @return número de células seguras restantes
     */
    public int getRemainingSafeCells() {
        int count = 0;
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                if (!cell.hasMine() && !cell.isRevealed()) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Conta quantas minas ainda não foram reveladas.
     *
     * @return número de minas restantes não reveladas
     */
    public int getRemainingMines() {
        int count = 0;
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                if (cell.hasMine() && !cell.isRevealed()) {
                    count++;
                }
            }
        }
        return count;
    }

    /** @return número de linhas do tabuleiro */
    public int getRows() {
        return rows;
    }

    /** @return número de colunas do tabuleiro */
    public int getCols() {
        return cols;
    }
}
