/*
 * Copyright (c) Ron Coleman
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package mitch.test.client;

import charlie.actor.Courier;
import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.plugin.IUi;
import charlie.test.framework.Perfect;
import java.util.List;

/**
 * This class is the minimalist perfect  test case.
 * @author Ron.Coleman
 */
public class PerfectSideBet extends Perfect implements IUi {
    Hid you;
    Boolean myTurn = false;

    Hand playerHand;
    Hand dealerHand;

    int gameNo = 1;
    double bankroll = 1000.0;

    /**
     * Runs the test.
     */
    public void test() throws Exception {
        System.setProperty("charlie.sidebet.rule", "mitch.sidebet.rule.SideBetRule");
        System.setProperty("charlie.shoe", "mitch.sidebet.test.Shoe_lab08");
        // Starts the server and logs in using only defaults
        go(this);

        // Game 1 doesn't use a sidebet
        // Rest of games in endGame()
        bet(25,0);
        // Wait for dealer to call end of game.
        assert await(120000);

        // Games 2-10 use the same bet amt

        // End of scope closes sockets which shuts down client and server.
        info("DONE! Final Bankroll: $" + bankroll);
    }

    /**
     * This method gets invoked whenever a card is dealt.
     * @param hid Target hand
     * @param card Card
     * @param handValues Hand value and soft value
     */
    @Override
    public void deal(Hid hid, Card card, int[] handValues) {
        info("DEAL: "+hid+" card: "+card+" hand values: "+handValues[0]+", "+handValues[1]);

        if (card != null) {
            if (hid.getSeat() == Seat.YOU)
                playerHand.hit(card);
            else
                dealerHand.hit(card);
        }

        if ((myTurn == true) && (hid.getSeat() == Seat.YOU))
            play(hid);
    }

    /**
     * This method gets invoked only once whenever the turn changes.
     * @param hid New hand's turn
     */
    @Override
    public void play(Hid hid) {
        if (hid.getSeat() != Seat.YOU) {
            myTurn = false;
            return;
        }
        myTurn = true;

        // Games 1-5, player hits once then stays
        if (gameNo <= 5) {
            if (playerHand.size() == 2) {
                hit(you);
            } else {
                stay(you);
            }
        }
        // Games 6-10, player stays
        else {
            stay(you);
        }
    }

    /**
     * This method gets invoked if a hand breaks.
     * @param hid Target hand
     */
    @Override
    public void bust(Hid hid) {
        // Possible if You or Dealer breaks but it will be one or the other.
        info("BREAK: "+hid);
    }

    /**
     * This method gets invoked for a winning hand.
     * @param hid Target hand
     */
    @Override
    public void win(Hid hid) {
        info("WIN: "+hid);
        if (hid.getSeat() != Seat.YOU) return;

        switch (gameNo) {
            case 2, 9:
                assert hid.getAmt() == 25.0;
                assert hid.getSideAmt() == 30.0;
                bankroll += 55.0;
                break;
            case 3, 7, 10:
                assert hid.getAmt() == 25.0;
                assert hid.getSideAmt() == -10.0;
                bankroll += 15.0;
                break;
            case 6:
                assert hid.getAmt() == 25.0;
                assert hid.getSideAmt() == 250.0;
                bankroll += 275.0;
                break;
            case 8:
                assert hid.getAmt() == 25.0;
                assert hid.getSideAmt() == 10.0;
                bankroll += 35.0;
                break;
            default:
                assert false;
        }
    }

    /**
     * This method gets invoked for a losing hand.
     * @param hid Target hand
     */
    @Override
    public void lose(Hid hid) {
        info("LOSE: "+hid);
        if (hid.getSeat() != Seat.YOU) return;

        switch (gameNo) {
            case 4:
                assert hid.getAmt() == -25.0;
                assert hid.getSideAmt() == 30.0; // Side bet Super 7 Won
                bankroll += 5.0;
                break;
            case 5:
                assert hid.getAmt() == -25.0;
                assert hid.getSideAmt() == -10.0; // Side bet Lost
                bankroll -= 35.0;
                break;
            default:
                assert false; // Should not reach lose in other games
        }
    }

    /**
     * This method gets invoke for a hand that pushes, ie, has same value as dealer's hand.
     * @param hid Target hand
     */
    @Override
    public void push(Hid hid) {
        info("PUSH: "+hid);

        if (gameNo == 1) {
            assert hid.getSeat() == Seat.YOU;
            assert hid.getAmt() == 0.0;
            assert hid.getSideAmt() == 0.0;
        } else {
            assert false;
        }
    }

    /**
     * This method gets invoked for a (natural) Blackjack hand, Ace+K, Ace+Q, etc.
     * @param hid Target hand
     */
    @Override
    public void blackjack(Hid hid) {
        // Not possible for this test case.
        assert false;
    }

    /**
     * This method gets invoked for a 5-card Charlie hand.
     * @param hid Target hand
     */
    @Override
    public void charlie(Hid hid) {
        // Not possible for this test case.
        assert false;
    }

    /**
     * This method get invoked at the start of a game before any cards are dealt.
     * @param hids Hands in the game
     * @param shoeSize Current shoe size, ie, original shoe less cards dealt
     */
    @Override
    public void startGame(List<Hid> hids, int shoeSize) {
        StringBuilder buffer = new StringBuilder();

        buffer.append("game STARTING: ");

        for(Hid hid: hids) {
            buffer.append(hid).append(", ");
            if(hid.getSeat() == Seat.YOU) {
                this.you = hid;
                playerHand = new Hand(hid);
            } else if (hid.getSeat() == Seat.DEALER) {
                dealerHand = new Hand(hid);
            }
        }
        buffer.append(" shoe size: ").append(shoeSize);
        info(buffer.toString());
    }

    /**
     * This method gets invoked after a game ends and before the start of a new game.
     * @param shoeSize Endind shoe size
     */
    @Override
    public void endGame(int shoeSize) {
        if (gameNo < 10) {
            gameNo++;
            bet(25, 10);
        }
        else {
            signal();
        }
    }

    /**
     * This method gets invoked when the burn card appears, it indicates a
     * re-shuffle is coming after the current game ends.
     */
    @Override
    public void shuffling() {
        info("SHUFFLING");
    }

    /**
     * This method sets the courier.
     * It's not used here because the base test case instantiates a courier for us.
     * @param courier Courier
     */
    @Override
    public void setCourier(Courier courier) {
    }

    /**
     * This method gets invoked when a player requests a split.
     * For instance, a 4+4 split results in two hands, each with two cards,
     * 4+x and 4+y where "x" and "y" are hits to each hand which the dealer
     * automatically performs, respectively.
     * @param newHid New hand split from the original.
     * @param origHid Original hand.
     */
    @Override
    public void split(Hid newHid, Hid origHid) {
        // Not possible for this test case.
        assert false;
    }

    /**
     * Handles insurance requests.
     */
    @Override
    public void insure() {
        // Insurance not supported.
        assert false;
    }
}