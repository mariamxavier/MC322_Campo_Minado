package mc322_campo_minado;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para a classe Cell.
 */
public class CellTest {
    private Cell cell;
    private AtomicInteger notifyCount;

    @BeforeEach
    void setup() {
        cell = new Cell();
        notifyCount = new AtomicInteger(0);
    }

    @Test
    void initialState_ShouldBeUnrevealedAndNoMine() {
        // Verify initial state
        assertFalse(cell.isRevealed(), "Célula não deve estar revelada ao criar");
        assertFalse(cell.hasMine(), "Célula não deve conter mina ao criar");
    }

    @Test
    void setMine_AndHasMine() {
        // Set a mine and verify
        cell.setMine(true);
        assertTrue(cell.hasMine(), "Célula deve indicar que contém mina após setMine(true)");

        cell.setMine(false);
        assertFalse(cell.hasMine(), "Célula deve indicar que não contém mina após setMine(false)");
    }

    @Test
    void reveal_ShouldNotifyObserverOnce() {
        // Arrange: add observer
        cell.addObserver(() -> notifyCount.incrementAndGet());

        // Act: reveal cell
        cell.reveal();

        // Assert: observer called exactly once
        assertEquals(1, notifyCount.get(), "Observer deve ser notificado uma vez");
    }

    @Test
    void reveal_WhenAlreadyRevealed_DoesNotNotifyAgain() {
        // Arrange: add observer and reveal twice
        cell.addObserver(() -> notifyCount.incrementAndGet());
        cell.reveal();
        cell.reveal();

        // Assert: still only one notification
        assertEquals(1, notifyCount.get(), "Observer não deve ser notificado mais de uma vez");
    }

    @Test
    void removeObserver_ShouldStopNotifications() {
        // Arrange: add then remove observer
        Observer obs = () -> notifyCount.incrementAndGet();
        cell.addObserver(obs);
        cell.removeObserver(obs);

        // Act: reveal
        cell.reveal();

        // Assert: no notification
        assertEquals(0, notifyCount.get(), "Observer removido não deve ser notificado");
    }
}
