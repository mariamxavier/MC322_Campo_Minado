package mc322_campo_minado;

import javax.swing.*;
import java.awt.*;

/**
 * Painel para exibir informações de status do jogo:
 * - Saldo do jogador
 * - Multiplicador atual
 * - Mensagem de status (Jogando, Game Over, Você venceu, etc.)
 * - Payout
 */
public class StatusPanel extends JPanel implements Observer {
    private JLabel balanceLabel;         // exibe o saldo atual
    private JLabel multiplierLabel;      // exibe o multiplicador em tempo real
    private JLabel statusLabel;          // exibe a mensagem de status
    private JLabel payoutLabel;          // exibe o valor de payout
    private Player player;               // referência ao jogador para atualizar o saldo

    /**
     * Construtor: inicializa o layout e os rótulos padrão.
     */
    public StatusPanel() {
        super(new FlowLayout(FlowLayout.LEFT));
        balanceLabel = new JLabel("Balance: --");
        multiplierLabel = new JLabel("Multiplier: --");
        statusLabel = new JLabel("Status: Ready");
        payoutLabel     = new JLabel("Payout: --");

        add(balanceLabel);
        add(multiplierLabel);
        add(statusLabel);
        add(payoutLabel);
    }

    /**
     * Atualiza o rótulo de saldo.
     * @param value novo saldo
     */
    /*public void updateBalance(double value) {
        balanceLabel.setText("Balance: " + String.format("%.2f", value));
    }*/

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

    /**
     * Atualiza o rótulo de payout.
     * @param value novo valor de payout
     */
    public void updatePayout(double value) {
        payoutLabel.setText("Payout: " + String.format("%.2f", value));
    }

    public void setPlayer(Player player) {
        if (this.player != null) {
            this.player.removeObserver(this);
        }
        this.player = player;
        this.player.addObserver(this);
        update();     // exibe o saldo atual
    }

    @Override
    public void update() {
        double balance = player.getBalance();
        balanceLabel.setText(String.format("Saldo: %.2f", balance));
    }
}
