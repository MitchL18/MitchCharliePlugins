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
public class TestNullCard extends TestCase {
    /**
     * Runs the test.
     */
    public void test() {

        // Hand needs a hid which we can generate with a seat.
        Hand myHand = new Hand(new Hid(Seat.YOU));

        // Put two cards in the hand, only rank matters, not suit.
        myHand.hit(new Card(Card.ACE, Card.Suit.CLUBS));
        myHand.hit(new Card(Card.ACE, Card.Suit.DIAMONDS));

        Card upCard = null;

        BasicStrategy strategy = new BasicStrategy();

        // Play should match the basic strategy.
        Play play = strategy.getPlay(myHand, upCard);

        // This throws an exception if play.NONE is not the expected Play.
        assert play == Play.NONE;
    }
}