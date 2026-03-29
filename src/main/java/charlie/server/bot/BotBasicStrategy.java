package charlie.server.bot;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.util.Play;
import mitch.client.BasicStrategy;

/**
 * A bot implementation that extends {@link BasicStrategy} and slightly modifies
 * its behavior, specifically handling certain split scenarios differently.
 *
 * <p>This class overrides the default decision-making to adjust how splits are
 * handled based on the total value of the hand.</p>
 */
public class BotBasicStrategy extends BasicStrategy {

    /**
     * Determines the optimal play for the bot given its current hand and the dealer's upcard.
     *
     * <p>This method first defers to the base {@link BasicStrategy} implementation.
     * If the recommended play is not a split, it is returned immediately. Otherwise,
     * custom logic is applied to override splitting behavior in certain cases:</p>
     *
     * <ul>
     *   <li>If the hand value is 4, the bot will {@link Play#HIT} instead of splitting.</li>
     *   <li>If the hand value is 8 or less, {@code doSection2} logic is applied.</li>
     *   <li>If the hand value is 12 or greater, {@code doSection1} logic is applied.</li>
     * </ul>
     *
     * @param myHand the bot's current hand
     * @param upCard the dealer's visible card
     * @return the chosen {@link Play} (e.g., HIT, STAND, SPLIT, etc.)
     */
    @Override
    public Play getPlay(Hand myHand, Card upCard) {

        Play play = super.getPlay(myHand, upCard);
        if (play != Play.SPLIT) {
            return play;
        }

        // Override split behavior for specific hand values
        if (myHand.getValue() == 4) {
            return Play.HIT;
        }

        if (myHand.getValue() <= 8) {
            return doSection2(myHand, upCard);
        }

        if (myHand.getValue() >= 12) {
            return doSection1(myHand, upCard);
        }

        return play;
    }
}