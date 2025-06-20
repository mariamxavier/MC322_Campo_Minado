package mc322_campo_minado;

/**
 * Representa o jogador, mantendo e gerenciando seu saldo para apostas.
 */
public class Player {
    private double balance;  // saldo disponível do jogador

    /**
     * Construtor: inicializa o jogador com um saldo inicial.
     *
     * @param initialBalance valor inicial de crédito do jogador
     */
    public Player(double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Saldo inicial não pode ser negativo");
        }
        this.balance = initialBalance;
    }

    /**
     * Valida uma aposta sem deduzir imediatamente do saldo.
     * A dedução ocorre apenas se o jogador perder.
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
        // Não deduzimos aqui: o valor será deduzido em loseBet() se perder.
    }

    /**
     * Trata a perda da aposta, deduzindo o valor do saldo.
     *
     * @param amount valor que foi perdido (igual ao valor apostado)
     */
    public void loseBet(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Valor de perda não pode ser negativo");
        }
        if (amount >= balance) {
            balance = 0;
        } else {
            balance -= amount;
        }
    }

    /**
     * Adiciona os ganhos ao saldo do jogador após um saque.
     *
     * @param amount valor ganho a ser adicionado ao saldo
     */
    public void addWinnings(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Valor de ganhos não pode ser negativo");
        }
        balance += amount;
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
     *
     * @param newBalance novo valor de saldo
     */
    public void setBalance(double newBalance) {
        if (newBalance < 0) {
            throw new IllegalArgumentException("Saldo não pode ser negativo");
        }
        this.balance = newBalance;
    }
}
