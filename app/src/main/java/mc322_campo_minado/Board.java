package mc322_campo_minado;

import java.util.ArrayList;

/**
 * Representa o tabuleiro de jogo, com uma matriz de Cells e minas posicionadas aleatoriamente.
 */
public class Board {
    private int rows;                                      // número de linhas
    private int cols;                                      // número de colunas
    private int totalMines;                                // quantidade total de minas
    private Cell[][] cells;                                // matriz de células
    private ArrayList<Cell> mineList;                      // lista de células que contêm minas
    private MineGenerationStrategy mineGenerationStrategy; // estratégia de geração de minas

    /**
     * Construtor: inicializa dimensões do tabuleiro, total de minas
     * e cria todas as células não reveladas.
     *
     * @param rows       número de linhas
     * @param cols       número de colunas
     * @param totalMines quantidade de minas
     */
    public Board(int rows, int cols, int totalMines) {
        this.rows = rows;
        this.cols = cols;
        this.totalMines = totalMines;
        this.cells = new Cell[rows][cols];
        this.mineList = new ArrayList<>();

        // Inicializa todas as células sem minas e não reveladas
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[r][c] = new Cell();
            }
        }
    
        // Define a estratégia de geração de minas padrão (aleatória)
        this.mineGenerationStrategy = new RandomMineGenerationStrategy();
    }

    /**
     * Define a estratégia de geração de minas a ser usada.
     * Deve ser chamada antes de gerar o tabuleiro.
     * 
     * Modo Padrão:
     * Board board = new Board(10, 10, 20);
     * board.generateBoard();
     * 
     * Modo Teste:
     * Board board = new Board(5, 5, 3);
     * board.setMineGenerationStrategy(
     *    new FixedMineGenerationStrategy(new int[][]{{0,0},{2,3},{4,4}})
     * );
     * board.generateBoard();
     *
     * @param strategy objeto que implementa a interface MineGenerationStrategy
     */
    public void setMineGenerationStrategy(MineGenerationStrategy strategy) {
        this.mineGenerationStrategy = strategy;
    }

    /**
     * Gera o tabuleiro de jogo, limpando a lista de minas e chamando a estratégia
     * de geração de minas para posicionar as minas.
     */
    public void generateBoard() {
        mineList.clear();
        mineGenerationStrategy.generateMines(this);
    }

    /**
     * Retorna a célula na posição especificada.
     *
     * @param row índice da linha
     * @param col índice da coluna
     * @return objeto Cell na posição (row, col)
     */
    public Cell getCell(int row, int col) {
        return cells[row][col];
    }

    /**
     * Conta quantas células seguras (sem minas) ainda não foram reveladas.
     *
     * @return número de células seguras restantes
     */
    public int getRemainingSafeCells() {
        int count = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = cells[r][c];
                if (!cell.hasMine() && !cell.isRevealed()) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Retorna o número total de minas configuradas neste tabuleiro.
     *
     * @return quantidade de minas
     */
    public int getRemainingMines() {
        return totalMines;
    }

    /**
     * Retorna a lista de todas as células que foram marcadas com mina.
     *
     * @return ArrayList de Cells com minas
     */
    public ArrayList<Cell> getMineList() {
        return mineList;
    }

    /**
     * Retorna o número de linhas do tabuleiro.
     *
     * @return número de linhas(rows)
     */
    public int getRows() {
        return rows;
    }

    /**
     * Retorna o número de colunas do tabuleiro.
     *
     * @return número de colunas(cols)
     */
    public int getCols() {
        return cols;
    }
}
