package charlie.server.bot;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Dealer;
import charlie.dealer.Seat;
import charlie.plugin.IBot;
import charlie.util.Play;
import mitch.client.BasicStrategy;
import java.util.concurrent.ThreadLocalRandom;
import java.util.List;

public class Huey implements IBot, Runnable {

    Seat mySeat;
    Hand myHand = null;
    Hand dealerHand = null;
    Card upCard;
    BasicStrategy bs = new BotBasicStrategy();
    Dealer dealer;

    @Override
    public Hand getHand() {
        Hid hid = new Hid(this.mySeat);
        this.myHand = new Hand(hid);
        return this.myHand;
    }

    @Override
    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }


    @Override
    public void sit(Seat seat) {
        this.mySeat = Seat.RIGHT;
    }

    @Override
    public void startGame(List<Hid> list, int i) {

    }

    @Override
    public void endGame(int i) {

    }

    @Override
    public void deal(Hid hid, Card card, int[] values) {
        if(hid.getSeat() == mySeat) {
            // do nothing
        }
        else if(hid.getSeat() == Seat.DEALER) {
            if(upCard == null) {
                upCard = card;
            }
        }
    }

    @Override
    public void insure() {

    }

    @Override
    public void bust(Hid hid) {

    }

    @Override
    public void win(Hid hid) {

    }

    @Override
    public void blackjack(Hid hid) {

    }

    @Override
    public void charlie(Hid hid) {

    }

    @Override
    public void lose(Hid hid) {

    }

    @Override
    public void push(Hid hid) {

    }

    @Override
    public void shuffling() {

    }

    private void randomDelay() {
        // random duration between 2000ms and 3000ms (average ~2500ms)
        long delay = ThreadLocalRandom.current().nextLong(2000, 3001);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void play(Hid hid) {
        if(hid.getSeat() != mySeat) return;

        // Start a thread that will handle the whole sequence of actions
        new Thread(() -> {
            boolean finished = false;
            while(!finished) {
                Play play = bs.getPlay(this.myHand, this.upCard);

                randomDelay(); // optional, makes it feel human

                switch(play) {
                    case STAY:
                        dealer.stay(this, hid);
                        finished = true; // hand is done
                        break;
                    case HIT:
                        dealer.hit(this, hid);
                        // loop again to decide next play
                        break;
                    case DOUBLE_DOWN:
                        dealer.doubleDown(this, hid);
                        finished = true; // double down ends turn
                        break;
                }
            }
        }).start();
    }

    @Override
    public void split(Hid hid, Hid hid1) {

    }

    @Override
    public void run() {

    }
}
