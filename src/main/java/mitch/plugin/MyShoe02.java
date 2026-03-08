/*
Driver: Mitchell Levy
Navigator: Weston LaBrecque
Professor Coleman
Software Design and Development
Lab 3
MyShoe02.java
This class is a shoe plugin that initializes the shoe with
10 specific cards.
 */

package mitch.plugin;

import charlie.card.Card;
import charlie.shoe.Shoe;
import charlie.card.Card.Suit;

public class MyShoe02 extends Shoe {

    /*

     */
    @Override public void init() {
        System.out.println("MyShoe02 init running");
        // reset the shoe because we are starting new game
        cards.clear();

        // Add ten specific cards to shoe for testing
        cards.add(new Card(2, Card.Suit.SPADES));
        cards.add(new Card(Card.QUEEN, Suit.HEARTS));
        cards.add(new Card(3, Card.Suit.HEARTS));
        cards.add(new Card(7, Suit.CLUBS));
        cards.add(new Card(4, Card.Suit.SPADES));
        cards.add(new Card(5, Suit.CLUBS));
        cards.add(new Card(6, Suit.DIAMONDS));
        cards.add(new Card(Card.KING, Suit.CLUBS));
        cards.add(new Card(9, Suit.DIAMONDS));
        cards.add(new Card(Card.JACK, Suit.SPADES));
    }
}
