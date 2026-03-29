package charlie.server.bot;

import charlie.dealer.Seat;

/**
 * A bot implementation that extends {@link Huey} and overrides its seating behavior.
 *
 * <p>This bot always chooses to sit in the {@link Seat#LEFT} position,
 * regardless of the seat provided.</p>
 */
public class Dewey extends Huey {

    /**
     * Assigns a seat to the bot.
     *
     * <p>This implementation ignores the provided seat and always sets
     * the bot's seat to {@link Seat#LEFT}.</p>
     *
     * @param seat the seat passed in (ignored in this implementation)
     */
    @Override
    public void sit(Seat seat) {
        this.mySeat = Seat.LEFT;
    }

}