package mitch.client;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.plugin.IAdvisor;
import charlie.util.Play;

/**
 * Advisor plugin for the Charlie blackjack client.
 * <p>
 * Implements the {@link IAdvisor} interface, providing advice
 * on what play to make based on the current hand and dealer's up card.
 * Internally, this class delegates to {@link BasicStrategy} to determine
 * the optimal play according to standard blackjack strategy.
 * </p>
 * <p>
 * Charlie consults this advisor before each player action. If the
 * player's action matches the advisor's recommendation, Charlie remains silent.
 * Otherwise, Charlie may prompt the player to reconsider.
 * </p>
 */
public class Advisor implements IAdvisor {

    /** Internal BasicStrategy instance used to generate advice. */
    BasicStrategy bs = new BasicStrategy();

    /**
     * Provides advice on the next play based on the player's hand
     * and the dealer's up card.
     *
     * @param hand the player's current hand
     * @param upCard the dealer's face-up card
     * @return a {@link Play} enum value indicating the recommended action
     *         (e.g., HIT, STAND, DOUBLE, SPLIT)
     */
    @Override
    public Play advise(Hand hand, Card upCard) {
        return bs.getPlay(hand, upCard);
    }
}