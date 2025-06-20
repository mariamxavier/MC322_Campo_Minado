package mc322_campo_minado;

import javax.swing.*;
import java.awt.*;

/**
 * Painel que renderiza o tabuleiro do jogo.
 * Cria botões para cada célula e delega eventos de clique.
 */
public class BoardPanel extends JPanel {
    private JButton[][] cellButtons;

    /**
     * Construtor padrão.
     */
    public BoardPanel() {
        super();
    }

    /**
     * Monta um tabuleiro quadrado de tamanho rows x cols.
     * Cada botão recebe um listener que informa linha, coluna e referência do botão.
     *
     * @param rows    quantidade de linhas
     * @param cols    quantidade de colunas
     * @param listener callback chamado em cada clique, recebendo (row, col, button)
     */
    public void buildBoard(int rows, int cols, CellClickListener listener) {
        removeAll();
        setLayout(new GridLayout(rows, cols));
        cellButtons = new JButton[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                JButton btn = new JButton();
                cellButtons[r][c] = btn;
                final int rr = r, cc = c;
                btn.addActionListener(e -> listener.onCellClick(rr, cc, btn));
                add(btn);
            }
        }

        revalidate();
        repaint();
    }

    /**
     * Revela todas as minas no tabuleiro: exibe "💣" e desabilita botões.
     *
     * @param board objeto Board para consultar localização das minas
     */
    public void revealAllMines(Board board) {
        int rows = board.getRows();
        int cols = board.getCols();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                JButton btn = cellButtons[r][c];
                if (board.getCell(r, c).hasMine()) {
                    btn.setText("💣");
                }
                btn.setEnabled(false);
            }
        }
    }

    /**
     * Interface para callback de clique em célula.
     */
    public interface CellClickListener {
        void onCellClick(int row, int col, JButton button);
    }
}
