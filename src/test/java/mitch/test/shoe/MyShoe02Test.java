/*
Driver: Mitchell Levy
Navigator: Weston LaBrecque
Professor Coleman
Software Design and Development
Lab 3
MyShoe02Test.java
This class uses JUnit to test a class.
 */

package mitch.test.shoe;

import charlie.card.Card;
import charlie.plugin.IShoe;
import junit.framework.TestCase;
import mitch.plugin.MyShoe02;

public class MyShoe02Test extends TestCase {

    /*
        Look for bugs in the shoe initialization.
        Check the shoe size and first two cards.
     */
    public void test() {
        // shoe size check
        IShoe shoe = new MyShoe02();
        shoe.init();
        assert shoe.size() == 10;

        // card1 check
        Card card1 = shoe.next();
        assert card1.getRank() == 2;

        // card2 check
        Card card2 = shoe.next();
        assert card2.getRank() == Card.QUEEN;
    }
}
