package mc322_campo_minado;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa cada célula do tabuleiro.
 * Contém informações sobre se a célula possui mina e se foi revelada.
 */
public class Cell {
    private boolean hasMine;    // indica presença de mina na célula
    private boolean isRevealed; // indica se a célula já foi revelada
    private List<Observer> observers = new ArrayList<>(); // lista de observadores para notificações

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
        notifyObservers(); // Notifica observadores sobre a revelação
    }

    /**
     * Verifica se a célula já foi revelada.
     * @return true se já revelada, false caso contrário
     */
    public boolean isRevealed() {
        return isRevealed;
    }

    /**
     * Registra um observador para notificações sobre mudanças nesta célula.
     * @param observer objeto que implementa a interface Observer
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Notifica todos os observadores sobre mudanças na célula.
     * Chama o método update() em cada um dos observadores.
     */
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
