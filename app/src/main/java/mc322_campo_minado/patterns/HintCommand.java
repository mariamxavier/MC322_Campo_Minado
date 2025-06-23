package mc322_campo_minado.patterns;

import mc322_campo_minado.*;

public class HintCommand implements Command {
    private final MinesweeperUI ui;

    public HintCommand(MinesweeperUI ui) {
        this.ui = ui;
    }

    @Override
    public void execute() {
        ui.handleHint();
    }
}
