package mitch.test.bs.invalid;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.util.Play;
import junit.framework.TestCase;
import mitch.client.BasicStrategy;

/**
 * Tests invalid test case when hand is null.
 * @author Mitchell.Levy
 */
public class TestNullHand extends TestCase {
    /**
     * Runs the test.
     */
    public void test() {

        Hand myHand = null;

        Card upCard = new Card(2,Card.Suit.HEARTS);

        BasicStrategy strategy = new BasicStrategy();

        // Play should match the basic strategy.
        Play play = strategy.getPlay(myHand, upCard);

        // This throws an exception if play.NONE is not the expected Play.
        assert play == Play.NONE;
    }
}