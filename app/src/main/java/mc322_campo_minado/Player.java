package mc322_campo_minado;

/**
 * Classe que representa o jogador do Campo Minado.
 * Responsável por gerenciar o saldo do jogador e as operações de aposta, perda e ganho.
 */
public class Player {
    /** Saldo disponível do jogador para apostas */
    private double balance;

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
     * Valida uma aposta, sem deduzir imediatamente do saldo.
     * A dedução ocorre apenas se o jogador perder.
     *
     * @param amount valor apostado
     * @throws IllegalArgumentException se o valor for inválido ou maior que o saldo disponível
     */
    public void placeBet(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Valor da aposta deve ser maior que zero");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar a aposta");
        }
        // A dedução do saldo ocorre apenas em loseBet().
    }

    /**
     * Deduz o valor da aposta do saldo do jogador em caso de perda.
     *
     * @param amount valor perdido (igual ao valor apostado)
     * @throws IllegalArgumentException se o valor de perda for negativo
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
     * Adiciona os ganhos ao saldo do jogador após um saque (cash out).
     *
     * @param amount valor ganho a ser adicionado ao saldo
     * @throws IllegalArgumentException se o valor de ganhos for negativo
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
     * @throws IllegalArgumentException se o novo saldo for negativo
     */
    public void setBalance(double newBalance) {
        if (newBalance < 0) {
            throw new IllegalArgumentException("Saldo não pode ser negativo");
        }
        this.balance = newBalance;
    }
}