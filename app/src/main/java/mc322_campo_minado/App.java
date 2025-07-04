package mc322_campo_minado;

/**
 * Classe de entrada que lança a interface gráfica do Minesweeper Bet Game.
 */
public class App {
    /**
     * Método usado pelos testes para verificar a classe.
     * @return saudação
     */
    public String getGreeting() {
        return "Salve, Professor! Bem-vindo ao Minesweeper Bet Game!";
    }

    /**
     * Ponto de entrada principal: inicia a UI Swing.
     * @param args argumentos de linha de comando (ignorados)
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(MinesweeperUI::new);
    }
}
