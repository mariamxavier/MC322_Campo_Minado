package mc322_campo_minado;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Painel de configuração inicial do jogo Campo Minado.
 * Permite ao usuário definir:
 * <ul>
 *   <li>Tamanho do tabuleiro (N x N)</li>
 *   <li>Quantidade de minas</li>
 *   <li>Valor da aposta</li>
 * </ul>
 * Contém o botão "Start" para iniciar o jogo com as configurações escolhidas.
 */
public class SetupPanel extends JPanel {
    /** Campo de texto para inserir o tamanho do tabuleiro (N) */
    private JTextField sizeField;
    /** Campo de texto para inserir o número de minas */
    private JTextField mineField;
    /** Campo de texto para inserir o valor da aposta */
    private JTextField betField;
    /** Botão para iniciar o jogo */
    private JButton startButton;

    /**
     * Construtor: configura layout, componentes e registra o listener do botão "Start".
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
     * Retorna o tamanho do tabuleiro informado pelo usuário.
     *
     * @return tamanho do tabuleiro (N)
     * @throws NumberFormatException se o valor não for um número válido
     */
    public int getBoardSize() {
        return Integer.parseInt(sizeField.getText());
    }

    /**
     * Retorna o número de minas informado pelo usuário.
     *
     * @return quantidade de minas
     * @throws NumberFormatException se o valor não for um número válido
     */
    public int getMineCount() {
        return Integer.parseInt(mineField.getText());
    }

    /**
     * Retorna o valor da aposta informado pelo usuário.
     *
     * @return valor da aposta
     * @throws NumberFormatException se o valor não for um número válido
     */
    public double getBetAmount() {
        return Double.parseDouble(betField.getText());
    }

    /**
     * Habilita ou desabilita o botão "Start".
     *
     * @param enabled true para habilitar, false para desabilitar
     */
    public void setStartEnabled(boolean enabled) {
        startButton.setEnabled(enabled);
    }
}