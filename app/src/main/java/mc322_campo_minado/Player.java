package mc322_campo_minado;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa o jogador, mantendo e gerenciando seu saldo para apostas.
 * Implementa o padrão Observer para notificar componentes interessados
 * sempre que o saldo do jogador for alterado.
 */
public class Player {
    private double balance;  // saldo disponível do jogador
    private final List<Observer> observers = new ArrayList<>(); // lista de observadores para notificação de mudanças no saldo

    /**
     * Construtor: inicializa o jogador com um saldo inicial.
     *
     * @param initialBalance valor inicial de crédito do jogador
     * @throws IllegalArgumentException se o saldo inicial for negativo
     */
    public Player(double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Saldo inicial não pode ser negativo");
        }
        this.balance = initialBalance;
    }

    /**
     * Deduz o valor da aposta do saldo do jogador.
     * Notifica os observadores sobre a alteração no saldo.
     *
     * @param amount valor apostado
     * @throws IllegalArgumentException se amount for inválido ou maior que o saldo
     */
    public void placeBet(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Valor da aposta deve ser maior que zero");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar a aposta");
        }
        balance -= amount; // deduz o valor da aposta do saldo
        notifyObservers(); // notifica os observadores sobre a alteração no saldo

    }

    /**
     * Adiciona os ganhos ao saldo do jogador após um saque.
     * Notifica os observadores após a alteração.
     *
     * @param amount valor ganho a ser adicionado ao saldo
     * @throws IllegalArgumentException se o valor de ganhos for negativo
     */
    public void addWinnings(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Valor de ganhos não pode ser negativo");
        }
        balance += amount;
        notifyObservers();
    }

    /**
     * Retorna o saldo atual do jogador.
     *
     * @return saldo disponível
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Ajusta diretamente o saldo do jogador.
     * Útil para cenários de recarga ou ajustes administrativos.
     * Notifica os observadores após a alteração.
     *
     * @param newBalance novo valor de saldo
     * @throws IllegalArgumentException se o novo saldo for negativo
     */
    public void setBalance(double newBalance) {
        if (newBalance < 0) {
            throw new IllegalArgumentException("Saldo não pode ser negativo");
        }
        this.balance = newBalance;
        notifyObservers();
    }

    /**
     * Adiciona um observador à lista de observadores do jogador.
     *
     * @param observer objeto que será notificado em mudanças de saldo
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Remove um observador da lista de observadores do jogador.
     *
     * @param observer objeto a ser removido da lista de notificações
     */
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notifica todos os observadores registrados sobre uma alteração no saldo.
     */
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }
}