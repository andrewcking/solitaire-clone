package solitude;

import acm.graphics.GRect;
import acm.util.RandomGenerator;

/**
 *
 * @author Andrew
 */
public class Deck extends Pile {

    public Deck(boolean defaultShuffle) {
        count = 0;
        for (int suit = 0; suit < Card.SUIT_NAME.length; ++suit) {
            for (int face = 0; face < Card.FACE_NAME.length; ++face) {
                cards[count] = new Card(suit, face);
                cards[count].setFaceUp(false);
                ++count;
            }
        }
        if (defaultShuffle) {
            shuffle();
        }
        showCards();
    }



    private void shuffle() {
        if (!isEmpty()) {
            RandomGenerator r = RandomGenerator.getInstance();
            for (int i = 0; i < count; ++i) {
                int j = r.nextInt(0, count - 1);
                Card temp = cards[j];
                cards[j] = cards[i];
                cards[i] = temp;
            }
            showCards();
        }
    }

    private void showCards() {
        this.removeAll();
        GRect border = new GRect(109, 152);
        add(border, 0, 0);
        if (!isEmpty()) {
            for (int i = 0; i < count; ++i) {
                add(cards[i], 0, 0);
            }
            top = cards[count - 1];
        }
    }
    
    public Card deal() {
        return remove();
    }
    
    public Deck() {
        this(true);
    }
}
