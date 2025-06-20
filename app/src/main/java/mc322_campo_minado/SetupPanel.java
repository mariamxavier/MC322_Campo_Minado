package mc322_campo_minado;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Painel de configuração inicial:
 * - Tamanho do tabuleiro (quadrado)
 * - Quantidade de minas
 * - Valor da aposta
 * Contém o botão "Start" para iniciar o jogo.
 */
public class SetupPanel extends JPanel {
    private JTextField sizeField;    // campo para inserir o tamanho N (tabuleiro N x N)
    private JTextField mineField;    // campo para inserir o número de minas
    private JTextField betField;     // campo para inserir o valor da aposta
    private JButton startButton;     // botão para iniciar o jogo

    /**
     * Construtor: configura layout e componentes, registrando listener para "Start".
     *
     * @param startListener ActionListener a ser executado ao clicar em "Start"
     */
    public SetupPanel(ActionListener startListener) {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        add(new JLabel("Size:"));
        sizeField = new JTextField("8", 3);
        add(sizeField);

        add(new JLabel("Mines:"));
        mineField = new JTextField("10", 3);
        add(mineField);

        add(new JLabel("Bet:"));
        betField = new JTextField("100", 5);
        add(betField);

        startButton = new JButton("Start");
        startButton.addActionListener(startListener);
        add(startButton);
    }

    /**
     * @return o tamanho do tabuleiro (N)
     */
    public int getBoardSize() {
        return Integer.parseInt(sizeField.getText());
    }

    /**
     * @return o número de minas configurado
     */
    public int getMineCount() {
        return Integer.parseInt(mineField.getText());
    }

    /**
     * @return o valor da aposta inserido
     */
    public double getBetAmount() {
        return Double.parseDouble(betField.getText());
    }

    /**
     * Habilita ou desabilita o botão Start.
     *
     * @param enabled true para habilitar, false para desabilitar
     */
    public void setStartEnabled(boolean enabled) {
        startButton.setEnabled(enabled);
    }
}
