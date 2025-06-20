package mc322_campo_minado;

import javax.swing.*;
import java.awt.*;

/**
 * Painel para exibir informações de status do jogo:
 * - Saldo do jogador
 * - Multiplicador atual
 * - Mensagem de status (Jogando, Game Over, Você venceu, etc.)
 */
public class StatusPanel extends JPanel {
    private JLabel balanceLabel;         // exibe o saldo atual
    private JLabel multiplierLabel;      // exibe o multiplicador em tempo real
    private JLabel statusLabel;          // exibe a mensagem de status

    /**
     * Construtor: inicializa o layout e os rótulos padrão.
     */
    public StatusPanel() {
        super(new FlowLayout(FlowLayout.LEFT));
        balanceLabel = new JLabel("Balance: --");
        multiplierLabel = new JLabel("Multiplier: --");
        statusLabel = new JLabel("Status: Ready");

        add(balanceLabel);
        add(multiplierLabel);
        add(statusLabel);
    }

    /**
     * Atualiza o rótulo de saldo.
     * @param value novo saldo
     */
    public void updateBalance(double value) {
        balanceLabel.setText("Balance: " + String.format("%.2f", value));
    }

    /**
     * Atualiza o rótulo de multiplicador.
     * @param value novo multiplicador
     */
    public void updateMultiplier(double value) {
        multiplierLabel.setText("Multiplier: " + String.format("%.2f", value));
    }

    /**
     * Atualiza o rótulo de status.
     * @param msg nova mensagem de status
     */
    public void updateStatus(String msg) {
        statusLabel.setText("Status: " + msg);
    }
}
