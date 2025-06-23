package mc322_campo_minado;

import javax.swing.*;
import java.awt.*;

/**
 * Painel que renderiza o tabuleiro do jogo usando CellButton para cada célula.
 * Usa CellButtonFactory para criar e configurar os botões e expõe acesso ao array
 * de botões para funcionalidades como hint.
 */
public class BoardPanel extends JPanel {
    private CellButton[][] cellButtons; // matriz de botões que representam as células do tabuleiro

    /**
     * Construtor padrão.
     */
    public BoardPanel() {
        super();
    }

    /**
     * Monta o tabuleiro de acordo com o objeto Board fornecido.
     * Para cada célula, delega a criação ao CellButtonFactory.
     *
     * @param board    objeto Board com as células do jogo
     * @param listener callback chamado em cada clique, recebendo (row, col, button)
     */
    public void buildBoard(Board board, CellClickListener listener) {
        int rows = board.getRows();
        int cols = board.getCols();
        removeAll(); // Remove componentes anteriores
        setLayout(new GridLayout(rows, cols, 4, 4)); // grid com espaçamento

        cellButtons = new CellButton[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                // Cria e configura o botão via fábrica
                CellButton btn = CellButtonFactory.create(r, c, listener);
                btn.reset();                 // garante estado inicial
                cellButtons[r][c] = btn;     // armazena para acesso futuro
                add(btn);                    // adiciona ao painel
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
     * Retorna a matriz de botões do tabuleiro.
     *
     * @return matriz de CellButton
     */
    public CellButton[][] getCellButtons() {
        return cellButtons;
    }

    /**
     * Callback interface para tratar cliques em células do tabuleiro.
     */
    public interface CellClickListener {
        /**
         * Invocado quando o usuário clica em uma célula.
         *
         * @param row    linha da célula clicada
         * @param col    coluna da célula clicada
         * @param button instância de CellButton que recebeu o clique
         */
        void onCellClick(int row, int col, CellButton button);
    }
}
