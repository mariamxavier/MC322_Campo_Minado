package mc322_campo_minado;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma célula do tabuleiro: pode conter mina e manter estado revelado.
 * Notifica observadores quando seu estado muda.
 */
public class Cell {
    private boolean hasMine = false;    // indica se há mina nesta célula
    private boolean isRevealed = false; // indica se a célula já foi revelada

    private final List<Observer> observers = new ArrayList<>();

    /**
     * Marca esta célula como contendo mina.
     * @param mine true para ter mina, false caso contrário
     */
    public void setMine(boolean mine) {
        this.hasMine = mine;
    }

    /**
     * Retorna se a célula contém mina.
     * @return true se há mina
     */
    public boolean hasMine() {
        return hasMine;
    }

    /**
     * Revela a célula, alterando seu estado e notificando observadores.
     * Se já estiver revelada, não faz nada.
     */
    public void reveal() {
        if (!isRevealed) {
            isRevealed = true;
            notifyObservers();
        }
    }

    /**
     * Retorna se a célula já foi revelada.
     * @return true se revelada
     */
    public boolean isRevealed() {
        return isRevealed;
    }

    /**
     * Registra um observador para ser notificado quando o estado mudar.
     * @param observer instância que implementa Observer
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Remove um observador do registro.
     * @param observer instância previamente registrada
     */
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notifica todos os observadores sobre mudança de estado.
     */
    private void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }
}
