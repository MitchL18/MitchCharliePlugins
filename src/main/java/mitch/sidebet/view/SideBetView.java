
/*
 Copyright (c) 2014 Ron Coleman

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package mitch.sidebet.view;

import charlie.audio.Effect;
import charlie.audio.SoundFactory;
import charlie.card.Hid;
import charlie.plugin.ISideBetView;
import charlie.view.AMoneyManager;

import charlie.view.sprite.AtStakeSprite;
import charlie.view.sprite.Chip;
import charlie.view.sprite.ChipButton;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import java.util.Random;

/**
 * This class implements the side bet view
 * @author Mitchell Levy
 */
public class SideBetView implements ISideBetView {
    private final Logger LOG = Logger.getLogger(SideBetView.class);

    public final static int X = 400;
    public final static int Y = 200;
    public final static int DIAMETER = 50;

    protected Font font = new Font("Arial", Font.BOLD, 18);
    protected Font payoutFont = new Font("Arial", Font.BOLD, 16);
    protected BasicStroke stroke = new BasicStroke(3);

    // See http://docs.oracle.com/javase/tutorial/2d/geometry/strokeandfill.html
    protected float dash1[] = {10.0f};
    protected BasicStroke dashed
            = new BasicStroke(3.0f,
            BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER,
            10.0f, dash1, 0.0f);

    protected List<ChipButton> buttons;
    protected int amt = 0;
    protected AMoneyManager moneyManager;

    // Corresponding chips equal to the stake
    public final static int PLACE_HOME_X = X + AtStakeSprite.DIAMETER - 13;
    public final static int PLACE_HOME_Y = Y - 20;
    protected Random ran = new Random();
    protected List<Chip> chips = new ArrayList<>();
    protected String outcomeText = "";
    protected Color outcomeBGColor = Color.WHITE;
    protected Color outcomeTextColor = Color.WHITE;

    public SideBetView() {
        LOG.info("side bet view constructed");
    }

    /**
     * Sets the money manager.
     * @param moneyManager
     */
    @Override
    public void setMoneyManager(AMoneyManager moneyManager) {
        this.moneyManager = moneyManager;
        this.buttons = moneyManager.getButtons();
    }

    /**
     * Registers a click for the side bet.
     * @param x X coordinate
     * @param y Y coordinate
     */
    @Override
    public void click(int x, int y) {
        int oldAmt = amt;

        // Test if any chip button has been pressed.
        for(ChipButton button: buttons) {
            if(button.isPressed(x, y)) {
                amt += button.getAmt();

                // Play the sound.
                SoundFactory.play(Effect.CHIPS_IN);

                // Add chip to list of chips.
                int n = chips.size();

                // Figure out where chips need to go.
                int placeX = PLACE_HOME_X + n * 35 / 3 + ran.nextInt(10) - 10;
                int placeY = PLACE_HOME_Y + ran.nextInt(5) - 5;

                Chip chip = new Chip(button.getImage(), placeX, placeY, amt);
                chips.add(chip);

                LOG.info("A. side bet amount "+button.getAmt()+" updated new amt = "+amt);
            }
        }

        // clear the side bet amount
        if(oldAmt == amt) {
            amt = 0;
            chips.clear();
            SoundFactory.play(Effect.CHIPS_OUT);
            LOG.info("B. side bet amount cleared");
        }
    }

    /**
     * Informs view the game is over and it's time to update the bankroll for the hand.
     * @param hid Hand id
     */
    @Override
    public void ending(Hid hid) {
        double bet = hid.getSideAmt();

        if(bet == 0) {
            outcomeText = "";
            return;
        }

        LOG.info("side bet outcome = "+bet);

        // Update the bankroll
        moneyManager.update(bet);

        // Determine outcome
        if (bet > 0) {
            outcomeText = "WIN!";
            outcomeBGColor = Color.GREEN;
            outcomeTextColor = Color.BLACK;
        }
        else if (bet < 0) {
            outcomeText = "LOSE!";
            outcomeBGColor = Color.RED;
            outcomeTextColor = Color.WHITE;
        }

        LOG.info("new bankroll = "+moneyManager.getBankroll());
    }

    /**
     * Informs view the game is starting
     */
    @Override
    public void starting() {
        // clear old side bet outcome text
        outcomeText = "";
    }

    /**
     * Gets the side bet amount.
     * @return Bet amount
     */
    @Override
    public Integer getAmt() {
        return amt;
    }

    /**
     * Updates the view
     */
    @Override
    public void update() {
    }

    /**
     * Renders the view
     * @param g Graphics context
     */
    @Override
    public void render(Graphics2D g) {
        // Draw the at-stake place on the table
        g.setColor(Color.RED);
        g.setStroke(dashed);
        g.drawOval(X-DIAMETER/2, Y-DIAMETER/2, DIAMETER, DIAMETER);

        // Draw the at-stake amount
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(""+amt, X-5, Y+5);

        // Draw Side Bet Payout Info
        g.setColor(Color.YELLOW);
        g.setFont(payoutFont);
        g.drawString("SUPER 7 pays 3:1", X+55, Y-80);
        g.drawString("ROYAL MATCH pays 25:1", X+55, Y-60);
        g.drawString("EXACTLY 13 pays 1:1", X+55, Y-40);

        // Render the chips
        for (Chip chips : chips) {
            chips.render(g);
        }

        // Renders Win or Lose Over Side Bet
        if (!outcomeText.isEmpty() && !chips.isEmpty()) {
            g.setFont(font);

            Chip firstChip = chips.get(0);

            java.awt.FontMetrics fm = g.getFontMetrics(font);
            int w = fm.charsWidth(outcomeText.toCharArray(), 0, outcomeText.length());
            int h = fm.getHeight();

            // Center on first chip
            int chipCenterX = firstChip.getX() + firstChip.getWidth() / 2;
            int chipCenterY = firstChip.getY() + firstChip.getHeight() / 2;

            // Text position centered on first chip
            int textX = (chipCenterX - w / 2) + 5;
            int textY = chipCenterY + h / 4;

            // Draw background
            g.setColor(outcomeBGColor);
            g.fillRoundRect(textX, textY - h + 5, w, h, 5, 5);

            // Draw text
            g.setColor(outcomeTextColor);
            g.drawString(outcomeText, textX, textY);
        }
    }
}
