package mc322_campo_minado;

import java.awt.event.ActionListener;

/**
 * Factory Method para criar e configurar CellButton.
 * Centraliza toda a customização (tema, listeners, etc.).
 */
public class CellButtonFactory {

    /**
     * Cria um CellButton já estilizado e com listener de clique.
     *
     * @param row      índice da linha
     * @param col      índice da coluna
     * @param listener callback a ser chamado no clique
     * @return CellButton pronto para uso
     */
    public static CellButton create(int row, int col, BoardPanel.CellClickListener listener) {
        CellButton btn = new CellButton();
        // configura o listener de clique para delegar ao painel
        btn.addActionListener(e -> listener.onCellClick(row, col, btn));
        return btn;
    }
}
