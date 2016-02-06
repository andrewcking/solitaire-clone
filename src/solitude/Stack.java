package solitude;

/**
 *
 * @author Andrew
 *  stacks of feathered cards as opposed to piles
 */
public class Stack extends Pile {

    public Stack() {
        super();
    }

    @Override
    public void add(Card card) {
        if (!isFull()) {
            cards[count++] = card;
            top = card;
            add(top, 0, count * 30 - 30);
        }
    }

}
