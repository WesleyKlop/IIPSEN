package game.routecards;

import java.io.Serializable;

public class RouteCardStackSelected implements Serializable {

    private static final int PICKABLE_CARD_COUNT = 3;
    private RouteCardStackBank bank;
    private RouteCard[] pickableCards = new RouteCard[PICKABLE_CARD_COUNT];

    public void RouteCardStackSelected(RouteCardStackBank bank) {
        this.bank = bank;
        refillFromBank();
    }


    private void refillFromBank() {
        for (int i = 0; i < PICKABLE_CARD_COUNT; i++) {
            pickableCards[i] = bank.getRandomRouteCard();
        }
    }

    public RouteCard[] getPickableCards() {
        return pickableCards;
    }
}