package charlie.server.bot;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.util.Play;
import mitch.client.BasicStrategy;

public class BotBasicStrategy  extends BasicStrategy {

    @Override
    public Play getPlay(Hand myHand, Card upCard) {

        Play play = super.getPlay(myHand, upCard);
        if (play != Play.SPLIT) {
            return play;
        }

        if(myHand.getValue() == 4) {
            return Play.HIT;
        }

        if(myHand.getValue() <= 8) {
            return doSection2(myHand, upCard);
        }

        if(myHand.getValue() >= 12) {
            return doSection1(myHand, upCard);
        }
        System.out.println("BIGGGGGGG OOOOOFFFFF");
        return play;
    }
}
