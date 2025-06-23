package mc322_campo_minado.patterns;

import mc322_campo_minado.*;

public class CashOutCommand implements Command {
    private final MinesweeperUI ui;

    public CashOutCommand(MinesweeperUI ui) {
        this.ui = ui;
    }

    @Override
    public void execute() {
        ui.handleCashOut();
    }
}
