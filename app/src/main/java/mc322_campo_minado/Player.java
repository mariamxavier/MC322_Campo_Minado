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
     * Registra uma aposta, deduzindo o valor do saldo.
     * 
     * @param amount valor apostado
     * @throws IllegalArgumentException se amount for maior que o saldo
     */
    public void placeBet(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Valor da aposta deve ser maior que zero");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar a aposta");
        }
        balance -= amount;
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
     * Trata a perda da aposta. 
     * Neste modelo genérico, a aposta já é deduzida em placeBet(),
     * então este método não aplica nova dedução.
     * Pode ser expandido para efeitos colaterais (estatísticas, histórico, etc.).
     * 
     * @param amount valor que foi perdido (igual ao valor apostado)
     */
    public void loseBet(double amount) {
        // A lógica de débito já ocorreu em placeBet().
        // Aqui podemos registrar estatísticas ou notificações, se desejar.
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
