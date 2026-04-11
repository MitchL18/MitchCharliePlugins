package mitch.sidebet.rule;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.plugin.ISideBetRule;
import org.apache.log4j.Logger;

/**
 * This class implements the side bet rule for Super 7.
 * @author Mitchell Levy
 */
public class SideBetRule implements ISideBetRule {
    private final Logger LOG = Logger.getLogger(SideBetRule.class);

    private final double PAYOFF_SUPER7 = 3.0;
    private final double PAYOFF_ROYALMATCH = 25.0;
    private final double PAYOFF_EXACTLY13 = 1.0;

    /**
     * Apply rule to the hand and return the payout if the rule matches
     * and the negative bet if the rule does not match.
     * @param hand Hand to analyze.
     * @return
     */
    @Override
    public double apply(Hand hand) {

        double bet = hand.getHid().getSideAmt();
        LOG.info("side bet amount = " + bet);

        if (bet == 0)
            return 0.0;

        LOG.info("side bet rule applying hand = " + hand);

        // Prevent crash if fewer than 2 cards
        if (hand.size() < 2) {
            LOG.warn("Side bet evaluated before 2 cards were dealt: " + hand);
            return 0.0; // or -bet depending on system design
        }

        Card card1 = hand.getCard(0);
        Card card2 = hand.getCard(1);

        // Check for super 7
        if (card1.getRank() == 7) {
            LOG.info("side bet SUPER 7 matches");
            return bet * PAYOFF_SUPER7;
        }

        // Check for Royal Match
        boolean isKingQueen =
                (card1.getRank() == Card.KING && card2.getRank() == Card.QUEEN) ||
                        (card1.getRank() == Card.QUEEN && card2.getRank() == Card.KING);

        boolean sameSuit = card1.getSuit() == card2.getSuit();

        if (isKingQueen && sameSuit) {
            LOG.info("side bet ROYAL MATCH matches");
            return bet * PAYOFF_ROYALMATCH;
        }

        int hardValue = 0;

        for (int i = 0; i < hand.size(); i++) {
            Card c = hand.getCard(i);
            int rank = c.getRank();

            if (rank == Card.ACE) {
                hardValue += 1;
            } else if (rank >= Card.JACK) {
                hardValue += 10;
            } else {
                hardValue += rank;
            }
        }

        if (hardValue == 13) {
            LOG.info("side bet EXACTLY 13 matches");
            return bet * PAYOFF_EXACTLY13;
        }

        LOG.info("side bet rule no match");

        return -bet;
    }
}