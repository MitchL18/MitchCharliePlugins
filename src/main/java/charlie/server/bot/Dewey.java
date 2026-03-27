package charlie.server.bot;

import charlie.dealer.Seat;

public class Dewey extends Huey {

    @Override
    public void sit(Seat seat) {
        this.mySeat = Seat.LEFT;
    }

}
