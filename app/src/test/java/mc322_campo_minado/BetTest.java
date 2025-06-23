package mc322_campo_minado;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testa comportamento inicial da classe Bet.
 */
class BetTest {

    @Test
    void constructor_ShouldInitializeMultiplier() {
        // Arrange: cria uma Bet de R$100
        Bet bet = new Bet(100.0);

        // Act: obtém o multiplicador atual sem nenhum ajuste
        double multiplier = bet.getCurrentMultiplier();

        // Assert: espera-se que o multiplicador inicial seja exatamente 0.4
        assertEquals(0.4, multiplier, 1e-6, 
            "Multiplicador inicial deve ser 0.4x");
    }

        @Test
    void getCurrentPayout_ShouldReturnInitialBetTimesMultiplier() {
        // Arrange: aposta de R$100 → multiplicador inicial 0.4 → payout esperado 40
        Bet bet = new Bet(100.0);

        // Act: obtém o payout sem ter aumentado o multiplicador
        double payout = bet.getCurrentPayout();

        // Assert: payout deve ser initialBet * currentMultiplier = 100 * 0.4 = 40
        assertEquals(40.0, payout, 1e-6,
            "Payout deve ser o valor da aposta vezes o multiplicador atual");
    }

        @Test
    void getHintFee_ShouldReturnSeventyFivePercentOfPayout() {
        // Arrange: aposta 100, multiplicador inicial 0.4 → payout 40
        Bet bet = new Bet(100.0);

        // Act: calcula a taxa de hint (75% do payout)
        double fee = bet.getHintFee();

        // Assert: 75% de 40 é 30
        assertEquals(30.0, fee, 1e-6,
            "Hint fee deve ser 75% do payout atual");
    }

        @Test
    void applyHintFee_ShouldDeductFeeFromPayout() {
        // Arrange: aposta 100, payout inicial 40, fee = 30
        Bet bet = new Bet(100.0);
        double initialPayout = bet.getCurrentPayout();
        double fee = bet.getHintFee();

        // Act: aplica a taxa de hint
        bet.applyHintFee();
        double newPayout = bet.getCurrentPayout();

        // Assert: novo payout = initialPayout - fee
        assertEquals(initialPayout - fee, newPayout, 1e-6,
            "applyHintFee deve descontar a hint fee do payout");
    }


}


