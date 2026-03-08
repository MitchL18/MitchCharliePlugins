package mitch.test.bs.invalid;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.util.Play;
import junit.framework.TestCase;
import mitch.client.BasicStrategy;

/**
 * Tests invalid test case when hand is blackjack.
 * @author Mitchell.Levy
 */
public class TestBlackJackHand extends TestCase {
    /**
     * Runs the test.
     */
    public void test() {

        // Hand needs a hid which we can generate with a seat.
        Hand myHand = new Hand(new Hid(Seat.YOU));

        // Put three cards in the hand, only rank matters, not suit.
        myHand.hit(new Card(Card.QUEEN, Card.Suit.CLUBS));
        myHand.hit(new Card(Card.KING, Card.Suit.DIAMONDS));
        myHand.hit(new Card(7, Card.Suit.DIAMONDS));

        Card upCard = new Card(2,Card.Suit.HEARTS);

        BasicStrategy strategy = new BasicStrategy();

        // Play should match the basic strategy.
        Play play = strategy.getPlay(myHand, upCard);

        // This throws an exception if play.NONE is not the expected Play.
        assert play == Play.NONE;
    }
}