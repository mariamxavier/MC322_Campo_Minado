package mc322_campo_minado.patterns;

import mc322_campo_minado.*;

public class RevealCommand implements Command {
    private final int row;
    private final int col;
    private final CellButton button;
    private final MinesweeperUI ui;

    public RevealCommand(int row, int col, CellButton button, MinesweeperUI ui) {
        this.row = row;
        this.col = col;
        this.button = button;
        this.ui = ui;
    }

    @Override
    public void execute() {
        ui.handleReveal(row, col, button);
    }
}
