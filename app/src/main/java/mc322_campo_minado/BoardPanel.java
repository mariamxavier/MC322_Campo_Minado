package mc322_campo_minado;

import javax.swing.*;
import java.awt.*;

/**
 * Painel que renderiza o tabuleiro do jogo usando CellButton para cada célula.
 * Cria botões para cada célula e delega eventos de clique.
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
     * Cria um CellButton para cada célula e associa um listener de clique.
     *
     * @param board    objeto Board com as células do jogo
     * @param listener callback chamado em cada clique, recebendo (row, col, button)
     */
    public void buildBoard(Board board, CellClickListener listener) {
        int rows = board.getRows();
        int cols = board.getCols();
        removeAll(); // Remove todos os componentes antigos do painel

        // Define o layout em grade com espaçamento entre as células
        setLayout(new GridLayout(rows, cols, 4, 4));
        cellButtons = new CellButton[rows][cols]; // inicializa matriz de botões

        // Cria os botões do tabuleiro
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = board.getCell(r, c);
                CellButton btn = new CellButton(cell);
                btn.reset(); // Garante que o botão está no estado inicial (sem ícone, habilitado)
                cellButtons[r][c] = btn; // armazena referência do botão na matriz
                final int rr = r, cc = c;
                // Listener para capturar o clique e informar a posição e o botão
                btn.addActionListener(e -> listener.onCellClick(rr, cc, btn));
                add(btn); // Adiciona o botão ao painel
            }
        }

        revalidate(); // Atualiza o layout do painel após adicionar/remover componentes
        repaint();    // Redesenha o painel
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
                // Se a célula contém mina, exibe o ícone de mina
                if (board.getCell(r, c).hasMine()) {
                    btn.showMine();
                }
                // Desabilita todos os botões ao final do jogo
                btn.setEnabled(false);
            }
        }
    }

    /**
     * Interface para callback de clique em célula.
     * Permite que o painel informe ao controlador qual célula foi clicada.
     */
    public interface CellClickListener {
        /**
         * Método chamado ao clicar em uma célula.
         *
         * @param row    Linha da célula clicada
         * @param col    Coluna da célula clicada
         * @param button Referência ao CellButton clicado
         */
        void onCellClick(int row, int col, CellButton button);
    }

    /**
     * Retorna a matriz de botões do tabuleiro.
     *
     * @return matriz de CellButton
     */
    public CellButton[][] getCellButtons() {
        return cellButtons;
    }
}