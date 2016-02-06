package solitude;

import acm.graphics.GCompound;
import acm.graphics.GRect;
import java.awt.Color;

/**
 *
 * @author Andrew
 * piles for card directly on top of one another as opposed to stacks
 */
public class Pile extends GCompound {

    public Card[] cards;
    public Card top = null;
    protected int count = 0;

    public Pile() {
        cards = new Card[Card.CARDTOTAL];
        GRect rect = new GRect(109, 152);
        rect.setFilled(true);
        rect.setFillColor(Color.black);
        add(rect, 0, 0);
    }

    public void add(Card card) {
        if (!isFull()) {
            cards[count++] = card;
            top = card;
            add(top, 0, 0);
        }
    }

    public Card remove() {
        if (!isEmpty()) {
            Card returnCard = top;
            cards[count - 1] = null;
            remove(returnCard);
            if (count > 1) {
                top = cards[count - 2];
            } else {
                top = null;
            }
            count--;
            return returnCard;
        }
        return null;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public boolean isFull() {
        return count == Card.CARDTOTAL;
    }

    public boolean isFoundationFull(){
        return count == 13;
    }
    public int size() {
        return count;
    }

    public Card getBottom() {
        return cards[0];
    }

}
