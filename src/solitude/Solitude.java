package solitude;

import acm.graphics.GImage;
import acm.graphics.GLabel;
import acm.program.GraphicsProgram;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

/**
 *
 * @author Andrew
 */
public class Solitude extends GraphicsProgram {

    public static final int WWIDTH = 900;
    public static final int WHEIGHT = 750;

    public static Deck deck;
    private Pile waste;

    private Card currentCard;
    private Stack currentStack;

    private Pile activePile;
    private Stack tempStack;

    private Pile[] home = new Pile[4];
    private Stack[] stack = new Stack[7];

    private boolean validPosition = true;
    private boolean skip = false;

    private GLabel winLabel;

    @Override
    public void run() {
        this.resize(WWIDTH, WHEIGHT);
    }

    @Override
    public void init() {
        //background image
        GImage background = new GImage("cardgifs/greenfelt.jpg");
        add(background, 0, 0);
        //set background color
        setBackground(new Color(64, 180, 64));
        //create stock pile
        deck = new Deck();
        add(deck, 600, 20);
        //create waste pile as an offset to stock
        waste = new Pile();
        add(waste, deck.getX() + 120, deck.getY());
        //create foundations
        for (int n = 0; n < 4; n++) {
            home[n] = new Pile();
            add(home[n], 20 + n * 120, 20);
        }
        //create tableau
        for (int t = 0; t < 7; ++t) {
            stack[t] = new Stack();
            for (int n = 0; n < t + 1; ++n) {
                stack[t].add(deck.deal());
            }
            add(stack[t], 20 + t * 120, 200);
            stack[t].top.setFaceUp(true);
        }
        //add mouse listeners
        addMouseListeners();
    }

    @Override
    public void mousePressed(MouseEvent event) {
        //mouse press on stock
        if (deck.contains(event.getX(), event.getY())) {
            if (!deck.isEmpty()) {
                Card card = deck.deal();
                card.setFaceUp(true);
                waste.add(card);
            } else {
                while (!waste.isEmpty()) {
                    Card card = waste.remove();
                    card.setFaceUp(false);
                    deck.add(card);
                }
            }
        }
        //mouse press on waste
        if (waste.contains(event.getX(), event.getY())) {
            if (!waste.isEmpty()) {
                if (currentCard == null && currentStack == null) {
                    currentCard = waste.remove();
                    currentCard.setLocation(event.getX(), event.getY());
                    activePile = waste;
                }
            }
        }
        //mouse press on foundation
        for (int n = 0; n < 4; n++) {
            if (home[n].contains(event.getX(), event.getY())) {
                if (currentCard == null && currentStack == null) {
                    currentCard = home[n].remove();
                    currentCard.setLocation(event.getX(), event.getY());
                    activePile = home[n];
                }
            }
        }
        // mouse press on tableau
        for (int i = 0; i < 7; ++i) {
            if (stack[i].contains(event.getX(), event.getY())) {
                if (stack[i].top.isFaceUp() == false) {
                    stack[i].top.setFaceUp(true);
                    break;
                } else if (currentStack == null && currentCard == null) {
                    double click = ((event.getY() - stack[i].getY()) / 30);
                    if (click > stack[i].size()) {
                        click = stack[i].size();
                    }
                    currentStack = new Stack();
                    tempStack = new Stack();
                    int tempSize = stack[i].size();
                    for (int q = tempSize; q >= click; --q) {
                        tempStack.add(stack[i].remove());
                    }
                    tempSize = tempStack.size();
                    for (int q = 0; q < tempSize; ++q) {
                        currentStack.add(tempStack.remove());
                    }
                    if (currentStack.size() == 1) {
                        currentCard = currentStack.cards[0];
                        currentStack = null;
                        currentCard.setLocation(event.getX(), event.getY());
                        activePile = stack[i];
                    } else {
                        currentStack.setLocation(event.getX(), event.getY());
                        activePile = stack[i];
                    }
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        validPosition = false;

        //For individual cards
        if (currentCard != null) {
            //release card on foundation
            for (int t = 0; t < 4; ++t) {
                if (home[t].contains(event.getX(), event.getY())) {
                    //if empty and ace(0) place
                    if (home[t].top == null && currentCard.getFace() == 0) {
                        home[t].add(currentCard);
                        currentCard = null;
                        validPosition = true;
                        break;
                        // if home pile is null and above condition was not met break out otherwise exception
                    } else if (home[t].top == null) {
                        break;
                        //if current card+1 and of same suit place
                    } else if ((currentCard.getFace() == home[t].top.getFace() + 1 && currentCard.getSuit() == home[t].top.getSuit())) {
                        home[t].add(currentCard);
                        currentCard = null;
                        validPosition = true;
                        //Win condition!
                        if (home[0].isFoundationFull() && home[1].isFoundationFull() && home[2].isFoundationFull() && home[3].isFoundationFull()) {
                            winLabel = new GLabel("You Win!");
                            winLabel.setFont("Times-bold-100");
                            add(winLabel, (WWIDTH - winLabel.getWidth()) / 2, 400);
                        }
                    }
                }
            }
            //release card on tableau
            for (int t = 0; t < 7; ++t) {
                if (stack[t].contains(event.getX(), event.getY())) {
                    //if card is king and stack is empty allow
                    if (stack[t].top == null) {
                        if (currentCard.getFace() == 12) {
                            stack[t].add(currentCard);
                            currentCard = null;
                            validPosition = true;
                        } else {
                            skip = true;
                        }
                        //if card is red and stack card is black and card is one more than it allow 
                    } else if (skip == false && (stack[t].top.getSuit() == 1 || stack[t].top.getSuit() == 0)) {
                        if ((stack[t].top.isFaceUp() == true && stack[t].top.getFace() == currentCard.getFace() + 1 && currentCard.getSuit() >= 2) || (currentCard.getSuit() == 11 && stack[t].top == null)) {
                            stack[t].add(currentCard);
                            currentCard = null;
                            validPosition = true;
                        }
                        //if card is black and stack card is red and card is one more than it allow 
                    } else if (skip == false && (stack[t].top.getSuit() == 2 || stack[t].top.getSuit() == 3)) {
                        if ((stack[t].top.isFaceUp() == true && stack[t].top.getFace() == currentCard.getFace() + 1 && currentCard.getSuit() < 2) || (currentCard.getSuit() == 11 && stack[t].top == null)) {
                            stack[t].add(currentCard);
                            currentCard = null;
                            validPosition = true;
                        }
                    }
                }
            }
        }

        //For stacks... oh goodness
        if (currentStack != null) {
            //release stack on tableau
            for (int t = 0; t < 7; ++t) {
                if (stack[t].contains(event.getX(), event.getY())) {
                    //if stack (where top is king) and you are dropping on empty tableau pile, allow
                    if (stack[t].top == null) {
                        if (currentStack.getBottom().getFace() == 12) {
                            int tempSize = currentStack.size();
                            for (int v = 0; v < tempSize; ++v) {
                                tempStack.add(currentStack.remove());
                            }
                            tempSize = tempStack.size();
                            for (int v = 0; v < tempSize; ++v) {
                                stack[t].add(tempStack.remove());
                            }
                            currentStack.removeAll();
                            currentStack = null;
                            validPosition = true;
                        } else {
                            skip = true;
                        }
                        //if stack (where top is black) and you are dropping on card that is one greater than top and is red, allow
                    } else if (skip == false && (stack[t].top.getSuit() == 1 || stack[t].top.getSuit() == 0)) {
                        if ((stack[t].top.isFaceUp() == true && stack[t].top.getFace() == currentStack.getBottom().getFace() + 1 && currentStack.getBottom().getSuit() >= 2)) {
                            int tempSize = currentStack.size();
                            for (int v = 0; v < tempSize; ++v) {
                                tempStack.add(currentStack.remove());
                            }
                            tempSize = tempStack.size();
                            for (int v = 0; v < tempSize; ++v) {
                                stack[t].add(tempStack.remove());
                            }
                            currentStack.removeAll();
                            currentStack = null;
                            validPosition = true;
                        }
                        //if stack (where top is red) and you are dropping on card that is one greater than top and is black, allow
                    } else if (skip == false && (stack[t].top.getSuit() == 2 || stack[t].top.getSuit() == 3)) {
                        if ((stack[t].top.isFaceUp() == true && stack[t].top.getFace() == currentStack.getBottom().getFace() + 1 && currentStack.getBottom().getSuit() < 2)) {
                            int tempSize = currentStack.size();
                            for (int v = 0; v < tempSize; ++v) {
                                tempStack.add(currentStack.remove());
                            }
                            tempSize = tempStack.size();
                            for (int v = 0; v < tempSize; ++v) {
                                stack[t].add(tempStack.remove());
                            }
                            currentStack.removeAll();
                            currentStack = null;
                            validPosition = true;
                        }
                    }
                }
            }
        }
        // Replace card if attempt to drop in invalid place
        if (validPosition == false && currentCard != null) {
            activePile.add(currentCard);
            currentCard = null;
        }
        // Replace stack if attempt to drop in invalid place
        if (validPosition == false && currentStack != null) {
            int tempSize = currentStack.size();
            for (int v = 0; v < tempSize; ++v) {
                tempStack.add(currentStack.remove());
            }
            tempSize = tempStack.size();
            for (int v = 0; v < tempSize; ++v) {
                activePile.add(tempStack.remove());
            }
            currentStack.removeAll();
            currentStack = null;
        }
        skip = false;
    }
    @Override
    public void mouseDragged(MouseEvent e){
        //not working
    }
   
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new Solitude().start(args);
    }

}
