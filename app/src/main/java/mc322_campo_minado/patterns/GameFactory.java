package mc322_campo_minado.patterns;

import mc322_campo_minado.Game;

public class GameFactory {
    public static Game createGame(int rows, int cols, int mines, double balance) {
        Game game = new Game(rows, cols, mines);
        game.getPlayer().setBalance(balance);
        return game;
    }
}
