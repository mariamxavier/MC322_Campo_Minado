package mc322_campo_minado;

import javax.swing.*;
import mc322_campo_minado.patterns.GameSession;
import mc322_campo_minado.patterns.StatusObserver;
import java.awt.*;

/**
 * Painel responsável por exibir informações de status do jogo Campo Minado.
 * Mostra o saldo do jogador, multiplicador atual, payout e mensagens de status.
 * Atualiza automaticamente quando notificado pelo Game (Observer).
 */
public class StatusPanel extends JPanel implements StatusObserver {
    /** Rótulo que exibe o saldo atual do jogador */
    private JLabel balanceLabel;
    /** Rótulo que exibe o multiplicador em tempo real */
    private JLabel multiplierLabel;
    /** Rótulo que exibe a mensagem de status do jogo */
    private JLabel statusLabel;
    /** Rótulo que exibe o valor do payout da aposta */
    private JLabel payoutLabel;

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
     * Atualiza o rótulo de saldo do jogador.
     * @param value novo saldo a ser exibido
     */
    public void updateBalance(double value) {
        balanceLabel.setText("Balance: " + String.format("%.2f", value));
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
     * Método chamado automaticamente quando o status do jogo muda.
     * Atualiza todos os rótulos com as informações atuais do Game.
     */
    @Override
    public void onStatusChanged() {
        Game game = GameSession.getInstance().getGame();
        updateBalance(game.getPlayer().getBalance());
        updateMultiplier(game.getBet().getCurrentMultiplier());
        updatePayout(game.getBet().getCurrentPayout());
    }
}