package game.cards;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class used for managing the main card stack where users pull cards from
 *
 * @author Wesley Klop
 */
public class CardStackController implements Serializable {
//    private transient static final Logger Log = LogManager.getLogger(CardStackController.class);
    /**
     * Default amount of locomotives in a traincardstack
     */
    private static final int DEFAULT_LOCOMOTIVE_COUNT = 14;
    /**
     * Default amount of carts in a traincardstack
     */
    private static final int DEFAULT_CART_COUNT = 12;
    /**
     * Internal cardstack
     */
    private CardStack stack = new CardStack();
    private Card[] openCards = new Card[5];

    /**
     * Creates a new CardStackController, pre-fills the cardstack with the default amount of locomotives and carts
     */
    public CardStackController() {
        generateTrainCards();
    }

    public void populateOpenCards() {
        for (int i = 0; i < openCards.length; i++) {
            openCards[i] = getRandomCard();
        }
    }

    /**
     * Get the internal cardstack to access its methods
     *
     * @return the internal CardStack
     */
    public CardStack getStack() {
        return stack;
    }

    /**
     * Fills the map with the correct values for train cards
     */
    private void generateTrainCards() {
        // Feels kind of hacky tbh
        for (CardType type : CardType.values()) {
            if (type == CardType.LOCOMOTIVE) {
                stack.put(type, DEFAULT_LOCOMOTIVE_COUNT);
            } else if (type != CardType.CART_ANY) {
                stack.put(type, DEFAULT_CART_COUNT);
            }
        }
    }

    /**
     * Get a random card from the stack
     * Side effects: Generates new stack when there are no game.cards left
     *
     * @return a random Card
     */
    public Card getRandomCard() {
        if (stack.isEmpty()) {
            generateTrainCards();
        }

        // Get a random game.cards.CardType from the stack
        Random random = new Random();
        List<CardType> keys = new ArrayList<>(stack.keySet());

        CardType randomCardType = keys.get(random.nextInt(keys.size()));

        // Decrement the amount of that type in the map
        stack.put(randomCardType, stack.get(randomCardType) - 1);

        // Remove from map when game.cards are out
        if (stack.get(randomCardType) == 0)
            stack.remove(randomCardType);

        return new Card(randomCardType);
    }

    /**
     * Get an open card from the cardstack, and replace the old one with a random one
     *
     * @param index the position of the open card that was clicked [0..4]
     * @return the open card
     */
    public Card getOpenCard(int index) {
        Card card = openCards[index];
        openCards[index] = getRandomCard();
        return card;
    }

    public Card[] getOpenCards() {
        return openCards;
    }
}
