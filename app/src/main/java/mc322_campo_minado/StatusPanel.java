package mc322_campo_minado;

import javax.swing.*;
import java.awt.*;

/**
 * Painel para exibir informações de status do jogo Campo Minado.
 * Mostra o saldo do jogador, multiplicador atual, payout e mensagens de status.
 * Atualiza automaticamente quando notificado pelo Player (Observer).
 */
public class StatusPanel extends JPanel implements Observer {
    private JLabel balanceLabel;      // exibe o saldo atual do jogador
    private JLabel multiplierLabel;   // exibe o multiplicador em tempo real
    private JLabel statusLabel;       // exibe a mensagem de status do jogo
    private JLabel payoutLabel;       // exibe o valor de payout da aposta
    private Player player;            // referência ao jogador para atualização do saldo

    /**
     * Construtor: inicializa o layout e os rótulos padrão.
     */
    public StatusPanel() {
        super(new FlowLayout(FlowLayout.LEFT));
        balanceLabel = new JLabel("Balance: --");
        multiplierLabel = new JLabel("Multiplier: --");
        statusLabel = new JLabel("Status: Ready");
        payoutLabel = new JLabel("Payout: --");

        add(balanceLabel);
        add(multiplierLabel);
        add(statusLabel);
        add(payoutLabel);
    }

    /**
     * Atualiza o rótulo de multiplicador.
     * @param value novo multiplicador a ser exibido
     */
    public void updateMultiplier(double value) {
        multiplierLabel.setText("Multiplier: " + String.format("%.2f", value));
    }

    /**
     * Atualiza o rótulo de status do jogo.
     * @param msg nova mensagem de status a ser exibida
     */
    public void updateStatus(String msg) {
        statusLabel.setText("Status: " + msg);
    }

    /**
     * Atualiza o rótulo de payout (pagamento potencial da aposta).
     * @param value novo valor de payout a ser exibido
     */
    public void updatePayout(double value) {
        payoutLabel.setText("Payout: " + String.format("%.2f", value));
    }

    /**
     * Define o jogador a ser observado pelo painel.
     * Remove o painel da lista de observadores do jogador anterior (se houver)
     * e adiciona ao novo jogador, atualizando o saldo exibido.
     *
     * @param player novo jogador a ser observado
     */
    public void setPlayer(Player player) {
        if (this.player != null) {
            this.player.removeObserver(this);
        }
        this.player = player;
        this.player.addObserver(this);
        update();     // exibe o saldo atual imediatamente
    }

    /**
     * Método chamado automaticamente quando o saldo do jogador muda.
     * Atualiza o rótulo de saldo com o valor atual.
     */
    @Override
    public void update() {
        double balance = player.getBalance();
        balanceLabel.setText(String.format("Balance: %.2f", balance));
    }
}