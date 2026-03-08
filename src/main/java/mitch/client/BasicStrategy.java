/*
 * Copyright (c) 2026 Hexant, LLC
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package mitch.client;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.util.Play;

/**
 * This class is an incomplete starter implementation of the Basic Strategy.
 * <p>It is table-driven, missing most of the rules and all validation.
 * @author Mitchell.Levy
 * Driver: Mitchell Levy
 * Navigator: Weston LaBrecque
 * Professor Coleman
 * Lab 5
 */
public class BasicStrategy {
    // These help make table formatting compact to look like the pocket card.
    public final static Play P = Play.SPLIT;
    public final static Play H = Play.HIT;
    public final static Play S = Play.STAY;
    public final static Play D = Play.DOUBLE_DOWN;

    /** Rules for section 1; see Instructional Services (2000) pocket card */
    Play[][] section1Rules = {
            /*         2  3  4  5  6  7  8  9  T  A  */
            /* 21 */ { S, S, S, S, S, S, S, S, S, S },
            /* 20 */ { S, S, S, S, S, S, S, S, S, S },
            /* 19 */ { S, S, S, S, S, S, S, S, S, S },
            /* 18 */ { S, S, S, S, S, S, S, S, S, S },
            /* 17 */ { S, S, S, S, S, S, S, S, S, S },
            /* 16 */ { S, S, S, S, S, H, H, H, H, H },
            /* 15 */ { S, S, S, S, S, H, H, H, H, H },
            /* 14 */ { S, S, S, S, S, H, H, H, H, H },
            /* 13 */ { S, S, S, S, S, H, H, H, H, H },
            /* 12 */ { H, H, S, S, S, H, H, H, H, H }
    };

    /** Rules for section 2; see Instructional Services (2000) pocket card */
    Play[][] section2Rules = {
            /*         2  3  4  5  6  7  8  9  T  A  */
            /* 11 */ { D, D, D, D, D, D, D, D, D, H },
            /* 10 */ { D, D, D, D, D, D, D, D, H, H },
            /*  9 */ { H, D, D, D, D, H, H, H, H, H },
            /*  8 */ { H, H, H, H, H, H, H, H, H, H },
            /*  7 */ { H, H, H, H, H, H, H, H, H, H },
            /*  6 */ { H, H, H, H, H, H, H, H, H, H },
            /*  5 */ { H, H, H, H, H, H, H, H, H, H }
    };

    /** Rules for section 3; see Instructional Services (2000) pocket card */
    // NOTE: the row number refers to the non-ACE card that the player has been dealt.
    Play[][] section3Rules = {
            /*         2  3  4  5  6  7  8  9  T  A  */
            /* 10 */ { S, S, S, S, S, S, S, S, S, S },
            /*  9 */ { S, S, S, S, S, S, S, S, S, S },
            /*  8 */ { S, S, S, S, S, S, S, S, S, S },
            /*  7 */ { S, D, D, D, D, S, S, H, H, H },
            /*  6 */ { H, D, D, D, D, H, H, H, H, H },
            /*  5 */ { H, H, D, D, D, H, H, H, H, H },
            /*  4 */ { H, H, D, D, D, H, H, H, H, H },
            /*  3 */ { H, H, H, D, D, H, H, H, H, H },
            /*  2 */ { H, H, H, D, D, H, H, H, H, H }
    };

    /** Rules for section 4; see Instructional Services (2000) pocket card */
    // NOTE: the row number refers to the card that we have a pair of.
    Play[][] section4Rules = {
            /*         2  3  4  5  6  7  8  9  T  A  */
            /*  A */ { P, P, P, P, P, P, P, P, P, P },
            /* 10 */ { S, S, S, S, S, S, S, S, S, S },
            /*  9 */ { P, P, P, P, P, S, P, P, S, S },
            /*  8 */ { P, P, P, P, P, P, P, P, P, P },
            /*  7 */ { P, P, P, P, P, P, H, H, H, H },
            /*  6 */ { P, P, P, P, P, H, H, H, H, H },
            /*  5 */ { D, D, D, D, D, D, D, D, H, H },
            /*  4 */ { H, H, H, P, P, H, H, H, H, H },
            /*  3 */ { P, P, P, P, P, P, H, H, H, H },
            /*  2 */ { P, P, P, P, P, P, H, H, H, H }
    };

    /**
     * Gets the play for player's hand vs. dealer up-card.
     * @param hand Hand player hand
     * @param upCard Dealer up-card
     * @return Play based on basic strategy
     */
    public Play getPlay(Hand hand, Card upCard) {

        if(!isValid(hand, upCard)) {
            return Play.NONE;
        }

        Card card1 = hand.getCard(0);
        Card card2 = hand.getCard(1);

        if(hand.isPair()) {
            return doSection4(hand,upCard);
        }
        else if(hand.size() == 2 && (card1.getRank() == Card.ACE || card2.getRank() == Card.ACE)) {
            return doSection3(hand,upCard);
        }
        else if(hand.getValue() >=5 && hand.getValue() < 12) {
            return doSection2(hand,upCard);
        }
        else if(hand.getValue() >= 12)
            return doSection1(hand,upCard);

        return Play.NONE;
    }

    /**
     * Does section 1 processing of the basic strategy, 12-21 (player) vs. 2-A (dealer)
     * @param hand Player's hand
     * @param upCard Dealer's up-card
     */
    protected Play doSection1(Hand hand, Card upCard) {
        int value = hand.getValue();

        // Section 1 now supports all hands
        if(value < 12 || value > 21)
            return Play.NONE;

        // TODO: Complete getting the row in the table.

        // Subtract 21 since the player's hand starts at 21 and we're working
        // our way down through section 1 from index 0.
        int rowIndex = 21 - value;

        Play[] row = section1Rules[rowIndex];

        // TODO: Complete getting the column in the table.

        // Subtract 2 since the dealer's up-card starts at 2
        int colIndex = upCard.getRank() - 2;

        if(upCard.isFace())
            colIndex = 10 - 2;

            // Ace is the 10th card (index 9)
        else if(upCard.isAce())
            colIndex = 9;

        // At this row, col we should have the correct play defined.
        Play play = row[colIndex];

        return play;
    }

    /**
     * Does section 2 processing of the basic strategy, 5-11 (player) vs. 2-A (dealer)
     * @param hand Player's hand
     * @param upCard Dealer's up-card
     */
    protected Play doSection2(Hand hand, Card upCard) {
        int value = hand.getValue();

        // Section 2 now supports all hands
        if(value < 5 || value > 11)
            return Play.NONE;

        // TODO: Complete getting the row in the table.

        // Subtract 11 since the player's hand starts at 11, and we're working
        // our way down through section 2 from index 0.
        int rowIndex = 11 - value;

        Play[] row = section2Rules[rowIndex];

        // TODO: Complete getting the column in the table.

        // Subtract 2 since the dealer's up-card starts at 2
        int colIndex = upCard.getRank() - 2;

        if(upCard.isFace())
            colIndex = 10 - 2;

            // Ace is the 10th card (index 9)
        else if(upCard.isAce())
            colIndex = 9;

        // At this row, col we should have the correct play defined.
        Play play = row[colIndex];

        return play;
    }

    /**
     * Does section 3 processing of the basic strategy, A,2 - A,10 (player) vs. 2-A (dealer)
     * @param hand Player's hand
     * @param upCard Dealer's up-card
     */
    protected Play doSection3(Hand hand, Card upCard) {
        Card card1 = hand.getCard(0);
        Card card2 = hand.getCard(1);

        /*
         If we are in section 3, we know we have an ACE and another card.
         This variable will hold the value of the other non-ace card.
         */
        int nonAceValue;

        if(card1.getRank() == Card.ACE) {
            nonAceValue = card2.getRank();
        }
        else {
            nonAceValue = card1.getRank();
        }

        // Section 3 now supports all hands
        if(nonAceValue < 2 || nonAceValue > 10)
            return Play.NONE;

        // TODO: Complete getting the row in the table.

        // Subtract 10 since the player's hand starts at 10, and we're working
        // our way down through section 3 from index 0.
        int rowIndex = 10 - nonAceValue;

        Play[] row = section3Rules[rowIndex];

        // TODO: Complete getting the column in the table.

        // Subtract 2 since the dealer's up-card starts at 2
        int colIndex = upCard.getRank() - 2;

        if(upCard.isFace())
            colIndex = 10 - 2;

            // Ace is the 10th card (index 9)
        else if(upCard.isAce())
            colIndex = 9;

        // At this row, col we should have the correct play defined.
        Play play = row[colIndex];

        return play;
    }

    protected Play doSection4(Hand hand, Card upCard) {
        Card card1 = hand.getCard(0);
        Card card2 = hand.getCard(1);

        /*
         If we are in section 4, we know we have a pair. This variable will be the value.
         EX: If pairValue is 8, then we have two 8s.
         */
        int pairValue = card1.getRank();

        // Section 3 now supports all hands
        if(card1.getRank() != card2.getRank())
            return Play.NONE;

        // TODO: Complete getting the row in the table.

        // Subtract 11 since the player's hand starts at 11, and we're working
        // our way down through section 4 from index 0.
        int rowIndex = 11 - pairValue;

        if(card1.isAce())
            rowIndex = 0;

        Play[] row = section4Rules[rowIndex];

        // TODO: Complete getting the column in the table.

        // Subtract 2 since the dealer's up-card starts at 2
        int colIndex = upCard.getRank() - 2;

        if(upCard.isFace())
            colIndex = 10 - 2;

            // Ace is the 10th card (index 9)
        else if(upCard.isAce())
            colIndex = 9;

        // At this row, col we should have the correct play defined.
        Play play = row[colIndex];

        return play;
    }

    /**
     * Validates a hand and up-card.
     * @param hand Hand
     * @param upCard Up-card
     * @return True if both are valid, false otherwise
     */
    boolean isValid(Hand hand, Card upCard) {
        return isValid(hand) && isValid(upCard);
    }

    /**
     * Validates a hand.
     * @param hand Hand
     * @return True if valid, false otherwise
     */
    boolean isValid(Hand hand) {
        if(hand == null || hand.size() < 2 || hand.size() > 5 || hand.getValue() >= 21) {
            return false;
        }
        return true;
    }

    /**
     * Validates a card
     * @param card Card
     * @return True if valid, false otherwise
     */
    boolean isValid(Card card) {
        if(card == null || card.value() < 0 || card.value() > 13) {
            return false;
        }
        return true;
    }
}