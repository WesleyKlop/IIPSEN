package cards;

import javafx.scene.control.Button;

/**
 * @author Wesley
 * CardButton
 */
public class CardButton extends Button {
    /**
     * listener the object that should be called on click
     */
    protected OnCardSelectListener listener;

    /**
     * Card card the card
     */
    private Card card;

    /**
     * Empty constructor for extending classes
     *
     * @see RandomCardButton
     */
    protected CardButton() {
    }

    /**
     * Constructor for normal card buttons
     *
     * @param card the card that the button links to
     */
    public CardButton(Card card) {
        super(card.toString());
        this.card = card;
        super.setOnAction(e -> listener.onCardSelected(this));
    }

    /**
     * Sets the click listener
     *
     * @param listener the object that implements OnCardSelectListener
     */
    public void setOnAction(OnCardSelectListener listener) {
        this.listener = listener;
    }

    /**
     * Get the card contained in the button
     *
     * @return the card
     */
    public Card getCard() {
        return this.card;
    }

    /**
     * Sets the card text and card object
     *
     * @param card the card to use
     */
    public void setCard(Card card) {
        this.setText(card.toString());
        this.card = card;
    }

}
