package routecards;

import cards.CardType;

/**
 * @author wesley
 */
public class Route {
    private int length;
    private int locomotiveCost;
    private EPlayer owner;
    private CardType color;

    public Route(int length, int locomotiveCost, CardType color) {
        this.length = length;
        this.locomotiveCost = locomotiveCost;
        this.color = color;
    }

    public void build(Player builder) {
        System.out.println(color + " " + getCartCost() + " " + getLocomotiveCost());
        var costsLocomotives = getLocomotiveCost() > 0;
        var costsCarts = getCartCost() > 0;
        if ((costsCarts || builder.getCardStack().containsCards(color, getCartCost())) &&
            (costsLocomotives || builder.getCardStack().containsCards(CardType.LOCOMOTIVE, locomotiveCost))
        ) {
            try {
                if (costsCarts)
                    builder.getCardStack().takeCards(color, getCartCost());
                if (costsLocomotives)
                    builder.getCardStack().takeCards(CardType.LOCOMOTIVE, locomotiveCost);

                builder.removeTrainCarts(length);
                this.owner = builder.getPlayer();
                System.out.println("Route build! by " + builder.getPlayerName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Player doesnt have the needed cards!!");
            System.out.println(builder.getCardStack());
        }
    }

    public boolean hasOwner() {
        return owner != null;
    }

    public int getCartCost() {
        return length - locomotiveCost;
    }

    public int getLocomotiveCost() {
        return locomotiveCost;
    }
}
