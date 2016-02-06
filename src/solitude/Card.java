package solitude;

import acm.graphics.GCompound;
import acm.graphics.GImage;
import acm.graphics.GRect;
import java.awt.Color;

/**
 *
 * @author Andrew
 */
public final class Card extends GCompound {

    private final int suit;
    private final int face;
    private boolean faceUp;
    private final GImage image;
    private final GImage back;

    public static String[] SUIT_NAME = {"Clubs", "Spades", "Diamonds", "Hearts"};
    public static String[] FACE_NAME = {"Ace", "Two", "Three", "Four", "Five", "Six",
        "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
    //used in pile
    public static final int CARDTOTAL = 52;

    public Card(int suit, int face) {
        this.suit = suit;
        this.face = face;
        String filename = "cardgifs/" + SUIT_NAME[suit].toLowerCase().charAt(0) + (face + 1) + ".gif";
        String filenameBack = "cardgifs/back.gif";
        image = new GImage(filename);
        back = new GImage(filenameBack);
        //GRect(image.getWidth() + 3, image.getHeight() + 3);
        //back.setFilled(true);
        //back.setFillColor(new Color(237, 31, 55));
        add(back);
        add(image, 2, 2);
        setFaceUp(true);
    }

    public boolean isFaceUp() {
        return faceUp;
    }

    public void setFaceUp(boolean faceUp) {
        this.faceUp = faceUp;
        image.setVisible(faceUp);
    }

    public int getFace() {
        return face;
    }

    public int getSuit() {
        return suit;
    }

}
