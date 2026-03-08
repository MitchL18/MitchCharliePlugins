package mitch.test.bs.invalid;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.util.Play;
import junit.framework.TestCase;
import mitch.client.BasicStrategy;

/**
 * Tests invalid test case when hand has 6 cards.
 * @author Mitchell.Levy
 */
public class Test6CardHand extends TestCase {
    /**
     * Runs the test.
     */
    public void test() {

        // Hand needs a hid which we can generate with a seat.
        Hand myHand = new Hand(new Hid(Seat.YOU));

        // Put 6 cards in the hand, only rank matters, not suit.
        myHand.hit(new Card(2, Card.Suit.CLUBS));
        myHand.hit(new Card(2, Card.Suit.DIAMONDS));
        myHand.hit(new Card(3, Card.Suit.CLUBS));
        myHand.hit(new Card(3, Card.Suit.DIAMONDS));
        myHand.hit(new Card(4, Card.Suit.CLUBS));
        myHand.hit(new Card(4, Card.Suit.SPADES));

        Card upCard = new Card(5,Card.Suit.HEARTS);

        BasicStrategy strategy = new BasicStrategy();

        // Play should match the basic strategy.
        Play play = strategy.getPlay(myHand, upCard);

        // This throws an exception if play.NONE is not the expected Play.
        assert play == Play.NONE;
    }
}