package mc322_campo_minado;

import javax.swing.*;
import java.awt.*;

/**
 * Painel que renderiza o tabuleiro do jogo usando CellButton para cada célula.
 * Cria botões para cada célula e delega eventos de clique.
 */
public class BoardPanel extends JPanel {
    private CellButton[][] cellButtons;

    /**
     * Construtor padrão.
     */
    public BoardPanel() {
        super();
    }

    /**
     * Monta um tabuleiro quadrado de tamanho rows x cols.
     * Cada CellButton recebe um listener que informa linha, coluna e referência do botão.
     *
     * @param rows     quantidade de linhas
     * @param cols     quantidade de colunas
     * @param listener callback chamado em cada clique, recebendo (row, col, button)
     */
    public void buildBoard(int rows, int cols, CellClickListener listener) {
        removeAll();
        setLayout(new GridLayout(rows, cols, 4, 4));  // espaços entre células
        cellButtons = new CellButton[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                CellButton btn = new CellButton();
                btn.reset();
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
     * Revela todas as minas no tabuleiro: exibe o ícone de mina e desabilita botões.
     *
     * @param board objeto Board para consultar localização das minas
     */
    public void revealAllMines(Board board) {
        int rows = board.getRows();
        int cols = board.getCols();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                CellButton btn = cellButtons[r][c];
                if (board.getCell(r, c).hasMine()) {
                    btn.showMine();
                }
                btn.setEnabled(false);
            }
        }
    }

    /**
     * Interface para callback de clique em célula.
     */
    public interface CellClickListener {
        void onCellClick(int row, int col, CellButton button);
    }

    /**
     * Retorna a matriz de botões que representam as células do tabuleiro.
     *
     * @return matriz de CellButton
     */

    public CellButton[][] getCellButtons() {
        return cellButtons;
    }

}
