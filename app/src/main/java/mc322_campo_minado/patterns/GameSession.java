package mc322_campo_minado.patterns;

import mc322_campo_minado.Game;

public class GameSession {
    private static GameSession instance;
    private Game currentGame;

    private GameSession() {}

    public static GameSession getInstance() {
        if (instance == null) {
            instance = new GameSession();
        }
        return instance;
    }

    public void setGame(Game game) {
        this.currentGame = game;
    }

    public Game getGame() {
        return currentGame;
    }
}
