package mc322_campo_minado;

/**
 * Classe que representa cada célula do tabuleiro.
 * Contém informações sobre se a célula possui mina e se foi revelada.
 */
public class Cell {
    private boolean hasMine;    // indica presença de mina na célula
    private boolean isRevealed; // indica se a célula já foi revelada

    /**
     * Construtor: inicializa célula sem mina e não revelada.
     */
    public Cell() {
        this.hasMine = false;
        this.isRevealed = false;
    }

    /**
     * Define se a célula possui mina.
     * @param hasMine true para adicionar mina, false para remover
     */
    public void setMine(boolean hasMine) {
        this.hasMine = hasMine;
    }

    /**
     * Retorna se a célula possui mina.
     * @return true se houver mina, false caso contrário
     */
    public boolean hasMine() {
        return hasMine;
    }

    /**
     * Marca a célula como revelada.
     * Uma vez revelada, não pode ser revertida.
     */
    public void reveal() {
        this.isRevealed = true;
    }

    /**
     * Verifica se a célula já foi revelada.
     * @return true se já revelada, false caso contrário
     */
    public boolean isRevealed() {
        return isRevealed;
    }
}
